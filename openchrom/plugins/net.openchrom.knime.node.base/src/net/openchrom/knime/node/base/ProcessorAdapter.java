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

	public static PortObject[] adaptFIDinFIDout(IMeasurementFilter<?> filter, PortObject[] inObjects,
			ExecutionContext exec, NodeLogger logger) {

		return adapt(filter, getInput(inObjects), e -> transformToFIDPortObject(e), exec, logger);
	}

	public static PortObject[] adaptFIDinNMRout(IMeasurementFilter<?> filter, PortObject[] inObjects,
			ExecutionContext exec, NodeLogger logger) {

		return adapt(filter, getInput(inObjects), e -> transformToNMRPortObject(e), exec, logger);
	}

	public static PortObject[] adaptNMRinNMRout(IMeasurementFilter<?> filter, PortObject[] inObjects,
			ExecutionContext exec, NodeLogger logger) {

		return adapt(filter, getInput(inObjects), e -> transformToNMRPortObject(e), exec, logger);
	}

	public static PortObject[] transformToNMRPortObject(Collection<? extends IMeasurement> e) {

		@SuppressWarnings("unchecked")
		List<KNIMENMRMeasurement> measurements = KNIMENMRMeasurement
				.build((Collection<? extends SpectrumMeasurement>) e);
		return new PortObject[] { new NMRPortObject(measurements) };
	}

	public static PortObject[] transformToFIDPortObject(Collection<? extends IMeasurement> e) {

		@SuppressWarnings("unchecked")
		List<KNIMEFIDMeasurement> measurements = KNIMEFIDMeasurement.build((Collection<? extends FIDMeasurement>) e);
		return new PortObject[] { new FIDPortObject(measurements) };
	}

	public static PortObject transformToFIDPortObject2(Collection<? extends IMeasurement> e) {

		@SuppressWarnings("unchecked")
		List<KNIMEFIDMeasurement> measurements = KNIMEFIDMeasurement.build((Collection<? extends FIDMeasurement>) e);
		return new FIDPortObject(measurements);
	}

	public static PortObject[] adapt(IMeasurementFilter<?> filter, Collection<? extends IMeasurement> measurements,
			Function<? super Collection<? extends IMeasurement>, PortObject[]> measurementFactory,
			ExecutionContext exec, NodeLogger logger) {

		try {
			PortObject[] result = filter.filterIMeasurements(measurements, null, measurementFactory,
					buildMessageConsumer(logger), new KnimeProgressMonitor(exec));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static <ConfigType> PortObject adapt(IMeasurementFilter<ConfigType> filter, ConfigType settings,
			Collection<? extends IMeasurement> measurements,
			Function<? super Collection<? extends IMeasurement>, PortObject> measurementFactory, ExecutionContext exec,
			NodeLogger logger) {

		try {
			PortObject result = filter.filterIMeasurements(measurements, settings, measurementFactory,
					buildMessageConsumer(logger), new KnimeProgressMonitor(exec));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
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

	public static <I extends KNIMEMeasurement, T extends GenericPortObject<I>> List<I> getInput(
			PortObject[] inObjects) {

		return getInput(inObjects, 0);
	}

	@SuppressWarnings("unchecked")
	public static <I extends KNIMEMeasurement, T extends GenericPortObject<I>> List<I> getInput(PortObject[] inObjects,
			int index) {
		return ((T) inObjects[index]).getMeasurements();
	}

	public ProcessorAdapter() {

	}
}
