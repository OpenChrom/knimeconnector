/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.nmr.process.supplier.knime.ui.processor;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.nmr.model.selection.IDataNMRSelection;
import org.eclipse.chemclipse.nmr.processor.core.ScanProcessorNMR;
import org.eclipse.chemclipse.nmr.processor.settings.IProcessorSettings;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import net.openchrom.nmr.process.supplier.knime.portobject.ScanNMRPortObject;
import net.openchrom.nmr.process.supplier.knime.portobject.ScanNMRPortObjectSpec;
import net.openchrom.process.supplier.knime.ui.dialogfactory.SettingsObjectWrapper;
import net.openchrom.process.supplier.knime.ui.dialoggeneration.DialogGenerationNodeModel;
import net.openchrom.xxd.process.supplier.knime.portobject.ChromatogramSelectionCSDPortObject;

/**
 * Concatenates chromatogram filters (see {@link ChromatogramFilterPortObject}) and optionally executes them on a given chromatogram selection (see {@link ChromatogramSelectionCSDPortObject}).
 *
 * @author Martin Horn, University of Konstanz
 *
 */
public class ScanProcessorNodeModel extends DialogGenerationNodeModel<IProcessorSettings> {

	private static final NodeLogger logger = NodeLogger.getLogger(ScanProcessorNodeModel.class);
	private String id;

	ScanProcessorNodeModel(String id, SettingsObjectWrapper<IProcessorSettings> so) {

		super(new PortType[]{ScanNMRPortObject.TYPE}, new PortType[]{ScanNMRPortObject.TYPE}, so);
		this.id = id;
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return new PortObjectSpec[]{new ScanNMRPortObjectSpec()};
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		ScanNMRPortObject scanNMRPortObject = (ScanNMRPortObject)inObjects[0];
		IDataNMRSelection scanNMR = scanNMRPortObject.getScanNMR();
		/*
		 * Apply the filter if a chromatogram selection is given at port 0.
		 */
		ScanProcessorNMR.process(scanNMR, getSettingsObject(), id, new NullProgressMonitor());
		/*
		 * Store applied chromatogram filter and it's settings
		 */
		return new PortObject[]{scanNMRPortObject};
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {

	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}
}
