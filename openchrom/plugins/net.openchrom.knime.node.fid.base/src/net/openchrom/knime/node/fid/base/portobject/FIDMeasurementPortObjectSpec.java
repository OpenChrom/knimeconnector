package net.openchrom.knime.node.fid.base.portobject;

import java.io.IOException;

import javax.swing.JComponent;

import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectSpecZipInputStream;
import org.knime.core.node.port.PortObjectSpecZipOutputStream;

public class FIDMeasurementPortObjectSpec implements PortObjectSpec {

	public static final class Serializer extends PortObjectSpecSerializer<FIDMeasurementPortObjectSpec> {

		@Override
		public void savePortObjectSpec(final FIDMeasurementPortObjectSpec portObjectSpec,
				final PortObjectSpecZipOutputStream out) throws IOException {
			// TODO Auto-generated method stub

		}

		@Override
		public FIDMeasurementPortObjectSpec loadPortObjectSpec(final PortObjectSpecZipInputStream in)
				throws IOException {
			return new FIDMeasurementPortObjectSpec();
		}
	}

	@Override
	public JComponent[] getViews() {
		// TODO Auto-generated method stub
		return null;
	}

}
