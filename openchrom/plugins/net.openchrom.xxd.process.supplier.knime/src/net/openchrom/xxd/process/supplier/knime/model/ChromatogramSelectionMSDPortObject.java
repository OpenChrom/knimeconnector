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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JComponent;

import org.apache.commons.io.IOUtils;
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

import net.openchrom.xxd.process.supplier.knime.model.exceptions.InvalidDataException;

public class ChromatogramSelectionMSDPortObject extends AbstractPortObject {

	public static final class Serializer extends AbstractPortObjectSerializer<ChromatogramSelectionMSDPortObject> {
	}

	public static final ChromatogramSelectionMSD EMPTY_CHROMATOGRAM_SELECTION = new ChromatogramSelectionMSD(new ChromatogramMSD());
	private static final String CHROMATOGRAM_SELECTION_DATA = "CHROMATOGRAM_SELECTION_DATA";
	private static final String CHROMATOGRAM_SELECTION_HEADER = "CHROMATOGRAM_SELECTION_HEADER";
	private static final String CHROMATOGRAM_SELECTION_SETTINGS = "CHROMATOGRAM_SELECTION_SETTINGS";
	public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionMSDPortObject.class);
	public static final PortType TYPE_OPTIONAL = PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionMSDPortObject.class, true);
	//
	private IChromatogramSelectionMSD chromatogramSelectionMSD;
	private InputStream chromatogramSelectionZipInputStream;
	private ChromatogramSelectionMSDPortObjectSpec portObjectSpec;
	private float startAbundance;
	private int startRetentionTime;
	private float stopAbundance;
	private int stopRetentionTime;

	public ChromatogramSelectionMSDPortObject() {
		this(EMPTY_CHROMATOGRAM_SELECTION);
	}

	public ChromatogramSelectionMSDPortObject(IChromatogramSelectionMSD chromatogramSelectionMSD) {
		this.chromatogramSelectionMSD = chromatogramSelectionMSD;
		this.portObjectSpec = new ChromatogramSelectionMSDPortObjectSpec();
	}

	protected IChromatogramSelectionMSD extractChromatogramSelectionMSD() throws IOException {

		if(chromatogramSelectionMSD == null) {
			ChromatogramReaderMSD chromatogramReaderMSD = new ChromatogramReaderMSD();
			IChromatogramMSD chromatogramMSD = chromatogramReaderMSD.read(new ZipInputStream(chromatogramSelectionZipInputStream), new NullProgressMonitor());
			chromatogramSelectionMSD = new ChromatogramSelectionMSD(chromatogramMSD);
			chromatogramSelectionMSD.setStartRetentionTime(startRetentionTime);
			chromatogramSelectionMSD.setStopRetentionTime(stopRetentionTime);
			chromatogramSelectionMSD.setStartAbundance(startAbundance);
			chromatogramSelectionMSD.setStopRetentionTime(stopRetentionTime);
			return chromatogramSelectionMSD;
		} else {
			return chromatogramSelectionMSD;
		}
	}

	public IChromatogramSelectionMSD getChromatogramSelectionMSD() throws IOException, InvalidDataException {

		if(this.portObjectSpec.getProcessingMode().equals(ChromatogramSelectionMSDPortObjectSpec.MODE_IMMEDIATE_PROCESSING)) {
			return extractChromatogramSelectionMSD();
		} else {
			throw new InvalidDataException();
		}
	}

	@Override
	public ChromatogramSelectionMSDPortObjectSpec getSpec() {

		return this.portObjectSpec;
	}

	@Override
	public String getSummary() {

		return "Chromatogram Selection (MSD)";
	}

	@Override
	public JComponent[] getViews() {

		return null;
	}

	@Override
	protected void load(PortObjectZipInputStream in, PortObjectSpec spec, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

		ChromatogramSelectionMSDPortObjectSpec chromatogramSelectionMSDPortObjectSpec = (ChromatogramSelectionMSDPortObjectSpec)spec;
		this.portObjectSpec.setProcessingMode(chromatogramSelectionMSDPortObjectSpec.getProcessingMode());
		//
		ZipEntry zipEntry = in.getNextEntry();
		assert zipEntry.getName().equals(CHROMATOGRAM_SELECTION_HEADER);
		int typeFlag = in.read();
		if(typeFlag == 0) {
			// empty chromatogram selection
			chromatogramSelectionMSD = EMPTY_CHROMATOGRAM_SELECTION;
			return;
		}
		//
		zipEntry = in.getNextEntry();
		assert zipEntry.getName().equals(CHROMATOGRAM_SELECTION_DATA);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		IOUtils.copy(in, outputStream);
		chromatogramSelectionZipInputStream = new ByteArrayInputStream(outputStream.toByteArray());
		chromatogramSelectionMSD = null;
		//
		zipEntry = in.getNextEntry();
		assert zipEntry.getName().equals(CHROMATOGRAM_SELECTION_SETTINGS);
		DataInputStream dataInputStream = new DataInputStream(in);
		this.startRetentionTime = dataInputStream.readInt();
		this.stopRetentionTime = dataInputStream.readInt();
		this.startAbundance = dataInputStream.readFloat();
		this.stopAbundance = dataInputStream.readFloat();
	}

	@Override
	protected void save(PortObjectZipOutputStream out, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

		ZipEntry zipEntry = new ZipEntry(CHROMATOGRAM_SELECTION_HEADER);
		out.putNextEntry(zipEntry);
		if(this.chromatogramSelectionMSD == EMPTY_CHROMATOGRAM_SELECTION) {
			out.write(0);
			out.closeEntry();
			return;
		} else {
			out.write(1);
		}
		//
		zipEntry = new ZipEntry(CHROMATOGRAM_SELECTION_DATA);
		out.putNextEntry(zipEntry);
		ZipOutputStream zipOutputStream = new ZipOutputStream(out);
		if(chromatogramSelectionMSD != null) {
			ChromatogramWriterMSD chromatogramWriterMSD = new ChromatogramWriterMSD();
			chromatogramWriterMSD.writeChromatogram(zipOutputStream, chromatogramSelectionMSD.getChromatogramMSD(), new NullProgressMonitor());
		} else {
			IOUtils.copy(chromatogramSelectionZipInputStream, zipOutputStream);
		}
		zipOutputStream.flush();
		//
		zipEntry = new ZipEntry(CHROMATOGRAM_SELECTION_SETTINGS);
		out.putNextEntry(zipEntry);
		DataOutputStream dataOutputStream = new DataOutputStream(out);
		if(chromatogramSelectionMSD != null) {
			dataOutputStream.writeInt(chromatogramSelectionMSD.getStartRetentionTime());
			dataOutputStream.writeInt(chromatogramSelectionMSD.getStopRetentionTime());
			dataOutputStream.writeFloat(chromatogramSelectionMSD.getStartAbundance());
			dataOutputStream.writeFloat(chromatogramSelectionMSD.getStopAbundance());
		} else {
			dataOutputStream.writeInt(startRetentionTime);
			dataOutputStream.writeInt(stopRetentionTime);
			dataOutputStream.writeFloat(startAbundance);
			dataOutputStream.writeFloat(stopAbundance);
		}
		dataOutputStream.flush();
		out.closeEntry();
	}
}
