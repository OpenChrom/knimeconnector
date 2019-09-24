package net.openchrom.knime.node.base;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.eclipse.chemclipse.model.core.IMeasurement;
import org.eclipse.chemclipse.processing.core.MessageConsumer;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.knime.core.node.ExecutionContext;

import net.openchrom.knime.node.base.progress.KnimeProgressMonitor;
import net.openchrom.nmr.processing.supplier.base.core.AbstractFIDSignalFilter;

public class ProcessorAdapter {

	@SuppressWarnings("unchecked")
	public static <R extends IMeasurement, ConfigType>

			List<R> adapt(AbstractFIDSignalFilter<ConfigType> filter, Collection<? extends IMeasurement> measurements,
					ExecutionContext exec) {

		List<R> result = (List<R>) filter.filterIMeasurements(measurements, null, Function.identity(),
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
