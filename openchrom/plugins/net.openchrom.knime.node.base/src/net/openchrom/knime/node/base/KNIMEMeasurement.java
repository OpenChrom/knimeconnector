package net.openchrom.knime.node.base;

import java.util.Map;

import org.eclipse.chemclipse.model.core.IMeasurement;

public interface KNIMEMeasurement extends IMeasurement {

	void setHeaderDataMap(Map<String, String> h);

}
