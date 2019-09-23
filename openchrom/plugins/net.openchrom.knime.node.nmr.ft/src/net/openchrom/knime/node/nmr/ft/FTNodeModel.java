/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *Alexander Kerner - initial API and implementation
 *******************************************************************************/
package net.openchrom.knime.node.nmr.ft;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.chemclipse.model.core.IComplexSignalMeasurement;
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

import net.openchrom.knime.node.fid.base.portobject.FIDMeasurementPortObject;
import net.openchrom.knime.node.nmr.ft.portobject.NMRMeasurementPortObject;
import net.openchrom.knime.node.nmr.ft.portobject.NMRMeasurementPortObjectSpec;

public class FTNodeModel extends NodeModel {

	private static final NodeLogger logger = NodeLogger.getLogger(FTNodeModel.class);

	public FTNodeModel() {
		super(new PortType[] { FIDMeasurementPortObject.TYPE }, new PortType[] { NMRMeasurementPortObject.TYPE });
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		final NMRMeasurementPortObjectSpec portOne = new NMRMeasurementPortObjectSpec();
		return new PortObjectSpec[] { portOne };
	}

	@SuppressWarnings("unchecked")
	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
		logger.info(this.getClass().getSimpleName() + ": InObjects: " + Arrays.asList(inObjects));
		FIDMeasurementPortObject fidObject = (FIDMeasurementPortObject) inObjects[0];
		Collection<IComplexSignalMeasurement<?>> measurements = fidObject.getMeasurements();
		Collection<IComplexSignalMeasurement<?>> measurementsFiltered = new ArrayList<>();
		// FourierTransformationProcessor filter = null;
		// exec.getProgressMonitor().setProgress(0);
		// long cnt = 0;
		// for (IComplexSignalMeasurement<?> measurement : measurements) {
		// exec.checkCanceled();
		// if (measurement instanceof FIDMeasurement)
		//
		// if (filter == null) {
		// filter = new FourierTransformationProcessor();
		// }
		//
		// measurementsFiltered.addAll(
		// // TODO: this should not be necessary, improve generics
		// (Collection<IComplexSignalMeasurement<?>>) FilterUtils.applyFilter(filter,
		// measurement));
		// exec.getProgressMonitor().setProgress(cnt++ / measurements.size());
		// }

		final NMRMeasurementPortObject portOneOut = new NMRMeasurementPortObject(measurementsFiltered);
		return new PortObject[] { portOneOut };
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void reset() {
		// TODO Auto-generated method stub

	}

}
