/*******************************************************************************
 * Copyright (c) 2017 hornm.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * hornm - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.model;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.ModelContentWO;
import org.knime.core.node.port.AbstractSimplePortObjectSpec;

public class ChromatogramFilterPortObjectSpec extends AbstractSimplePortObjectSpec {

	public static final class Serializer extends AbstractSimplePortObjectSpecSerializer<ChromatogramFilterPortObjectSpec> {
	}

	@Override
	protected void load(ModelContentRO model) throws InvalidSettingsException {

		// nothing to do so far
	}

	@Override
	protected void save(ModelContentWO model) {

		// nothing to do so far
	}
}
