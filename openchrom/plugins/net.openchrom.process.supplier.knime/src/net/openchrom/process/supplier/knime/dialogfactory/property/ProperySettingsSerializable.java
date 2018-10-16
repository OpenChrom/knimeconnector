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
package net.openchrom.process.supplier.knime.dialogfactory.property;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ProperySettingsSerializable implements Serializable, PropertyProvider {

	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = 3978680040826208015L;
	private Map<String, Object> properties;

	public ProperySettingsSerializable() {

	}

	public ProperySettingsSerializable(PropertyProvider prov) {

		properties = new HashMap<>(prov.values());
	}

	@Override
	public boolean getBooleanProperty(String id) {

		return (boolean)properties.get(id);
	}

	@Override
	public double getDoubleProperty(String id) {

		return (double)properties.get(id);
	}

	@Override
	public float getFloatProperty(String id) {

		return (float)properties.get(id);
	}

	@Override
	public int getIntProperty(String id) {

		return (int)properties.get(id);
	}

	@Override
	public String getStringProperty(String id) {

		return (String)properties.get(id);
	}

	@Override
	public Map<String, Object> values() {

		return Collections.unmodifiableMap(properties);
	}
}
