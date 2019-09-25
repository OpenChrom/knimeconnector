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
import org.knime.core.node.port.PortObject;

import net.openchrom.knime.node.base.progress.KnimeProgressMonitor;

public class ProcessorAdapter {

	public static PortObject[] adaptFIDinFIDout(IMeasurementFilter<?> filter, PortObject[] inObjects,
			ExecutionContext exec) {
		FIDPortObject fidObject = (FIDPortObject) inObjects[0];
		List<KNIMEFIDMeasurement> measurements = adapt(filter, fidObject.getMeasurements(),
				e -> KNIMEFIDMeasurement.build((Collection<? extends FIDMeasurement>) e), exec);
		final FIDPortObject portOneOut = new FIDPortObject(measurements);
		return new PortObject[] { portOneOut };
	}

	public static PortObject[] adaptFIDinNMRout(IMeasurementFilter<?> filter, PortObject[] inObjects,
			ExecutionContext exec) {
		FIDPortObject fidObject = (FIDPortObject) inObjects[0];
		List<KNIMENMRMeasurement> measurements = adapt(filter, fidObject.getMeasurements(),
				e -> KNIMENMRMeasurement.build((Collection<? extends SpectrumMeasurement>) e), exec);
		final NMRPortObject portOneOut = new NMRPortObject(measurements);
		return new PortObject[] { portOneOut };
	}

	public static PortObject[] adaptNMRinNMRout(IMeasurementFilter<?> filter, PortObject[] inObjects,
			ExecutionContext exec) {
		NMRPortObject fidObject = (NMRPortObject) inObjects[0];
		List<KNIMENMRMeasurement> measurements = adapt(filter, fidObject.getMeasurements(),
				e -> KNIMENMRMeasurement.build((Collection<? extends SpectrumMeasurement>) e), exec);
		final NMRPortObject portOneOut = new NMRPortObject(measurements);
		return new PortObject[] { portOneOut };
	}

	public static <In extends KNIMEMeasurement, Out extends KNIMEMeasurement> List<Out> adapt(
			IMeasurementFilter<?> filter, Collection<In> measurements,
			Function<? super Collection<? extends IMeasurement>, List<Out>> measurementFactory, ExecutionContext exec) {

		List<Out> result = filter.filterIMeasurements(measurements, null, measurementFactory, new MessageConsumer() {

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
