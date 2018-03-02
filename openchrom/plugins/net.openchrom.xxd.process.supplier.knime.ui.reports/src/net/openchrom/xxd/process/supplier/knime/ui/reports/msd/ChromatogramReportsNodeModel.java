/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.reports.msd;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.chromatogram.xxd.report.settings.IChromatogramReportSettings;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionMSDPortObject;
import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionMSDPortObjectSpec;
import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.SettingsObjectWrapper;
import net.openchrom.xxd.process.supplier.knime.ui.dialoggeneration.DialogGenerationNodeModel;
import net.openchrom.xxd.process.supplier.knime.ui.reports.support.ReportsSupport;

public class ChromatogramReportsNodeModel extends DialogGenerationNodeModel<IChromatogramReportSettings> {

	private static final NodeLogger logger = NodeLogger.getLogger(ChromatogramReportsNodeModel.class);
	private String id;
	private static final String REPORT_FILE_OUTPUT = "ReportFileOutput";
	protected static final SettingsModelString SETTING_REPORT_FILE_OUTPUT = new SettingsModelString(REPORT_FILE_OUTPUT, "");

	protected ChromatogramReportsNodeModel(String id, SettingsObjectWrapper<IChromatogramReportSettings> settingsObject) {
		super(new PortType[]{ChromatogramSelectionMSDPortObject.TYPE}, new PortType[]{}, settingsObject);
		this.id = id;
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return new PortObjectSpec[]{new ChromatogramSelectionMSDPortObjectSpec()};
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		ChromatogramSelectionMSDPortObject chromatogramSelectionPortObject = (ChromatogramSelectionMSDPortObject)inObjects[0];
		/*
		 * Apply the filter if a chromatogram selection is given at port 0.
		 */
		logger.info("Apply the filter");
		IChromatogramSelection chromatogramSelection = chromatogramSelectionPortObject.getChromatogramSelectionMSD();
		ReportsSupport.generate(new File(REPORT_FILE_OUTPUT), chromatogramSelection.getChromatogram(), getSettingsObject(), id, new NullProgressMonitor());
		return new PortObject[]{};
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {

		SETTING_REPORT_FILE_OUTPUT.loadSettingsFrom(settings);
		super.loadValidatedSettingsFrom(settings);
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {

		SETTING_REPORT_FILE_OUTPUT.saveSettingsTo(settings);
		super.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

		SETTING_REPORT_FILE_OUTPUT.validateSettings(settings);
		super.validateSettings(settings);
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void reset() {

	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}
}
