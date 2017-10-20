/*******************************************************************************
 * Copyright (c) 2017 Martin Horn.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Martin Horn - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.apply;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionMSDPortObject;
import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionMSDPortObjectSpec;
import net.openchrom.xxd.process.supplier.knime.model.IChromatogramSelectionProcessing;

/**
 * Node model for the "Apply Filters"-node.
 *
 * @author Martin Horn, University of Konstanz
 *
 */
public class ChromatogramApplyNodeModel extends NodeModel {

	protected ChromatogramApplyNodeModel() {
		super(new PortType[]{ChromatogramSelectionMSDPortObject.TYPE}, new PortType[]{ChromatogramSelectionMSDPortObject.TYPE});
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return new PortObjectSpec[]{new ChromatogramSelectionMSDPortObjectSpec()};
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		ChromatogramSelectionMSDPortObject chromatogramSelectionPortObject = (ChromatogramSelectionMSDPortObject)inObjects[0];
		ChromatogramSelectionMSDPortObjectSpec chromatogramSelectionMSDPortObjectSpec = chromatogramSelectionPortObject.getSpec();
		if(chromatogramSelectionMSDPortObjectSpec.getProcessingMode().equals(ChromatogramSelectionMSDPortObjectSpec.MODE_POSTPONED_PROCESSING)) {
			IChromatogramSelectionProcessing.updateChromatogramSelection(chromatogramSelectionPortObject, new NullProgressMonitor(), true);
			chromatogramSelectionMSDPortObjectSpec.setProcessingMode(ChromatogramSelectionMSDPortObjectSpec.MODE_IMMEDIATE_PROCESSING);
		}
		return new PortObject[]{chromatogramSelectionPortObject};
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {

	}

	@Override
	protected void reset() {

	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {

	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

	}
}
