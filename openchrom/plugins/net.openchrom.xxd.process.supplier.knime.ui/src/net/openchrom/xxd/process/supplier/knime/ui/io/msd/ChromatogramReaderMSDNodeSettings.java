/*******************************************************************************
 * Copyright (c) 2017 Lablicat GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lorenz Gerber - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.io.msd;

import java.io.File;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public class ChromatogramReaderMSDNodeSettings {

	private File[] files;
	private String filePath;
	// private boolean retainData;

	/**
	 * Returns the file.
	 * 
	 * @return the file
	 */
	public File[] files() {

		return files;
	}

	/**
	 * Sets the file.
	 * 
	 * @param name
	 *            the file
	 */
	public void files(final File[] files) {

		this.files = files;
	}

	/**
	 * Returns the file path.
	 * 
	 * @return the file path
	 */
	public String filePath() {

		return filePath;
	}

	/**
	 * Sets the file path.
	 * 
	 * @param path
	 *            the file path
	 */
	public void filePath(final String path) {

		this.filePath = path;
	}
	/**
	 * Returns if data should be retained after reset.
	 * 
	 * @return boolean
	 */
	// public boolean retainData() {
	// return retainData;
	// }

	/**
	 * If data should be retained after reset.
	 * 
	 * @param retainData
	 *            boolean
	 */
	// public void retainData(boolean retainData) {
	// this.retainData = retainData;
	// }
	/**
	 * Saves all settings into the given node settings object.
	 * 
	 * @param settings
	 *            the node settings
	 */
	public void saveSettings(final NodeSettingsWO settings) {

		settings.addStringArray("files", getFileStrings());
		settings.addString("path", filePath);
		// settings.addBoolean("retain", retainData);
	}

	/**
	 * Loads all settings from the given node settings object.
	 * 
	 * @param settings
	 *            the node settings
	 * @throws InvalidSettingsException
	 *             if a setting is missing
	 */
	public void loadSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		getFiles(settings.getStringArray("files"));
		filePath = settings.getString("path");
		// retainData = settings.getBoolean("retain");
	}

	private String[] getFileStrings() {

		int i = 0;
		String[] names = new String[files.length];
		for(File file : files) {
			names[i] = file.getAbsolutePath();
			i++;
		}
		return names;
	}

	private void getFiles(String[] names) {

		files = new File[names.length];
		int i = 0;
		for(String name : names) {
			files[i] = new File(name);
			i++;
		}
	}
}
