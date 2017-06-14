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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;

import javax.swing.JComponent;

import org.eclipse.chemclipse.msd.converter.supplier.chemclipse.io.ChromatogramReaderMSD;
import org.eclipse.chemclipse.msd.converter.supplier.chemclipse.io.ChromatogramWriterMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.core.runtime.NullProgressMonitor;
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
	private static final String CHROMATOGRAM_SELECTION = "CHROMATOGRAM_SELECTION";
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

		ChromatogramWriterMSD chromatogramWriterMSD = new ChromatogramWriterMSD();
		chromatogramWriterMSD.writeChromatogram(out, getChromatogramSelectionMSD().getChromatogramMSD(), new NullProgressMonitor());
		//
		ZipEntry zipEntry = new ZipEntry(CHROMATOGRAM_SELECTION);
		out.putNextEntry(zipEntry);
		DataOutputStream dataOutputStream = new DataOutputStream(out);
		dataOutputStream.writeInt(chromatogramSelectionMSD.getStartRetentionTime());
		dataOutputStream.writeInt(chromatogramSelectionMSD.getStopRetentionTime());
		dataOutputStream.writeFloat(chromatogramSelectionMSD.getStartAbundance());
		dataOutputStream.writeFloat(chromatogramSelectionMSD.getStopAbundance());
		dataOutputStream.flush();
		out.closeEntry();
	}

	@Override
	protected void load(PortObjectZipInputStream in, PortObjectSpec spec, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

		ChromatogramReaderMSD chromatogramReaderMSD = new ChromatogramReaderMSD();
		IChromatogramMSD chromatogramMSD = chromatogramReaderMSD.read(in, new NullProgressMonitor());
		chromatogramSelectionMSD = new ChromatogramSelectionMSD(chromatogramMSD);
		chromatogramSelectionMSD.setStartRetentionTime(in.read());
		chromatogramSelectionMSD.setStopRetentionTime(in.read());
		//
		ZipEntry zipEntry = in.getNextEntry();
		if(zipEntry != null && !zipEntry.isDirectory()) {
			String name = zipEntry.getName();
			if(name.equals(CHROMATOGRAM_SELECTION)) {
				DataInputStream dataInputStream = new DataInputStream(in);
				chromatogramSelectionMSD.setStartRetentionTime(dataInputStream.readInt());
				chromatogramSelectionMSD.setStopRetentionTime(dataInputStream.readInt());
				chromatogramSelectionMSD.setStartAbundance(dataInputStream.readFloat());
				chromatogramSelectionMSD.setStopAbundance(dataInputStream.readFloat());
			}
		}
	}
}
