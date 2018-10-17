/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.nmr.process.supplier.knime.portobject;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.ModelContentWO;
import org.knime.core.node.port.AbstractSimplePortObjectSpec;

public class ScanNMRPortObjectSpec extends AbstractSimplePortObjectSpec {

	public static final class Serializer extends AbstractSimplePortObjectSpecSerializer<ScanNMRPortObjectSpec> {
	}

	public ScanNMRPortObjectSpec() {

	}

	@Override
	protected void save(ModelContentWO model) {

	}

	@Override
	protected void load(ModelContentRO model) throws InvalidSettingsException {

	}
}
