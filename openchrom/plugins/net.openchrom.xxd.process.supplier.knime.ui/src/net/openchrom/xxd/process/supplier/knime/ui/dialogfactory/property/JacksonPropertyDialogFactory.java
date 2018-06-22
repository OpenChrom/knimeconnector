/*******************************************************************************
 * Copyright (c) 2017 Martin Horn.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Martin Horn - initial API and implementation
 * Jan Holy - implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.property;

import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.SettingsDialogFactory;

/**
 * A {@link PropertyDialogFactory} implementation that extracts the properties from jackson-annotated classes.
 * <code>@JsonProperty</code> and <code>@JsonPropertyDescription</code> annotations are considered.
 *
 * @author Martin Horn, University of Konstanz
 *
 * @param <SO>
 *            see {@link SettingsDialogFactory}
 */
public class JacksonPropertyDialogFactory<SO> extends PropertyDialogFactory<SO> {

	private JacksonSettingObjectSupplier<SO> jacksonSettingObjectSupplier;

	public JacksonPropertyDialogFactory() {
		jacksonSettingObjectSupplier = new JacksonSettingObjectSupplier<>();
	}

	@Override
	public SO createSettingsObject(Class<? extends SO> settingsObjectClass, PropertyProvider prov) {

		return jacksonSettingObjectSupplier.createSettingsObject(settingsObjectClass, prov);
	}

	@Override
	public void extractProperties(Class<? extends SO> settingsObjectClass, PropertyCollector coll) {

		jacksonSettingObjectSupplier.extractProperties(settingsObjectClass, coll);
	}
}
