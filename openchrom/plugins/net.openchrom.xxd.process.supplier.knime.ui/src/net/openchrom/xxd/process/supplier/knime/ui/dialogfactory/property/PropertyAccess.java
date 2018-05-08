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
package net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import net.openchrom.xxd.process.supplier.knime.ui.dialog.DialogComponentIonSelection;
import net.openchrom.xxd.process.supplier.knime.ui.dialog.DialogComponentMultiFileChooser;
import net.openchrom.xxd.process.supplier.knime.ui.dialog.DialogComponentStringIdSelection;

/**
 * Helper class for the {@link PropertyDialogFactory} that unifies and implements the {@link PropertyCollector} and {@link PropertyProvider}.
 * It creates {@link DialogComponent}s and {@link SettingsModel}s for added properties and returns the {@link SettingsModel}s values.
 *
 * @author Martin Horn, University of Konstanz
 *
 */
public class PropertyAccess implements PropertyCollector, PropertyProvider {

	Map<String, String> descriptions = new HashMap<>();
	/*
	 * Components for the dialog.
	 */
	List<DialogComponent> dialogComponents = new ArrayList<>();
	/*
	 * Settings models for the node model (never the same as passed with the respective dialog component!).
	 */
	Map<String, SettingsModel> settingsModels = new HashMap<>();

	@Override
	public void addBooleanProperty(String id, String name, boolean defaultValue, String description) {

		addPropertyDescriptions(name, description);
		dialogComponents.add(new DialogComponentBoolean(new SettingsModelBoolean(id, defaultValue), name));
		settingsModels.put(id, new SettingsModelBoolean(id, defaultValue));
	}

	@Override
	public void addDoubleProperty(String id, String name, double defaultValue, String description, int step, double min, double max) {

		addPropertyDescriptions(name, description);
		dialogComponents.add(new DialogComponentNumber(new SettingsModelDoubleBounded(id, defaultValue, min, max), name, step));
		settingsModels.put(id, new SettingsModelDouble(id, defaultValue));
	}

	@Override
	public void addFloatProperty(String id, String name, float defaultValue, String description, int step, float min, float max) {

		addPropertyDescriptions(name, description);
		dialogComponents.add(new DialogComponentNumber(new SettingsModelDoubleBounded(id, defaultValue, min, max), name, step));
		settingsModels.put(id, new SettingsModelDouble(id, defaultValue));
	}

	@Override
	public void addIntProperty(String id, String name, int defaultValue, String description, int step, int min, int max) {

		addPropertyDescriptions(name, description);
		dialogComponents.add(new DialogComponentNumber(new SettingsModelIntegerBounded(id, defaultValue, min, max), name, step));
		settingsModels.put(id, new SettingsModelInteger(id, defaultValue));
	}

	private void addPropertyDescriptions(String name, String description) {

		descriptions.put(name, description);
	}

	@Override
	public void addStringProperty(String id, String name, String defaultValue, String description) {

		addPropertyDescriptions(name, description);
		dialogComponents.add(new DialogComponentString(new SettingsModelString(id, defaultValue), name));
		settingsModels.put(id, new SettingsModelString(id, defaultValue));
	}

	@Override
	public void addStringProperty(String id, String name, String defaultValue, String description, Collection<String> list, Map<String, String> ids) {

		addPropertyDescriptions(name, description);
		dialogComponents.add(new DialogComponentStringIdSelection(new SettingsModelString(id, defaultValue), name, list, ids, false));
		settingsModels.put(id, new SettingsModelString(id, defaultValue));
	}

	@Override
	public void addFileProperty(String id, String name, String defaultValue, String description, String idHistory, String[] extension) {

		addPropertyDescriptions(name, description);
		dialogComponents.add(new DialogComponentFileChooser(new SettingsModelString(id, defaultValue), idHistory, extension));
		settingsModels.put(id, new SettingsModelString(id, defaultValue));
	}

	@Override
	public boolean getBooleanProperty(String id) {

		return ((SettingsModelBoolean)settingsModels.get(id)).getBooleanValue();
	}

	@Override
	public double getDoubleProperty(String id) {

		return ((SettingsModelDouble)settingsModels.get(id)).getDoubleValue();
	}

	@Override
	public float getFloatProperty(String id) {

		return (float)((SettingsModelDouble)settingsModels.get(id)).getDoubleValue();
	}

	@Override
	public int getIntProperty(String id) {

		return ((SettingsModelInteger)settingsModels.get(id)).getIntValue();
	}

	@Override
	public String getStringProperty(String id) {

		return ((SettingsModelString)settingsModels.get(id)).getStringValue();
	}

	@Override
	public void addIonSelectionProperty(String id, String name, String defaultValue, String description) {

		addPropertyDescriptions(name, description);
		dialogComponents.add(new DialogComponentIonSelection(new SettingsModelString(id, defaultValue), name));
		settingsModels.put(id, new SettingsModelString(id, defaultValue));
	}

	@Override
	public void addMultiFileProperty(String id, String name, String defaultValue, String description, String idHistory, String[] extensions) {

		addPropertyDescriptions(name, description);
		dialogComponents.add(new DialogComponentMultiFileChooser(new SettingsModelString(id, defaultValue), idHistory, extensions));
		settingsModels.put(id, new SettingsModelString(id, defaultValue));
	}
}
