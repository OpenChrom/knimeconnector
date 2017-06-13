/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 * 
 * This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;

import javax.swing.JComponent;

import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.ScanMSD;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.port.AbstractPortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

public class ChromatogramSelectionMSDPortObject extends AbstractPortObject {

	public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionMSDPortObject.class);
	private static final String ID_SERIALIZED_ENTRY = "CS-MSD";
	//
	private IChromatogramSelectionMSD chromatogramSelectionMSD;
	private ChromatogramSelectionMSDPortObjectSpec portObjectSpec;

	public static final class Serializer extends AbstractPortObjectSerializer<ChromatogramSelectionMSDPortObject> {
	}

	public ChromatogramSelectionMSDPortObject() {
		this(new ChromatogramSelectionMSD(new ChromatogramMSD()));
	}

	public ChromatogramSelectionMSDPortObject(IChromatogramSelectionMSD chromatogramSelectionMSD) {
		this.chromatogramSelectionMSD = chromatogramSelectionMSD;
		this.portObjectSpec = new ChromatogramSelectionMSDPortObjectSpec();
	}

	public IChromatogramSelectionMSD getChromatogramSelectionMSD() {

		return chromatogramSelectionMSD;
	}

	@Override
	public String getSummary() {

		return "Chromatogram Selection (MSD)";
	}

	@Override
	public PortObjectSpec getSpec() {

		return this.portObjectSpec;
	}

	@Override
	public JComponent[] getViews() {

		return null;
	}

	@Override
	protected void save(PortObjectZipOutputStream out, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

		out.putNextEntry(new ZipEntry(ID_SERIALIZED_ENTRY));
		List<IScan> scans = getChromatogramSelectionMSD().getChromatogram().getScans();
		List<IScanMSD> scansMSD = new ArrayList<IScanMSD>();
		for(IScan scan : scans) {
			if(scan instanceof IScanMSD) {
				IScanMSD scanMSD = new ScanMSD((IScanMSD)scan);
				scansMSD.add(scanMSD);
			}
		}
		ObjectOutputStream outputStream = new ObjectOutputStream(out);
		outputStream.writeObject(scansMSD);
	}

	@Override
	protected void load(PortObjectZipInputStream in, PortObjectSpec spec, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

		ZipEntry nextEntry = in.getNextEntry();
		if(!nextEntry.getName().equals(ID_SERIALIZED_ENTRY)) {
			throw new IOException("Expected " + ID_SERIALIZED_ENTRY + ", but got " + nextEntry.getName());
		}
		ObjectInputStream inputStream = new ObjectInputStream(in);
		try {
			@SuppressWarnings("unchecked")
			List<IScan> scans = (List<IScan>)inputStream.readObject();
			IChromatogramMSD chromatogramMSD = new ChromatogramMSD();
			chromatogramMSD.addScans(scans);
			chromatogramSelectionMSD = new ChromatogramSelectionMSD(chromatogramMSD);
		} catch(ClassNotFoundException e) {
			System.out.println(e);
		}
	}
}
