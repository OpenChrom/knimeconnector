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
package net.openchrom.process.supplier.knime.ui.node.listfiles;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.chemclipse.support.util.FileSettingUtil;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;

import net.openchrom.process.supplier.knime.filesportobject.FilePortObject;

public class ListFilesNodeModel extends NodeModel {

	private static final String INPUT_FILES = "INPUT_FILES";
	private FileSettingUtil fileSettingUtil = new FileSettingUtil();
	private SettingsModelString settingFilesInput;

	public ListFilesNodeModel() {

		super(new PortType[0], new PortType[]{FilePortObject.TYPE});
		settingFilesInput = getSettingsChromatogamFileInput();
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected PortObject[] execute(PortObject[] inData, ExecutionContext exec) throws Exception {

		List<File> files = fileSettingUtil.deserialize(settingFilesInput.getStringValue());
		FilePortObject filePortObject = new FilePortObject();
		filePortObject.addFiles(files);
		return new PortObject[]{filePortObject};
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {

		settingFilesInput.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

		settingFilesInput.validateSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {

		settingFilesInput.loadSettingsFrom(settings);
	}

	@Override
	protected void reset() {

	}

	static SettingsModelString getSettingsChromatogamFileInput() {

		return new SettingsModelString(INPUT_FILES, "");
	}
}
