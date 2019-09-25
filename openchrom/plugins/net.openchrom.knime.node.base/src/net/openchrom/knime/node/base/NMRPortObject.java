package net.openchrom.knime.node.base;

import java.util.Collection;
import java.util.List;

import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

public class NMRPortObject extends GenericPortObject<KNIMENMRMeasurement> {

	public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(NMRPortObject.class);

	public static final class Serializer extends AbstractPortObjectSerializer<NMRPortObject> {
	}

	public NMRPortObject() {
		super();

	}

	public NMRPortObject(Collection<? extends KNIMENMRMeasurement> measurements, GenericPortObjectSpec portObjectSpec) {
		super(measurements, portObjectSpec);

	}

	public NMRPortObject(Collection<? extends KNIMENMRMeasurement> measurements) {
		super(measurements);

	}

	@Override
	public List<KNIMENMRMeasurement> getMeasurements() {
		return super.getMeasurements();
	}

}
