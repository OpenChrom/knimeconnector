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
package net.openchrom.xxd.process.supplier.knime.ui.peakintegrators.msd;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.IPeakIntegrationSettings;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
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

import net.openchrom.process.supplier.knime.ui.dialogfactory.SettingsObjectWrapper;
import net.openchrom.process.supplier.knime.ui.dialoggeneration.DialogGenerationNodeModel;
import net.openchrom.xxd.process.supplier.knime.portobject.ChromatogramSelectionMSDPortObject;
import net.openchrom.xxd.process.supplier.knime.portobject.ChromatogramSelectionMSDPortObjectSpec;
import net.openchrom.xxd.process.supplier.knime.ui.peakintegrators.support.PeakIntegratorsSupport;

public class PeakIntegratorsNodeModel extends DialogGenerationNodeModel<IPeakIntegrationSettings> {

	private static final NodeLogger logger = NodeLogger.getLogger(PeakIntegratorsNodeModel.class);
	private String id;

	protected PeakIntegratorsNodeModel(String id, SettingsObjectWrapper<IPeakIntegrationSettings> settingsObject) {

		super(new PortType[]{ChromatogramSelectionMSDPortObject.TYPE}, new PortType[]{ChromatogramSelectionMSDPortObject.TYPE}, settingsObject);
		this.id = id;
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return new PortObjectSpec[]{new ChromatogramSelectionMSDPortObjectSpec()};
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		ChromatogramSelectionMSDPortObject chromatogramSelectionPortObject = (ChromatogramSelectionMSDPortObject)inObjects[0];
		ChromatogramSelectionMSDPortObjectSpec chromatogramSelectionMSDPortObjectSpec = chromatogramSelectionPortObject.getSpec();
		/*
		 * Apply the filter if a chromatogram selection is given at port 0.
		 */
		if(chromatogramSelectionMSDPortObjectSpec.getProcessingMode().equals(ChromatogramSelectionMSDPortObjectSpec.MODE_IMMEDIATE_PROCESSING)) {
			logger.info("Apply the filter");
			IChromatogramSelectionMSD chromatogramSelection = chromatogramSelectionPortObject.getChromatogramSelectionMSD();
			IProcessingInfo processingInfo = PeakIntegratorsSupport.integrateMSD(chromatogramSelection, getSettingsObject(), id, new NullProgressMonitor());
			chromatogramSelectionPortObject.chromatogramSelectionUpdate();
			ProcessingInfoViewSupport.updateProcessingInfo(processingInfo, false);
		} else if(chromatogramSelectionMSDPortObjectSpec.getProcessingMode().equals(ChromatogramSelectionMSDPortObjectSpec.MODE_POSTPONED_PROCESSING)) {
			logger.info("Add the filter");
			chromatogramSelectionPortObject.addProcessings(PeakIntegratorsSupport.getProcessingPeakIntegratoMSD(id, getPropertyProvider()));
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
