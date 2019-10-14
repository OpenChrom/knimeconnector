/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *Alexander Kerner - initial API and implementation
 *******************************************************************************/
package net.openchrom.knime.node.base;

import java.io.IOException;

import javax.swing.JComponent;

import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectSpecZipInputStream;
import org.knime.core.node.port.PortObjectSpecZipOutputStream;

/**
 * No-opt implementation of {@link PortObjectSpec}.
 * 
 * @author Alexander Kerner
 *
 */
public class GenericPortObjectSpec implements PortObjectSpec {

	public static final class Serializer extends PortObjectSpecSerializer<GenericPortObjectSpec> {

		@Override
		public void savePortObjectSpec(final GenericPortObjectSpec portObjectSpec,
				final PortObjectSpecZipOutputStream out) throws IOException {

		}

		@Override
		public GenericPortObjectSpec loadPortObjectSpec(final PortObjectSpecZipInputStream in) throws IOException {
			return new GenericPortObjectSpec();
		}
	}

	@Override
	public JComponent[] getViews() {
		return null;
	}

}
