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
import java.util.zip.ZipEntry;

import javax.swing.JComponent;

import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.port.AbstractPortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

public class ChromatogramSelectionPortObject extends AbstractPortObject {

	public static final class Serializer extends AbstractPortObjectSerializer<ChromatogramSelectionPortObject> {
	}

	/** Convenience accessor for the port type. */
	public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionPortObject.class);
	//
	private IChromatogramSelection chromatogramSelection;
	private ChromatogramSelectionPortObjectSpec spec;

	public ChromatogramSelectionPortObject(IChromatogramSelection chromatogramSelection) {
		this.chromatogramSelection = chromatogramSelection;
		this.spec = new ChromatogramSelectionPortObjectSpec();
	}

	public IChromatogramSelection getChromatogramSelection() {

		return chromatogramSelection;
	}

	protected void setChromatogramSelection(IChromatogramSelection chromatogramSelection) {

		this.chromatogramSelection = chromatogramSelection;
	}

	@Override
	public String getSummary() {

		return "Chromatogram Selection";
	}

	@Override
	public PortObjectSpec getSpec() {

		return this.spec;
	}

	@Override
	public JComponent[] getViews() {

		return null;
	}

	@Override
	protected void save(PortObjectZipOutputStream out, ExecutionMonitor exec) throws IOException, CanceledExecutionException {
		out.putNextEntry(new ZipEntry("chromatogram-selection"));
		ObjectOutputStream outputStream = new ObjectOutputStream(out);
		outputStream.writeObject(chromatogramSelection);
	}

	@Override
	protected void load(PortObjectZipInputStream in, PortObjectSpec spec, ExecutionMonitor exec) throws IOException, CanceledExecutionException {
		ZipEntry nextEntry = in.getNextEntry();
		if(!nextEntry.getName().equals("chromatogram-selection")) {
			throw new IOException("expected chromatogram-selection, got " + nextEntry.getName());
		}
		ObjectInputStream inputStream = new ObjectInputStream(in);
		try {
			chromatogramSelection = (IChromatogramSelection)inputStream.readObject();
		} catch(ClassNotFoundException e) {
			System.out.println(e);
		}
	}
}
