/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Alexander Kerner - initial API and implementation
 *******************************************************************************/
package net.openchrom.knime.node.nmr.apodization;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

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

import net.openchrom.knime.node.base.FIDPortObject;
import net.openchrom.knime.node.base.GenericPortObjectSpec;
import net.openchrom.knime.node.base.KNIMEFIDMeasurement;
import net.openchrom.knime.node.base.ProcessorAdapter;
import net.openchrom.knime.node.base.progress.KnimeProgressMonitor;
import net.openchrom.nmr.processing.apodization.ExponentialApodizationFunctionProcessor;
import net.openchrom.nmr.processing.apodization.ExponentialApodizationSettings;

/**
 * {@link NodeModel} for the Apodization node. It applies an exponential
 * apodization on fid data.
 * 
 * @see ExponentialApodizationFunctionProcessor
 * 
 * @author Alexander Kerner
 *
 */
public class ApodizationNodeModel extends NodeModel {

    private static final NodeLogger logger = NodeLogger.getLogger(ApodizationNodeModel.class);

    public ApodizationNodeModel() {

	super(new PortType[] { FIDPortObject.TYPE }, new PortType[] { FIDPortObject.TYPE });
    }

    @Override
    protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

	final GenericPortObjectSpec portOne = new GenericPortObjectSpec();
	return new PortObjectSpec[] { portOne };
    }

    @Override
    protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

	// FID in, FID out.
	List<KNIMEFIDMeasurement> inData = ProcessorAdapter.getInput(inObjects);
	if (inData.isEmpty()) {
	    logger.warn(this.getClass().getSimpleName() + ": Empty input data!");
	}
	List<KNIMEFIDMeasurement> outData = new ArrayList<>();
	for (int i = 0; i < inData.size(); i++) {
	    String headerData = inData.get(i).getHeaderData("procs_" + "LB");
	    ExponentialApodizationSettings settings = ExponentialApodizationSettings.build(headerData);
	    @SuppressWarnings("unchecked")
	    Collection<KNIMEFIDMeasurement> outElement = (Collection<KNIMEFIDMeasurement>) new ExponentialApodizationFunctionProcessor()
		    .filterIMeasurements(inData, settings, Function.identity(),
			    ProcessorAdapter.buildMessageConsumer(logger), new KnimeProgressMonitor(exec));
	    if (outElement != null)
		outData.addAll(outElement);
	}
	if (outData.isEmpty()) {
	    logger.warn(this.getClass().getSimpleName() + ": No data processed!");
	}
	return ProcessorAdapter.transformToFIDPortObject(outData);

    }

    @Override
    protected void loadInternals(final File nodeInternDir, final ExecutionMonitor exec)
	    throws IOException, CanceledExecutionException {

	logger.debug(this.getClass().getSimpleName() + ": Load internals");
    }

    @Override
    protected void saveInternals(final File nodeInternDir, final ExecutionMonitor exec)
	    throws IOException, CanceledExecutionException {

	logger.debug(this.getClass().getSimpleName() + ": Save internals");
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {

	logger.debug(this.getClass().getSimpleName() + ": Saving settings");
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

	logger.debug(this.getClass().getSimpleName() + ": Validate settings");
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

	logger.debug(this.getClass().getSimpleName() + ": Loading validated settings");
    }

    @Override
    protected void reset() {

	logger.debug(this.getClass().getSimpleName() + ": OnReset");
    }
}
