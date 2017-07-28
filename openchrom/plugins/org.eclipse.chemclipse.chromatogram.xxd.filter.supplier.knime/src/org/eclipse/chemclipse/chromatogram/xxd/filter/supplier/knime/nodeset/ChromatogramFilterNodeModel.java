/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.nodeset;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.chromatogram.filter.exceptions.NoChromatogramFilterSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.dialogfactory.SettingsDialogManager;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.dialogfactory.SettingsObject;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.ui.support.ProcessingInfoViewSupport;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionMSDPortObject;
import net.openchrom.xxd.process.supplier.knime.model.PortObjectSupport;

public class ChromatogramFilterNodeModel extends NodeModel {

	private static final NodeLogger logger = NodeLogger.getLogger(ChromatogramFilterNodeModel.class);
	private SettingsObject<IChromatogramFilterSettings> filterSettings;
	private String filterId;
	private Class<? extends IChromatogramFilterSettings> filterSettingsClass;

	ChromatogramFilterNodeModel(String filterId) {
		super(new PortType[]{PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionMSDPortObject.class)}, new PortType[]{PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionMSDPortObject.class)});
		this.filterId = filterId;
		try {
			this.filterSettingsClass = org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.ChromatogramFilter.getChromatogramFilterSupport().getFilterSupplier(filterId).getFilterSettingsClass();
			filterSettings = (SettingsObject<IChromatogramFilterSettings>)SettingsDialogManager.getSettingsDialogFactoryFor(filterSettingsClass).get().createSettings(filterSettingsClass);
		} catch(NoChromatogramFilterSupplierAvailableException e) {
			logger.warn(e);
		}
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		ChromatogramSelectionMSDPortObject chromatogramSelectionPortObject = PortObjectSupport.getChromatogramSelectionMSDPortObject(inObjects);
		if(chromatogramSelectionPortObject != null) {
			/*
			 * Apply the filter.
			 */
			logger.info("Apply the filter");
			IChromatogramSelectionMSD chromatogramSelection = chromatogramSelectionPortObject.getChromatogramSelectionMSD();
			//
			IProcessingInfo processingInfo = org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.ChromatogramFilter.applyFilter(chromatogramSelection, filterSettings.getObject(), filterId, new NullProgressMonitor());
			ProcessingInfoViewSupport.updateProcessingInfo(processingInfo, false);
			//
			return new PortObject[]{chromatogramSelectionPortObject};
		} else {
			/*
			 * If things have gone wrong.
			 */
			return new PortObject[]{};
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {

	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		filterSettings.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

		filterSettings.loadValidatedSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		filterSettings.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}
}
