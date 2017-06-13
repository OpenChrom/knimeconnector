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
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.knime.node;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.core.ChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.settings.ISupplierFilterSettings;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.settings.SupplierFilterSettings;
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
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionMSDPortObject;
import net.openchrom.xxd.process.supplier.knime.model.PortObjectSupport;

public class ChromatogramFilterNodeModel extends NodeModel {

	private static final NodeLogger logger = NodeLogger.getLogger(ChromatogramFilterNodeModel.class);

	static SettingsModelIntegerBounded createModelDerivative() {

		return new SettingsModelIntegerBounded("Derivate", PreferenceSupplier.DEF_DERIVATIVE, PreferenceSupplier.MIN_DERIVATIVE, PreferenceSupplier.MAX_DERIVATIVE);
	}

	static SettingsModelIntegerBounded createModelOrder() {

		return new SettingsModelIntegerBounded("Order", PreferenceSupplier.DEF_ORDER, PreferenceSupplier.MIN_ORDER, PreferenceSupplier.MAX_ORDER);
	}

	static SettingsModelIntegerBounded createModelWidth() {

		return new SettingsModelIntegerBounded("Width", PreferenceSupplier.DEF_WIDTH, PreferenceSupplier.MIN_WIDTH, PreferenceSupplier.MAX_WIDTH);
	}

	private final SettingsModelIntegerBounded smDerivative = createModelDerivative();
	private final SettingsModelIntegerBounded smOrder = createModelOrder();
	private final SettingsModelIntegerBounded smWidth = createModelWidth();

	protected ChromatogramFilterNodeModel() {
		super(new PortType[]{PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionMSDPortObject.class)}, new PortType[]{PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionMSDPortObject.class)});
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		ChromatogramSelectionMSDPortObject chromatogramSelectionMSDPortObject = PortObjectSupport.getChromatogramSelectionMSDPortObject(inObjects);
		if(chromatogramSelectionMSDPortObject != null) {
			/*
			 * Apply the filter.
			 */
			logger.info("Apply the filter");
			IChromatogramSelectionMSD chromatogramSelectionMSD = chromatogramSelectionMSDPortObject.getChromatogramSelectionMSD();
			//
			ISupplierFilterSettings supplierFilterSettings = new SupplierFilterSettings();
			supplierFilterSettings.setDerivative(smDerivative.getIntValue());
			supplierFilterSettings.setOrder(smOrder.getIntValue());
			supplierFilterSettings.setWidth(smWidth.getIntValue());
			//
			ChromatogramFilter chromatogramFilter = new ChromatogramFilter();
			IProcessingInfo processingInfo = chromatogramFilter.applyFilter(chromatogramSelectionMSD, supplierFilterSettings, new NullProgressMonitor());
			ProcessingInfoViewSupport.updateProcessingInfo(processingInfo, false);
			//
			return new PortObject[]{chromatogramSelectionMSDPortObject};
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

		smDerivative.saveSettingsTo(settings);
		smOrder.saveSettingsTo(settings);
		smWidth.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

		smDerivative.loadSettingsFrom(settings);
		smOrder.loadSettingsFrom(settings);
		smWidth.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		smDerivative.validateSettings(settings);
		smOrder.validateSettings(settings);
		smWidth.validateSettings(settings);
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
