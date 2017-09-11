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
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.filter.dialogfactory.property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * Helper class for the {@link PropertyDialogFactory} that unifies and implements the {@link PropertyCollector} and {@link PropertyProvider}.
 * It creates {@link DialogComponent}s and {@link SettingsModel}s for added properties and returns the {@link SettingsModel}s values.
 * 
 * @author Martin Horn, University of Konstanz
 *
 */
public class PropertyAccess implements PropertyCollector, PropertyProvider {

	/*
	 * Components for the dialog.
	 */
	List<DialogComponent> dialogComponents = new ArrayList<>();
	/*
	 * Settings models for the node model (never the same as passed with the respective dialog component!).
	 */
	Map<String, SettingsModel> settingsModels = new HashMap<>();
	Map<String, String> descriptions = new HashMap<>();

	@Override
	public void addIntProperty(String id, String name, int defaultValue) {

		dialogComponents.add(new DialogComponentNumber(new SettingsModelInteger(id, defaultValue), name, 1));
		settingsModels.put(id, new SettingsModelInteger(id, defaultValue));
	}

	@Override
	public int getIntProperty(String id) {

		return ((SettingsModelInteger)settingsModels.get(id)).getIntValue();
	}

	@Override
	public void addFloatProperty(String id, String name, float defaultValue) {

		dialogComponents.add(new DialogComponentNumber(new SettingsModelDouble(id, defaultValue), name, 1));
		settingsModels.put(id, new SettingsModelDouble(id, defaultValue));
	}

	@Override
	public float getFloatProperty(String id) {

		return (float)((SettingsModelDouble)settingsModels.get(id)).getDoubleValue();
	}

	@Override
	public void addDoubleProperty(String id, String name, double defaultValue) {

		dialogComponents.add(new DialogComponentNumber(new SettingsModelDouble(id, defaultValue), name, 1));
		settingsModels.put(id, new SettingsModelDouble(id, defaultValue));
	}

	@Override
	public double getDoubleProperty(String id) {

		return ((SettingsModelDouble)settingsModels.get(id)).getDoubleValue();
	}

	@Override
	public void addStringProperty(String id, String name, String defaultValue) {

		dialogComponents.add(new DialogComponentString(new SettingsModelString(id, defaultValue), name));
		settingsModels.put(id, new SettingsModelString(id, defaultValue));
	}

	@Override
	public String getStringProperty(String id) {

		return ((SettingsModelString)settingsModels.get(id)).getStringValue();
	}

	public void addPropertyDescriptions(String name, String description) {

		descriptions.put(name, description);
	}
}
