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
package net.openchrom.xxd.process.supplier.knime.ui.process.end.csd;

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
import org.knime.core.node.workflow.LoopEndNode;

import net.openchrom.xxd.process.supplier.knime.portobject.ChromatogramProcessingPortObject;
import net.openchrom.xxd.process.supplier.knime.portobject.ChromatogramSelectionCSDPortObject;
import net.openchrom.xxd.process.supplier.knime.portobject.ChromatogramSelectionCSDPortObjectSpec;

/**
 * Node model for the "Apply Filters"-node.
 *
 * @author Martin Horn, University of Konstanz
 *
 */
public class ProcessControllerEndNodeModel extends NodeModel implements LoopEndNode {

	protected ProcessControllerEndNodeModel() {

		super(new PortType[]{ChromatogramSelectionCSDPortObject.TYPE}, new PortType[]{ChromatogramSelectionCSDPortObject.TYPE});
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return new PortObjectSpec[]{new ChromatogramSelectionCSDPortObjectSpec()};
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		ChromatogramSelectionCSDPortObject chromatogramSelectionPortObject = (ChromatogramSelectionCSDPortObject)inObjects[0];
		ChromatogramSelectionCSDPortObjectSpec chromatogramSelectionCSDPortObjectSpec = chromatogramSelectionPortObject.getSpec();
		if(chromatogramSelectionCSDPortObjectSpec.getProcessingMode().equals(ChromatogramSelectionCSDPortObjectSpec.MODE_POSTPONED_PROCESSING)) {
			ChromatogramProcessingPortObject.updateChromatogramSelection(chromatogramSelectionPortObject, new NullProgressMonitor(), true);
			chromatogramSelectionCSDPortObjectSpec.setProcessingMode(ChromatogramSelectionCSDPortObjectSpec.MODE_IMMEDIATE_PROCESSING);
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
