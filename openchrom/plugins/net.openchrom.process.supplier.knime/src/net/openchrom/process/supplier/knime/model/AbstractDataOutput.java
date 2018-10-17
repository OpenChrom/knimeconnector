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

import org.eclipse.chemclipse.model.core.IMeasurementInfo;

public abstract class AbstractDataOutput<Data extends IMeasurementInfo> implements IDataOutput<Data> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6869432432403783917L;
	private String id;
	private String prefix;
	private String postfix;
	private File directory;
	private String fileName;

	protected AbstractDataOutput() {

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

	private String getFileName(Data data) {

		StringBuilder sb = new StringBuilder();
		sb.append(getPrefix());
		sb.append(data.getDataName());
		sb.append(getPostfix());
		return sb.toString();
	}
}
