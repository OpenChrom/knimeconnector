package net.openchrom.knime.node.base;

import java.io.IOException;

import javax.swing.JComponent;

import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectSpecZipInputStream;
import org.knime.core.node.port.PortObjectSpecZipOutputStream;

public class GenericPortObjectSpec implements PortObjectSpec {

	public static final class Serializer extends PortObjectSpecSerializer<GenericPortObjectSpec> {

		@Override
		public void savePortObjectSpec(final GenericPortObjectSpec portObjectSpec,
				final PortObjectSpecZipOutputStream out) throws IOException {
			// TODO Auto-generated method stub

		}

		@Override
		public GenericPortObjectSpec loadPortObjectSpec(final PortObjectSpecZipInputStream in) throws IOException {
			return new GenericPortObjectSpec();
		}
	}

	@Override
	public JComponent[] getViews() {
		// TODO Auto-generated method stub
		return null;
	}

}
