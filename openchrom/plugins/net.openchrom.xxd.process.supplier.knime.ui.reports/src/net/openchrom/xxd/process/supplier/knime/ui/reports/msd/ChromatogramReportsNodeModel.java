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
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionMSDPortObject;
import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionMSDPortObjectSpec;
import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.SettingsObjectWrapper;
import net.openchrom.xxd.process.supplier.knime.ui.dialoggeneration.DialogGenerationNodeModel;

public class ChromatogramReportsNodeModel extends DialogGenerationNodeModel<IChromatogramReportSettings> {

	private static final NodeLogger logger = NodeLogger.getLogger(ChromatogramReportsNodeModel.class);

	protected ChromatogramReportsNodeModel(String id, SettingsObjectWrapper<IChromatogramReportSettings> settingsObject) {
		super(new PortType[]{ChromatogramSelectionMSDPortObject.TYPE}, new PortType[]{}, settingsObject);
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
		chromatogramSelectionPortObject.getChromatogramSelectionMSD();
		// ChromatogramReports.getChromatogramReportSupplierSupport().getReportSupplier(null).getChromatogramReportSettingsClass()
		// ReportConverter.getReportConverterSupport().getSupplier(null).
		/*
		 * Store applied chromatogram filter and it's settings
		 */
		return new PortObject[]{};
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
