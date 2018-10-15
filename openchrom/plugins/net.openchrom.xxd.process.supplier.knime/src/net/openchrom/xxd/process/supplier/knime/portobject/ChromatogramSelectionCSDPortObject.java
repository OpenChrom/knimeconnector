/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
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
package net.openchrom.xxd.process.supplier.knime.portobject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JComponent;

import org.apache.commons.io.IOUtils;
import org.eclipse.chemclipse.csd.converter.supplier.chemclipse.io.ChromatogramReaderCSD;
import org.eclipse.chemclipse.csd.converter.supplier.chemclipse.io.ChromatogramWriterCSD;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.csd.model.core.selection.ChromatogramSelectionCSD;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.csd.model.implementation.ChromatogramCSD;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.port.AbstractPortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

import net.openchrom.xxd.process.supplier.knime.model.IChromatogramSelectionProcessing;
import net.openchrom.xxd.process.supplier.knime.model.exceptions.InvalidDataException;

public class ChromatogramSelectionCSDPortObject extends AbstractPortObject {

	public static final class Serializer extends AbstractPortObjectSerializer<ChromatogramSelectionCSDPortObject> {
	}

	public static final ChromatogramSelectionCSD EMPTY_CHROMATOGRAM_SELECTION = new ChromatogramSelectionCSD(new ChromatogramCSD());
	private static final String CHROMATOGRAM_SELECTION_DATA = "CHROMATOGRAM_SELECTION_DATA";
	private static final String CHROMATOGRAM_SELECTION_HEADER = "CHROMATOGRAM_SELECTION_HEADER";
	private static final String CHROMATOGRAM_SELECTION_PROCESSING = "CHROMATOGRAM_SELECTION_PROCESSING";
	private static final String CHROMATOGRAM_SELECTION_SETTINGS = "CHROMATOGRAM_SELECTION_SETTINGS";
	public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionCSDPortObject.class);
	public static final PortType TYPE_OPTIONAL = PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionCSDPortObject.class, true);
	private byte[] chromatogramByteArray;
	//
	private IChromatogramSelectionCSD chromatogramSelectionCSD;
	private boolean chromatogramUpdate;
	private ChromatogramSelectionCSDPortObjectSpec portObjectSpec;
	List<IChromatogramSelectionProcessing<? super IChromatogramSelectionCSD>> processing = new ArrayList<>();
	private float startAbundance;
	private int startRetentionTime;
	private float stopAbundance;
	private int stopRetentionTime;
	private File inputFile;

	public ChromatogramSelectionCSDPortObject() throws IOException {
		this(EMPTY_CHROMATOGRAM_SELECTION);
	}

	public ChromatogramSelectionCSDPortObject(IChromatogramSelectionCSD chromatogramSelectionCSD) throws IOException {
		this.chromatogramSelectionCSD = chromatogramSelectionCSD;
		this.portObjectSpec = new ChromatogramSelectionCSDPortObjectSpec();
		this.inputFile = chromatogramSelectionCSD.getChromatogram().getFile();
	}

	public void addProcessings(IChromatogramSelectionProcessing<? super IChromatogramSelectionCSD> chromatogramSelectionProcessing) {

		processing.add(chromatogramSelectionProcessing);
	}

	private byte[] convertChromatogram(IChromatogramCSD chromatogramCSD) throws IOException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(bos);
		ZipEntry entry = new ZipEntry("Chromatogram");
		zipOutputStream.putNextEntry(entry);
		ChromatogramWriterCSD chromatogramWriterCSD = new ChromatogramWriterCSD();
		chromatogramWriterCSD.writeChromatogram(zipOutputStream, "", chromatogramSelectionCSD.getChromatogramCSD(), new NullProgressMonitor());
		zipOutputStream.closeEntry();
		zipOutputStream.close();
		return bos.toByteArray();
	}

	IChromatogramSelectionCSD extractChromatogramSelectionCSD() throws IOException {

		if(chromatogramSelectionCSD == null) {
			ChromatogramReaderCSD chromatogramReaderCSD = new ChromatogramReaderCSD();
			IChromatogramCSD chromatogramCSD = chromatogramReaderCSD.read(new ZipInputStream(new ByteArrayInputStream(chromatogramByteArray)), "", new NullProgressMonitor());
			chromatogramCSD.setFile(inputFile);
			chromatogramSelectionCSD = new ChromatogramSelectionCSD(chromatogramCSD);
			chromatogramSelectionCSD.setStartRetentionTime(startRetentionTime);
			chromatogramSelectionCSD.setStopRetentionTime(stopRetentionTime);
			chromatogramSelectionCSD.setStartAbundance(startAbundance);
			chromatogramSelectionCSD.setStopRetentionTime(stopRetentionTime);
			return chromatogramSelectionCSD;
		} else {
			return chromatogramSelectionCSD;
		}
	}

	public IChromatogramSelectionCSD getChromatogramSelectionCSD() throws IOException, InvalidDataException {

		if(this.portObjectSpec.getProcessingMode().equals(ChromatogramSelectionCSDPortObjectSpec.MODE_IMMEDIATE_PROCESSING)) {
			return extractChromatogramSelectionCSD();
		} else {
			throw new InvalidDataException("To avoid this exception add a node \"End Processing\", before the node where the exception occured.");
		}
	}

	public byte[] getChromatogramByteArray() throws InvalidDataException {

		if(this.portObjectSpec.getProcessingMode().equals(ChromatogramSelectionCSDPortObjectSpec.MODE_IMMEDIATE_PROCESSING)) {
			return chromatogramByteArray;
		} else {
			throw new InvalidDataException("To avoid this exception add a node \"End Processing\", before the node where the exception occured.");
		}
	}

	@Override
	public ChromatogramSelectionCSDPortObjectSpec getSpec() {

		return this.portObjectSpec;
	}

	@Override
	public String getSummary() {

		return "Chromatogram Selection (CSD)";
	}

	@Override
	public JComponent[] getViews() {

		return null;
	}

	public void chromatogramSelectionUpdate() {

		chromatogramUpdate = true;
	}

	@Override
	protected void load(PortObjectZipInputStream in, PortObjectSpec spec, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

		ChromatogramSelectionCSDPortObjectSpec chromatogramSelectionCSDPortObjectSpec = (ChromatogramSelectionCSDPortObjectSpec)spec;
		this.portObjectSpec.setProcessingMode(chromatogramSelectionCSDPortObjectSpec.getProcessingMode());
		//
		ZipEntry zipEntry = in.getNextEntry();
		assert zipEntry.getName().equals(CHROMATOGRAM_SELECTION_HEADER);
		int typeFlag = in.read();
		if(typeFlag == 0) {
			// empty chromatogram selection
			chromatogramSelectionCSD = EMPTY_CHROMATOGRAM_SELECTION;
			return;
		}
		ObjectInputStream objectInputStream = new ObjectInputStream(in);
		try {
			inputFile = (File)objectInputStream.readObject();
		} catch(ClassNotFoundException e) {
			throw new IOException(e);
		}
		//
		zipEntry = in.getNextEntry();
		assert zipEntry.getName().equals(CHROMATOGRAM_SELECTION_DATA);
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		IOUtils.copy(in, arrayOutputStream);
		chromatogramByteArray = arrayOutputStream.toByteArray();
		chromatogramSelectionCSD = null;
		//
		zipEntry = in.getNextEntry();
		assert zipEntry.getName().equals(CHROMATOGRAM_SELECTION_SETTINGS);
		DataInputStream dataInputStream = new DataInputStream(in);
		this.startRetentionTime = dataInputStream.readInt();
		this.stopRetentionTime = dataInputStream.readInt();
		this.startAbundance = dataInputStream.readFloat();
		this.stopAbundance = dataInputStream.readFloat();
		//
		zipEntry = in.getNextEntry();
		assert zipEntry.getName().equals(CHROMATOGRAM_SELECTION_PROCESSING);
		objectInputStream = new ObjectInputStream(in);
		int size = objectInputStream.readInt();
		for(int i = 0; i < size; i++) {
			try {
				Object object = objectInputStream.readObject();
				processing.add(((IChromatogramSelectionProcessing<? super IChromatogramSelectionCSD>)object));
			} catch(ClassNotFoundException e) {
				throw new IOException(e);
			}
		}
	}

	@Override
	protected void save(PortObjectZipOutputStream out, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

		ZipEntry zipEntry = new ZipEntry(CHROMATOGRAM_SELECTION_HEADER);
		out.putNextEntry(zipEntry);
		if(this.chromatogramSelectionCSD == EMPTY_CHROMATOGRAM_SELECTION) {
			out.write(0);
			out.closeEntry();
			return;
		} else {
			out.write(1);
		}
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
		objectOutputStream.writeObject(inputFile);
		//
		zipEntry = new ZipEntry(CHROMATOGRAM_SELECTION_DATA);
		out.putNextEntry(zipEntry);
		DataOutputStream dataOutputStream = new DataOutputStream(out);
		if(chromatogramUpdate || chromatogramByteArray == null) {
			chromatogramByteArray = convertChromatogram(chromatogramSelectionCSD.getChromatogramCSD());
			chromatogramUpdate = false;
		}
		dataOutputStream.write(chromatogramByteArray);
		dataOutputStream.flush();
		//
		zipEntry = new ZipEntry(CHROMATOGRAM_SELECTION_SETTINGS);
		out.putNextEntry(zipEntry);
		dataOutputStream = new DataOutputStream(out);
		if(chromatogramSelectionCSD != null) {
			dataOutputStream.writeInt(chromatogramSelectionCSD.getStartRetentionTime());
			dataOutputStream.writeInt(chromatogramSelectionCSD.getStopRetentionTime());
			dataOutputStream.writeFloat(chromatogramSelectionCSD.getStartAbundance());
			dataOutputStream.writeFloat(chromatogramSelectionCSD.getStopAbundance());
		} else {
			dataOutputStream.writeInt(startRetentionTime);
			dataOutputStream.writeInt(stopRetentionTime);
			dataOutputStream.writeFloat(startAbundance);
			dataOutputStream.writeFloat(stopAbundance);
		}
		dataOutputStream.flush();
		//
		zipEntry = new ZipEntry(CHROMATOGRAM_SELECTION_PROCESSING);
		out.putNextEntry(zipEntry);
		objectOutputStream = new ObjectOutputStream(out);
		objectOutputStream.writeInt(processing.size());
		for(int i = 0; i < processing.size(); i++) {
			objectOutputStream.writeObject(processing.get(i));
		}
		objectOutputStream.flush();
		out.closeEntry();
	}
}
