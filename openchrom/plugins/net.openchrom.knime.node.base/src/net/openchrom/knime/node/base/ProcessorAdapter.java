package net.openchrom.knime.node.base;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.eclipse.chemclipse.model.core.IMeasurement;
import org.eclipse.chemclipse.model.filter.IMeasurementFilter;
import org.eclipse.chemclipse.processing.core.MessageConsumer;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.port.PortObject;

import net.openchrom.knime.node.base.progress.KnimeProgressMonitor;

public class ProcessorAdapter {

	public static <M extends IMeasurement> PortObject[] adapt(IMeasurementFilter<?> filter, PortObject[] inObjects,
			ExecutionContext exec) {
		GenericPortObject fidObject = (GenericPortObject) inObjects[0];
		final GenericPortObject portOneOut = new GenericPortObject(adapt(filter, fidObject.getMeasurements(), exec));
		return new PortObject[] { portOneOut };
	}

	@SuppressWarnings("unchecked")
	public static <M extends IMeasurement> List<M> adapt(IMeasurementFilter<?> filter,
			Collection<? extends IMeasurement> measurements, ExecutionContext exec) {

		List<M> result = (List<M>) filter.filterIMeasurements(measurements, null, Function.identity(),
				new MessageConsumer() {

					@Override
					public void addMessage(String description, String message, Throwable t, MessageType type) {
						t.printStackTrace();
						exec.getProgressMonitor()
								.setMessage(description + ": " + message + " (" + t.getLocalizedMessage() + ")");

					}
				}, new KnimeProgressMonitor(exec));

		return result;

	}

	public ProcessorAdapter() {

	}

}
