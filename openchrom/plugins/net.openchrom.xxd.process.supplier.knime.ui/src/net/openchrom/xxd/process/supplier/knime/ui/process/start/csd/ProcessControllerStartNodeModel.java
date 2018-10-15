/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.process.start.csd;

import java.io.File;
import java.io.IOException;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.workflow.LoopStartNode;

import net.openchrom.xxd.process.supplier.knime.portobject.ChromatogramSelectionCSDPortObject;
import net.openchrom.xxd.process.supplier.knime.portobject.ChromatogramSelectionCSDPortObjectSpec;

public class ProcessControllerStartNodeModel extends NodeModel implements LoopStartNode {

	private SettingsModelString processType;

	protected ProcessControllerStartNodeModel() {

		super(new PortType[]{ChromatogramSelectionCSDPortObject.TYPE}, new PortType[]{ChromatogramSelectionCSDPortObject.TYPE});
		processType = getProcessingType();
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		ChromatogramSelectionCSDPortObjectSpec chromatogramSelectionCSDPortObjectSpec = new ChromatogramSelectionCSDPortObjectSpec();
		chromatogramSelectionCSDPortObjectSpec.setProcessingMode(processType.getStringValue());
		return new PortObjectSpec[]{chromatogramSelectionCSDPortObjectSpec};
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		ChromatogramSelectionCSDPortObject chromatogramSelectionPortObject = (ChromatogramSelectionCSDPortObject)inObjects[0];
		ChromatogramSelectionCSDPortObjectSpec chromatogramSelectionCSDPortObjectSpec = chromatogramSelectionPortObject.getSpec();
		chromatogramSelectionCSDPortObjectSpec.setProcessingMode(processType.getStringValue());
		return new PortObject[]{chromatogramSelectionPortObject};
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {

		processType.loadSettingsFrom(settings);
	}

	@Override
	protected void reset() {

	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {

		processType.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

		processType.validateSettings(settings);
	}

	static SettingsModelString getProcessingType() {

		return new SettingsModelString(ChromatogramSelectionCSDPortObjectSpec.PROCESSING_MODE, ChromatogramSelectionCSDPortObjectSpec.MODE_POSTPONED_PROCESSING);
	}
}
