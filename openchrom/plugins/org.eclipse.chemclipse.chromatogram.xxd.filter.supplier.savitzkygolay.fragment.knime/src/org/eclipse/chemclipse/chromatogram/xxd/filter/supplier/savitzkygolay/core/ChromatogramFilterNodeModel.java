/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 * 
 * This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.core;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.settings.ISupplierFilterSettings;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.settings.SupplierFilterSettings;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
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
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionPortObject;

public class ChromatogramFilterNodeModel extends NodeModel {

	private static final NodeLogger logger = NodeLogger.getLogger(ChromatogramFilterNodeModel.class);
	//
	private static final String DERIVATIVE = "Derivative";
	protected static final SettingsModelInteger SETTING_DERIVATIVE = new SettingsModelInteger(DERIVATIVE, PreferenceSupplier.DEF_DERIVATIVE);
	//
	private static final String ORDER = "Order";
	protected static final SettingsModelInteger SETTING_ORDER = new SettingsModelInteger(ORDER, PreferenceSupplier.DEF_ORDER);
	//
	private static final String WIDTH = "Width";
	protected static final SettingsModelInteger SETTING_WIDTH = new SettingsModelInteger(WIDTH, PreferenceSupplier.DEF_WIDTH);

	protected ChromatogramFilterNodeModel() {
		super(new PortType[]{PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionPortObject.class)}, new PortType[]{PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionPortObject.class)});
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		ChromatogramSelectionPortObject chromatogramSelectionPortObject = getChromatogramSelectionPortObject(inObjects);
		if(chromatogramSelectionPortObject != null) {
			/*
			 * Apply the filter.
			 */
			logger.info("Apply the filter");
			IChromatogramSelection chromatogramSelection = chromatogramSelectionPortObject.getChromatogramSelection();
			//
			ISupplierFilterSettings supplierFilterSettings = new SupplierFilterSettings();
			supplierFilterSettings.setDerivative(SETTING_DERIVATIVE.getIntValue());
			supplierFilterSettings.setOrder(SETTING_ORDER.getIntValue());
			supplierFilterSettings.setWidth(SETTING_WIDTH.getIntValue());
			//
			ChromatogramFilter chromatogramFilter = new ChromatogramFilter();
			IProcessingInfo processingInfo = chromatogramFilter.applyFilter(chromatogramSelection, supplierFilterSettings, new NullProgressMonitor());
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

	private ChromatogramSelectionPortObject getChromatogramSelectionPortObject(PortObject[] inObjects) {

		for(Object object : inObjects) {
			if(object instanceof ChromatogramSelectionPortObject) {
				return (ChromatogramSelectionPortObject)object;
			}
		}
		return null;
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

		SETTING_DERIVATIVE.saveSettingsTo(settings);
		SETTING_ORDER.saveSettingsTo(settings);
		SETTING_WIDTH.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

		SETTING_DERIVATIVE.loadSettingsFrom(settings);
		SETTING_ORDER.loadSettingsFrom(settings);
		SETTING_WIDTH.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		SETTING_DERIVATIVE.validateSettings(settings);
		SETTING_ORDER.validateSettings(settings);
		SETTING_WIDTH.validateSettings(settings);
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
