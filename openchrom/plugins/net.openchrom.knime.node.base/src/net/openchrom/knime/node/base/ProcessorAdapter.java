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
package net.openchrom.knime.node.base;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.eclipse.chemclipse.model.core.IMeasurement;
import org.eclipse.chemclipse.model.filter.IMeasurementFilter;
import org.eclipse.chemclipse.nmr.model.core.FIDMeasurement;
import org.eclipse.chemclipse.nmr.model.core.SpectrumMeasurement;
import org.eclipse.chemclipse.processing.core.MessageConsumer;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.port.PortObject;

import net.openchrom.knime.node.base.progress.KnimeProgressMonitor;

/**
 * {@code ProcessorAdapter} provides static helper methods to adapt the KNIME
 * processing API to the OpenChrom processing API and vice versa.
 * 
 * @author Alexander Kerner
 *
 */
public class ProcessorAdapter {

	public static PortObject[] transformToNMRPortObject(Collection<? extends SpectrumMeasurement> e) {

		return new PortObject[] { new NMRPortObject(e) };
	}

	public static PortObject[] transformToFIDPortObject(Collection<? extends FIDMeasurement> e) {

		return new PortObject[] { new FIDPortObject(e) };
	}

	public static MessageConsumer buildMessageConsumer(NodeLogger logger) {

		return new MessageConsumer() {

			@Override
			public void addMessage(String description, String message, Throwable t, MessageType type) {

				if (t == null) {
					logger.error(description + ": " + message);
				} else {
					t.printStackTrace();
					logger.error(description + ": " + message + " (" + t.getLocalizedMessage() + ")");
				}
			}
		};
	}

    public static <I extends IMeasurement, T extends GenericPortObject<I>> List<I> getInput(
			PortObject[] inObjects) {

		return getInput(inObjects, 0);
	}

	@SuppressWarnings("unchecked")
	public static <I extends IMeasurement, T extends GenericPortObject<I>> List<I> getInput(PortObject[] inObjects,
			int index) {
		return ((T) inObjects[index]).getMeasurements();
	}

	public ProcessorAdapter() {

	}

    @SuppressWarnings("unchecked")
    public static <R extends IMeasurement, I extends IMeasurement> Collection<R> filter(IMeasurementFilter<?> filter,
	    Collection<I> inData, NodeLogger logger, ExecutionContext exec) {
	return (Collection<R>) filter.filterIMeasurements(inData, null, Function.identity(),
		ProcessorAdapter.buildMessageConsumer(logger), new KnimeProgressMonitor(exec));
    }

	public static PortObject[] adaptFIDInFIDOut(IMeasurementFilter<?> filter,PortObject[] inObjects, ExecutionContext exec, NodeLogger logger) {
		List<FIDMeasurement> inData = ProcessorAdapter.getInput(inObjects);
		if (inData.isEmpty()) {
			logger.warn("Empty input data!");
		}
		Collection<FIDMeasurement> outData = ProcessorAdapter.filter(filter, inData, logger, exec);
		if (outData.isEmpty()) {
			logger.warn("No data processed!");
		}
		return ProcessorAdapter.transformToFIDPortObject(outData);
	}

	public static PortObject[] adaptFIDInNMROut(IMeasurementFilter<?> filter, PortObject[] inObjects,
			ExecutionContext exec, NodeLogger logger) {
		List<FIDMeasurement> inData = ProcessorAdapter.getInput(inObjects);
		if (inData.isEmpty()) {
			logger.warn("Empty input data!");
		}
		Collection<SpectrumMeasurement> outData = ProcessorAdapter.filter(filter, inData, logger, exec);
		if (outData.isEmpty()) {
			logger.warn("No data processed!");
		}
		return ProcessorAdapter.transformToNMRPortObject(outData);
	}

	public static PortObject[] adaptNMRInNMROut(IMeasurementFilter<?> filter, PortObject[] inObjects,
			ExecutionContext exec, NodeLogger logger) {
		List<SpectrumMeasurement> inData = ProcessorAdapter.getInput(inObjects);
		if (inData.isEmpty()) {
			logger.warn("Empty input data!");
		}
		Collection<SpectrumMeasurement> outData = ProcessorAdapter.filter(filter, inData, logger, exec);
		if (outData.isEmpty()) {
			logger.warn("No data processed!");
		}
		return ProcessorAdapter.transformToNMRPortObject(outData);
	}
}
