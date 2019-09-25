package net.openchrom.knime.node.base;

import java.util.Collection;
import java.util.List;

import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

public class FIDPortObject extends GenericPortObject<KNIMEFIDMeasurement> {

	public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(FIDPortObject.class);

	public static final class Serializer extends AbstractPortObjectSerializer<FIDPortObject> {
	}

	public FIDPortObject() {
		super();

	}

	public FIDPortObject(Collection<? extends KNIMEFIDMeasurement> measurements, GenericPortObjectSpec portObjectSpec) {
		super(measurements, portObjectSpec);

	}

	public FIDPortObject(Collection<? extends KNIMEFIDMeasurement> measurements) {
		super(measurements);

	}

	@Override
	public List<KNIMEFIDMeasurement> getMeasurements() {
		return super.getMeasurements();
	}

}
