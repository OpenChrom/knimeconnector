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
package net.openchrom.xxd.process.supplier.knime.ui.peakintegrators.csd;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.IPeakIntegrationSettings;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.ui.support.ProcessingInfoViewSupport;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionCSDPortObject;
import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionCSDPortObjectSpec;
import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.SettingsObjectWrapper;
import net.openchrom.xxd.process.supplier.knime.ui.dialoggeneration.DialogGenerationNodeModel;
import net.openchrom.xxd.process.supplier.knime.ui.peakintegrators.support.PeakIntegratorsSupport;

public class PeakIntegratorsNodeModel extends DialogGenerationNodeModel<IPeakIntegrationSettings> {

	private static final NodeLogger logger = NodeLogger.getLogger(PeakIntegratorsNodeModel.class);
	private String id;

	protected PeakIntegratorsNodeModel(String id, SettingsObjectWrapper<IPeakIntegrationSettings> settingsObject) {
		super(new PortType[]{ChromatogramSelectionCSDPortObject.TYPE}, new PortType[]{ChromatogramSelectionCSDPortObject.TYPE}, settingsObject);
		this.id = id;
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return new PortObjectSpec[]{new ChromatogramSelectionCSDPortObjectSpec()};
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		ChromatogramSelectionCSDPortObject chromatogramSelectionPortObject = (ChromatogramSelectionCSDPortObject)inObjects[0];
		ChromatogramSelectionCSDPortObjectSpec chromatogramSelectionCSDPortObjectSpec = chromatogramSelectionPortObject.getSpec();
		/*
		 * Apply the filter if a chromatogram selection is given at port 0.
		 */
		if(chromatogramSelectionCSDPortObjectSpec.getProcessingMode().equals(ChromatogramSelectionCSDPortObjectSpec.MODE_IMMEDIATE_PROCESSING)) {
			logger.info("Apply the filter");
			IChromatogramSelectionCSD chromatogramSelection = chromatogramSelectionPortObject.getChromatogramSelectionCSD();
			IProcessingInfo processingInfo = PeakIntegratorsSupport.integrateCSD(chromatogramSelection, getSettingsObject(), id, new NullProgressMonitor());
			chromatogramSelectionPortObject.chromatogramSelectionUpdate();
			ProcessingInfoViewSupport.updateProcessingInfo(processingInfo, false);
		} else if(chromatogramSelectionCSDPortObjectSpec.getProcessingMode().equals(ChromatogramSelectionCSDPortObjectSpec.MODE_POSTPONED_PROCESSING)) {
			logger.info("Add the filter");
			IPeakIntegrationSettings settings = getSettingsObject();
			chromatogramSelectionPortObject.addProcessings(PeakIntegratorsSupport.getProcessingPeakIntegratoCSD(id, settings));
		}
		/*
		 * Store applied chromatogram filter and it's settings
		 */
		return new PortObject[]{chromatogramSelectionPortObject};
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
