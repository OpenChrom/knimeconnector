package net.openchrom.knime.node.base;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.eclipse.chemclipse.model.core.IMeasurement;
import org.eclipse.chemclipse.model.filter.IMeasurementFilter;
import org.eclipse.chemclipse.nmr.model.core.FIDMeasurement;
import org.eclipse.chemclipse.processing.core.MessageConsumer;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.port.PortObject;

import net.openchrom.knime.node.base.progress.KnimeProgressMonitor;

public class ProcessorAdapter {

	public static PortObject[] adaptFID(IMeasurementFilter<?> filter, PortObject[] inObjects, ExecutionContext exec) {
		GenericPortObject fidObject = (GenericPortObject) inObjects[0];
		final GenericPortObject portOneOut = new GenericPortObject(adapt(filter, fidObject.getMeasurements(),
				e -> KNIMEFIDMeasurement.build((Collection<? extends FIDMeasurement>) e), exec));
		return new PortObject[] { portOneOut };
	}

	public static PortObject[] adaptNMR(IMeasurementFilter<?> filter, PortObject[] inObjects, ExecutionContext exec) {
		GenericPortObject fidObject = (GenericPortObject) inObjects[0];
		final GenericPortObject portOneOut = new GenericPortObject(adapt(filter, fidObject.getMeasurements(),
				e -> KNIMENMRMeasurement.build((Collection<? extends KNIMENMRMeasurement>) e), exec));
		return new PortObject[] { portOneOut };
	}

	public static <M extends KNIMEMeasurement> PortObject[] adapt(IMeasurementFilter<?> filter,
			Function<? super Collection<? extends IMeasurement>, List<M>> measurementFactory, PortObject[] inObjects,
			ExecutionContext exec) {
		GenericPortObject fidObject = (GenericPortObject) inObjects[0];
		final GenericPortObject portOneOut = new GenericPortObject(
				adapt(filter, fidObject.getMeasurements(), measurementFactory, exec));
		return new PortObject[] { portOneOut };
	}

	public static <M extends KNIMEMeasurement> List<M> adapt(IMeasurementFilter<?> filter,
			Collection<? extends IMeasurement> measurements,
			Function<? super Collection<? extends IMeasurement>, List<M>> measurementFactory, ExecutionContext exec) {

		List<M> result = filter.filterIMeasurements(measurements, null, measurementFactory, new MessageConsumer() {

			@Override
			public void addMessage(String description, String message, Throwable t, MessageType type) {
				if (t == null) {
					exec.getProgressMonitor().setMessage(description + ": " + message);
				} else {
					t.printStackTrace();
					exec.getProgressMonitor()
							.setMessage(description + ": " + message + " (" + t.getLocalizedMessage() + ")");
				}
			}
		}, new KnimeProgressMonitor(exec));

		return result;

	}

	public ProcessorAdapter() {

	}

}
