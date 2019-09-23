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
package net.openchrom.knime.node.nmr.digitalfilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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
import net.openchrom.knime.node.fid.base.portobject.FIDMeasurementPortObjectSpec;
import net.openchrom.knime.node.fid.base.portobject.KNIMEFIDMeasurement;

public class DigitalFilterNodeModel extends NodeModel {

	private static final NodeLogger logger = NodeLogger.getLogger(DigitalFilterNodeModel.class);

	protected DigitalFilterNodeModel() {
		super(new PortType[] { FIDMeasurementPortObject.TYPE }, new PortType[] { FIDMeasurementPortObject.TYPE });
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		logger.info(this.getClass().getSimpleName() + ": Input specs: " + Arrays.asList(inSpecs));
		final FIDMeasurementPortObjectSpec portOne = new FIDMeasurementPortObjectSpec();
		return new PortObjectSpec[] { portOne };
	}

	@SuppressWarnings("unchecked")
	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
		logger.info(this.getClass().getSimpleName() + ": InObjects: " + Arrays.asList(inObjects));
		FIDMeasurementPortObject fidObject = (FIDMeasurementPortObject) inObjects[0];
		Collection<KNIMEFIDMeasurement> measurements = fidObject.getMeasurements();
		Collection<KNIMEFIDMeasurement> measurementsFiltered = new ArrayList<>();
		// DigitalFilterRemoval filter= new DigitalFilterRemoval();
		// exec.getProgressMonitor().setProgress(0);
		// long cnt = 0;
		// for (IComplexSignalMeasurement<?> measurement : measurements) {
		// exec.checkCanceled();
		// if (measurement instanceof FIDMeasurement)
		// measurementsFiltered.addAll(
		// // TODO: this should not be necessary, improve generics
		// (Collection<IComplexSignalMeasurement<?>>) FilterUtils.applyFilter(filter,
		// measurement));
		// exec.getProgressMonitor().setProgress(cnt++ / measurements.size());
		// }
		final FIDMeasurementPortObject portOneOut = new FIDMeasurementPortObject(measurementsFiltered);
		return new PortObject[] { portOneOut };
	}

	@Override
	protected void loadInternals(final File nodeInternDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		logger.info(this.getClass().getSimpleName() + ": Load internals");

	}

	@Override
	protected void saveInternals(final File nodeInternDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		logger.info(this.getClass().getSimpleName() + ": Save internals");

	}

	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		logger.info(this.getClass().getSimpleName() + ": Saving settings");

	}

	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		logger.info(this.getClass().getSimpleName() + ": Validate settings");

	}

	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		logger.info(this.getClass().getSimpleName() + ": Loading validated settings");

	}

	@Override
	protected void reset() {
		logger.info(this.getClass().getSimpleName() + ": OnReset");

	}

}
