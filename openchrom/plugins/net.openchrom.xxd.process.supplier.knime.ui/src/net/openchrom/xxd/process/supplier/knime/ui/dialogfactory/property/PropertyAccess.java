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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.eclipse.chemclipse.support.settings.FileSettingProperty.DialogType;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelOddIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.util.ButtonGroupEnumInterface;

import net.openchrom.xxd.process.supplier.knime.ui.dialog.DialogComponentIonSelection;
import net.openchrom.xxd.process.supplier.knime.ui.dialog.DialogComponentMultiFileChooser;
import net.openchrom.xxd.process.supplier.knime.ui.dialog.DialogComponentRetentionTimeMinutes;
import net.openchrom.xxd.process.supplier.knime.ui.dialog.DialogComponentStringIdSelection;
import net.openchrom.xxd.process.supplier.knime.ui.dialog.SettingsModelStringValidated;

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
	List<Object> dialogComponents = new ArrayList<>();
	private Map<String, DialogComponent> dialogComponentsMap = new HashMap<>();
	/*
	 * Settings models for the node model (never the same as passed with the respective dialog component!).
	 */
	Map<String, SettingsModel> settingsModels = new HashMap<>();
	private Map<String, BiFunction<String, Object, Boolean>> conditions;
	private ChangeListener changeListener = new ChangeListener() {

		@Override
		public void stateChanged(ChangeEvent e) {

			SettingsModel model = (SettingsModel)e.getSource();
			updateDialogComponents(model);
		}
	};

	public PropertyAccess() {
		conditions = new HashMap<>();
	}

	public void build() {

		Iterator<DialogComponent> it = dialogComponentsMap.values().iterator();
		while(it.hasNext()) {
			it.next().getModel().addChangeListener(changeListener);
		}
		it = dialogComponentsMap.values().iterator();
		while(it.hasNext()) {
			updateDialogComponents(it.next().getModel());
		}
	}

	private void updateDialogComponents(SettingsModel model) {

		Object value = null;
		String name = null;
		if(model instanceof SettingsModelBoolean) {
			SettingsModelBoolean modelBoolean = (SettingsModelBoolean)model;
			value = modelBoolean.getBooleanValue();
			name = modelBoolean.getConfigName();
		} else if(model instanceof SettingsModelInteger) {
			SettingsModelInteger modelInteger = (SettingsModelInteger)model;
			name = modelInteger.getKey();
			value = modelInteger.getIntValue();
		} else if(model instanceof SettingsModelDouble) {
			SettingsModelDouble modelDouble = (SettingsModelDouble)model;
			value = modelDouble.getDoubleValue();
			name = modelDouble.getKey();
		} else if(model instanceof SettingsModelString) {
			SettingsModelString modelString = (SettingsModelString)model;
			value = modelString.getStringValue();
			name = modelString.getKey();
		}
		if(name != null && value != null) {
			for(Map.Entry<String, DialogComponent> entry : dialogComponentsMap.entrySet()) {
				BiFunction<String, Object, Boolean> condition = conditions.get(entry.getKey());
				if(condition != null) {
					boolean b = condition.apply(name, value);
					entry.getValue().setEnabled(b);
				}
			}
		}
	}

	@Override
	public void addBooleanProperty(String id, String name, boolean defaultValue, String description) {

		addPropertyDescriptions(name, description);
		addComponent(id, new DialogComponentBoolean(new SettingsModelBoolean(id, defaultValue), name));
		settingsModels.put(id, new SettingsModelBoolean(id, defaultValue));
	}

	@Override
	public void addDoubleProperty(String id, String name, double defaultValue, String description, double step, double min, double max) {

		addPropertyDescriptions(name, description);
		addComponent(id, new DialogComponentNumber(new SettingsModelDoubleBounded(id, defaultValue, min, max), name, step));
		settingsModels.put(id, new SettingsModelDouble(id, defaultValue));
	}

	@Override
	public void addFloatProperty(String id, String name, float defaultValue, String description, float step, float min, float max) {

		addPropertyDescriptions(name, description);
		addComponent(id, new DialogComponentNumber(new SettingsModelDoubleBounded(id, defaultValue, min, max), name, step));
		settingsModels.put(id, new SettingsModelDouble(id, defaultValue));
	}

	@Override
	public void addIntProperty(String id, String name, int defaultValue, String description, int step, int min, int max) {

		addPropertyDescriptions(name, description);
		addComponent(id, new DialogComponentNumber(new SettingsModelIntegerBounded(id, defaultValue, min, max), name, step));
		settingsModels.put(id, new SettingsModelInteger(id, defaultValue));
	}

	@Override
	public void addIntOddNumberProperty(String id, String name, int defaultValue, String description, int step, int min, int max) {

		addPropertyDescriptions(name, description);
		addComponent(id, new DialogComponentNumber(new SettingsModelOddIntegerBounded(id, defaultValue, min, max), name, step));
		settingsModels.put(id, new SettingsModelInteger(id, defaultValue));
	}

	private void addPropertyDescriptions(String name, String description) {

		descriptions.put(name, description);
	}

	@Override
	public void addStringProperty(String id, String name, String defaultValue, String description, String regExp) {

		addPropertyDescriptions(name, description);
		addComponent(id, new DialogComponentString(new SettingsModelStringValidated(id, defaultValue, regExp), name));
		settingsModels.put(id, new SettingsModelString(id, defaultValue));
	}

	@Override
	public void addStringProperty(String id, String name, String defaultValue, String description, Collection<String> list, Map<String, String> ids) {

		addPropertyDescriptions(name, description);
		addComponent(id, new DialogComponentStringIdSelection(new SettingsModelString(id, defaultValue), name, list, ids, false));
		settingsModels.put(id, new SettingsModelString(id, defaultValue));
	}

	@Override
	public void addFileProperty(String id, String name, String defaultValue, String description, String idHistory, DialogType dialogType, boolean directoryOnly, String[] extension) {

		addPropertyDescriptions(name, description);
		switch(dialogType) {
			case OPEN_DIALOG:
				addComponent(id, new DialogComponentFileChooser(new SettingsModelString(id, defaultValue), idHistory, JFileChooser.OPEN_DIALOG, directoryOnly, extension));
				break;
			case SAVE_DIALOG:
				addComponent(id, new DialogComponentFileChooser(new SettingsModelString(id, defaultValue), idHistory, JFileChooser.SAVE_DIALOG, directoryOnly, extension));
				break;
		}
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
		addComponent(id, new DialogComponentIonSelection(new SettingsModelString(id, defaultValue), name));
		settingsModels.put(id, new SettingsModelString(id, defaultValue));
	}

	@Override
	public void addMultiFileProperty(String id, String name, String defaultValue, String description, String idHistory, String[] extensions) {

		addPropertyDescriptions(name, description);
		addComponent(id, new DialogComponentMultiFileChooser(new SettingsModelString(id, defaultValue), name, extensions));
		settingsModels.put(id, new SettingsModelString(id, defaultValue));
	}

	@Override
	public void addStringProperty(String id, String name, String defaultValue, String description, ButtonGroupEnumInterface[] list) {

		addPropertyDescriptions(name, description);
		addComponent(id, new DialogComponentButtonGroup(new SettingsModelString(id, defaultValue), name, true, list));
		settingsModels.put(id, new SettingsModelString(id, defaultValue));
	}

	@Override
	public void addCondition(String id, BiFunction<String, Object, Boolean> condition) {

		conditions.put(id, condition);
	}

	@Override
	public void addRetentionTimeMinutesProperty(String id, String name, int defaultValue, String description, int step, int min, int max) {

		addPropertyDescriptions(name, description);
		addComponent(id, new DialogComponentRetentionTimeMinutes(new SettingsModelIntegerBounded(id, defaultValue, min, max), name, step, 10));
		settingsModels.put(id, new SettingsModelInteger(id, defaultValue));
	}

	@Override
	public void addLabel(String label) {

		addComponent(new DialogComponentLabel(label));
	}

	private void addComponent(String id, DialogComponent dialogComponent) {

		dialogComponentsMap.put(id, dialogComponent);
		dialogComponents.add(dialogComponent);
	}

	private void addComponent(DialogComponent dialogComponent) {

		dialogComponents.add(dialogComponent);
	}

	@Override
	public void createGroup(String title) {

		dialogComponents.add(new CreateNewGroup(title));
	}

	@Override
	public void closeGroup() {

		dialogComponents.add(new CloseGroup());
	}

	@Override
	public void createNewTab(String title, boolean isDefault) {

		dialogComponents.add(new CreateNewTab(title, isDefault));
	}
}
