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
package net.openchrom.xxd.process.supplier.knime.ui.identifier.model;

import org.eclipse.chemclipse.model.identifier.IIdentifierSettings;

import net.openchrom.process.supplier.knime.dialogfactory.SettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyCollector;
import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;
import net.openchrom.process.supplier.knime.ui.dialogfactory.property.PropertyDialogFactory;
import net.openchrom.xxd.process.supplier.knime.model.settingssupplier.IdentifierSettingsObjectSupplier;

public class IdentifierPropertyDialogFactory<SO extends IIdentifierSettings> extends PropertyDialogFactory<SO> {

	//
	private SettingObjectSupplier<SO> settingObjectSupplier = new IdentifierSettingsObjectSupplier<>();

	public IdentifierPropertyDialogFactory() {

		settingObjectSupplier = new IdentifierSettingsObjectSupplier<SO>();
	}

	@Override
	public SO createSettingsObject(Class<? extends SO> settingsObjectClass, PropertyProvider prov) {

		return settingObjectSupplier.createSettingsObject(settingsObjectClass, prov);
	}

	@Override
	public void extractProperties(Class<? extends SO> settingsObjectClass, PropertyCollector coll) {

		settingObjectSupplier.extractProperties(settingsObjectClass, coll);
	}
}
