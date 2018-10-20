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
package net.openchrom.process.supplier.knime.model;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.eclipse.chemclipse.model.core.IMeasurementInfo;

public abstract class AbstractDataOutput<Data extends IMeasurementInfo> implements IDataOutput<Data> {

	private String id;
	private String prefix;
	private String postfix;
	private File directory;
	private String fileName;
	//
	private static final int INTERNAL_VERSION_ID = 1;

	public AbstractDataOutput() {

	}

	public AbstractDataOutput(String id, File directory) {

		if(!directory.isDirectory()) {
			throw new IllegalArgumentException("File has to be direcotry");
		}
		this.id = id;
		this.directory = directory;
		this.prefix = "";
		this.postfix = "";
	}

	public AbstractDataOutput(String id, File directory, String fileName) {

		this(id, directory);
		this.fileName = fileName;
	}

	@Override
	public String getId() {

		return id;
	}

	@Override
	public void setPrefix(String prefix) {

		this.prefix = prefix;
	}

	@Override
	public String getPrefix() {

		return prefix;
	}

	@Override
	public void setPostfix(String postfix) {

		this.postfix = postfix;
	}

	@Override
	public String getPostfix() {

		return postfix;
	}

	@Override
	public void setDirectory(File directory) {

		if(!directory.isDirectory()) {
			throw new IllegalArgumentException("File has to be direcotry");
		}
		this.directory = directory;
	}

	@Override
	public void setDirectory(String directory) {

		File file = new File(directory);
		if(!file.isDirectory()) {
			throw new IllegalArgumentException("File has to be direcotry");
		}
		this.directory = file;
	}

	@Override
	public File getDirectory() {

		return directory;
	}

	@Override
	public void setFileName(String fileName) {

		this.fileName = fileName;
	}

	@Override
	public String getFileName() {

		return fileName;
	}

	protected String generateFilePath(Data data) {

		return getDirectory().getAbsolutePath() + File.separator + getFileName(data);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

		int version = in.readInt();
		switch(version) {
			case 1:
				id = (String)in.readObject();
				prefix = (String)in.readObject();
				postfix = (String)in.readObject();
				directory = (File)in.readObject();
				fileName = (String)in.readObject();
				break;
			default:
				break;
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {

		out.writeInt(INTERNAL_VERSION_ID);
		out.writeObject(id);
		out.writeObject(prefix);
		out.writeObject(postfix);
		out.writeObject(directory);
		out.writeObject(fileName);
	}

	private String getFileName(Data data) {

		StringBuilder sb = new StringBuilder();
		sb.append(getPrefix());
		sb.append(data.getDataName());
		sb.append(getPostfix());
		return sb.toString();
	}
}
