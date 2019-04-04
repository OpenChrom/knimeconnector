package net.openchrom.knime.base.portobject;

import java.io.IOException;
import java.util.Objects;

import javax.swing.JComponent;

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

	public FIDMeasurementPortObject(final FIDMeasurementPortObjectSpec portObjectSpec) {
		this.portObjectSpec = Objects.requireNonNull(portObjectSpec);
	}

	public FIDMeasurementPortObject() {
		this(new FIDMeasurementPortObjectSpec());
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
		// TODO Auto-generated method stub

	}

	@Override
	protected void load(final PortObjectZipInputStream in, final PortObjectSpec spec, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO Auto-generated method stub

	}

}
