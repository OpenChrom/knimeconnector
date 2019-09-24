package net.openchrom.knime.node.base;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;

import javax.swing.JComponent;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.port.AbstractPortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

public class GenericPortObject extends AbstractPortObject {

	public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(GenericPortObject.class);

	public static final class Serializer extends AbstractPortObjectSerializer<GenericPortObject> {
	}

	private final static String summary = "OpenChrom Measurement";

	private final GenericPortObjectSpec portObjectSpec;

	private final List<KNIMEMeasurement> measurements;

	public GenericPortObject(final Collection<? extends KNIMEMeasurement> measurements,
			final GenericPortObjectSpec portObjectSpec) {
		this.measurements = new ArrayList<>(measurements);
		this.portObjectSpec = Objects.requireNonNull(portObjectSpec);
	}

	public GenericPortObject(final Collection<? extends KNIMEMeasurement> measurements) {
		this(measurements, new GenericPortObjectSpec());
	}

	public GenericPortObject() {
		this(new ArrayList<>(0), new GenericPortObjectSpec());
	}

	public List<KNIMEMeasurement> getMeasurements() {
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
			for (final KNIMEMeasurement m : measurements) {
				objectOutputStream.writeObject(m);
				objectOutputStream.writeObject(m.getHeaderDataMap());
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
		final GenericPortObjectSpec fidSpec = (GenericPortObjectSpec) spec;
		@SuppressWarnings("unused")
		final ZipEntry zipEntry = in.getNextEntry();
		final int numMeasurements = in.read();
		final ObjectInputStream objectInputStream = new ObjectInputStream(in);
		for (int i = 0; i < numMeasurements; i++) {
			try {
				Object o = objectInputStream.readObject();
				final KNIMEMeasurement m = (KNIMEMeasurement) o;
				Map<String, String> h = (Map<String, String>) objectInputStream.readObject();
				m.setHeaderDataMap(h);
				measurements.add(m);
			} catch (final ClassNotFoundException e) {
				throw new IOException(e);
			}
		}
	}

}
