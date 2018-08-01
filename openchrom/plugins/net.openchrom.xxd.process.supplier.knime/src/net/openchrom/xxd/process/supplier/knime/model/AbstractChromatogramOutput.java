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
package net.openchrom.xxd.process.supplier.knime.model;

import java.io.File;
import java.io.Serializable;
import java.util.function.BiFunction;

import org.eclipse.chemclipse.model.selection.IChromatogramSelection;

public abstract class AbstractChromatogramOutput<ChromatogramSelection extends IChromatogramSelection> implements IChromatogramOutput<ChromatogramSelection> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4171358163435953704L;
	private String id;
	private String prefix;
	private String postfix;
	private File directory;
	private String fileName;
	private BiFunction<IChromatogramOutput, IChromatogramSelection, String> functionCreateFileName;

	protected AbstractChromatogramOutput() {
	}

	public AbstractChromatogramOutput(String id, File directory) {
		if(!directory.isDirectory()) {
			throw new IllegalArgumentException("File has to be direcotry");
		}
		this.id = id;
		this.directory = directory;
		this.prefix = "";
		this.postfix = "";
		this.functionCreateFileName = IChromatogramOutput.USE_CHROMATOGAM_NAME;
	}

	public AbstractChromatogramOutput(String id, File directory, String fileName) {
		this(id, directory);
		this.fileName = fileName;
		this.functionCreateFileName = IChromatogramOutput.USE_NAME;
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

	@Override
	public void setFunctionCreateFileName(BiFunction<IChromatogramOutput, IChromatogramSelection, String> functionCreateFileName) {

		if(functionCreateFileName instanceof Serializable) {
			this.functionCreateFileName = functionCreateFileName;
		} else {
			throw new IllegalArgumentException("Parameter has to implement Serializable interface");
		}
	}

	@Override
	public BiFunction<IChromatogramOutput, IChromatogramSelection, String> getFunctionCreateFileName() {

		return functionCreateFileName;
	}

	protected String generateFilePath(ChromatogramSelection chromatogramSelection) {

		return getDirectory().getAbsolutePath() + File.separator + getFunctionCreateFileName().apply(this, chromatogramSelection);
	}
}
