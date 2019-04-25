package net.openchrom.knime.node.fid.base.portobject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.zip.ZipEntry;

import javax.swing.JComponent;

import org.eclipse.chemclipse.model.core.IComplexSignalMeasurement;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.port.AbstractPortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

public class FIDMeasurementPortObject extends AbstractPortObject {

	public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(FIDMeasurementPortObject.class);

	public static final class Serializer extends AbstractPortObjectSerializer<FIDMeasurementPortObject> {
	}

	private final static String summary = "OpenChrom FID Measurement";

	private final FIDMeasurementPortObjectSpec portObjectSpec;

	private final Collection<IComplexSignalMeasurement<?>> measurements;

	public FIDMeasurementPortObject(final Collection<IComplexSignalMeasurement<?>> measurements,
			final FIDMeasurementPortObjectSpec portObjectSpec) {
		this.measurements = measurements;
		this.portObjectSpec = Objects.requireNonNull(portObjectSpec);
	}

	public FIDMeasurementPortObject(final Collection<IComplexSignalMeasurement<?>> measurements) {
		this(measurements, new FIDMeasurementPortObjectSpec());
	}

	public FIDMeasurementPortObject() {
		this(new ArrayList<>(0), new FIDMeasurementPortObjectSpec());
	}

	public Collection<IComplexSignalMeasurement<?>> getMeasurements() {
		return measurements;
	}

	@Override
	public String getSummary() {
		return summary;
	}

	@Override
	public PortObjectSpec getSpec() {
		return portObjectSpec;
	}

	@Override
	public JComponent[] getViews() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void save(final PortObjectZipOutputStream out, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		final ZipEntry zipEntry = new ZipEntry(this.getClass().getSimpleName());
		out.putNextEntry(zipEntry);
		if (measurements != null) {
			out.write(measurements.size());
			final ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
			for (final IComplexSignalMeasurement<?> m : measurements) {
				objectOutputStream.writeObject(m);
			}
			objectOutputStream.flush();
		} else {
			out.write(0);
		}

		out.closeEntry();

	}

	@Override
	protected void load(final PortObjectZipInputStream in, final PortObjectSpec spec, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// specs currently not needed
		@SuppressWarnings("unused")
		final FIDMeasurementPortObjectSpec fidSpec = (FIDMeasurementPortObjectSpec) spec;
		final ZipEntry zipEntry = in.getNextEntry();
		final int numMeasurements = in.read();
		final ObjectInputStream objectInputStream = new ObjectInputStream(in);
		for (int i = 0; i < numMeasurements; i++) {
			try {
				final IComplexSignalMeasurement<?> m = (IComplexSignalMeasurement<?>) objectInputStream.readObject();
				measurements.add(m);
			} catch (final ClassNotFoundException e) {
				throw new IOException(e);
			}
		}
	}

}
