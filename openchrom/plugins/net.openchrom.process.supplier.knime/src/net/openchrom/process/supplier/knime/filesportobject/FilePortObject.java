/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.process.supplier.knime.filesportobject;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;

import javax.swing.JComponent;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.port.AbstractPortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

public class FilePortObject extends AbstractPortObject {

	private static final String SCAN_NMR_DATA = "SCAN_NMR_DATA";
	public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(FilePortObject.class);
	public static final PortType TYPE_OPTIONAL = PortTypeRegistry.getInstance().getPortType(FilePortObject.class, true);
	private List<File> files;

	public static final class Serializer extends AbstractPortObjectSerializer<FilePortObject> {
	}

	private FilePortObjectSpec portObjectSpec;

	public FilePortObject() {

		files = new ArrayList<>();
	}

	@Override
	public String getSummary() {

		return "Files";
	}

	@Override
	public FilePortObjectSpec getSpec() {

		return portObjectSpec;
	}

	@Override
	public JComponent[] getViews() {

		return null;
	}

	@Override
	protected void save(PortObjectZipOutputStream out, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

		ZipEntry zipEntry = new ZipEntry(SCAN_NMR_DATA);
		out.putNextEntry(zipEntry);
		ObjectOutputStream outputStream = new ObjectOutputStream(out);
		outputStream.writeInt(files.size());
		for(File file : files) {
			outputStream.writeObject(file);
		}
		outputStream.close();
	}

	@Override
	protected void load(PortObjectZipInputStream in, PortObjectSpec spec, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

		ZipEntry zipEntry = in.getNextEntry();
		assert zipEntry.getName().equals(SCAN_NMR_DATA);
		ObjectInputStream inputStream = new ObjectInputStream(in);
		try {
			int size = inputStream.readInt();
			for(int i = 0; i < size; i++) {
				files.add((File)inputStream.readObject());
			}
		} catch(ClassNotFoundException e) {
			throw new IOException(e);
		}
	}

	public List<File> getFiles() {

		return Collections.unmodifiableList(files);
	}

	public void addFiles(File... files) {

		for(File file : files) {
			this.files.add(file);
		}
	}

	public void addFiles(Collection<File> files) {

		for(File file : files) {
			this.files.add(file);
		}
	}

	void clearFile() {

		files.clear();
	}
}
