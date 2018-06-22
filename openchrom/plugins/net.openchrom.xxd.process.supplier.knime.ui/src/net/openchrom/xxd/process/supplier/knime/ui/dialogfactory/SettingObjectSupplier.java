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
package net.openchrom.xxd.process.supplier.knime.ui.dialogfactory;

import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.property.PropertyCollector;
import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.property.PropertyProvider;

public interface SettingObjectSupplier<SO> {

	/**
	 * Creates the actual settings object from the given settings object class and the {@link PropertyProvider}.
	 *
	 * @param settingsObjectClass
	 * @param prov
	 * @return the settings object instance
	 */
	SO createSettingsObject(Class<? extends SO> settingsObjectClass, PropertyProvider prov);

	/**
	 * Extract the properties, e.g. via reflection represented by getter and setter methods, from the settings object class. The extracted properties are added to the provided {@link PropertyCollector}.
	 *
	 * @param settingsObjectClass
	 * @param coll
	 */
	void extractProperties(Class<? extends SO> settingsObjectClass, PropertyCollector coll);
}
