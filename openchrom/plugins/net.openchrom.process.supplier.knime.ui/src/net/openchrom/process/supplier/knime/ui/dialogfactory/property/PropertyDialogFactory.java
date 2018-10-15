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
package net.openchrom.process.supplier.knime.ui.dialogfactory.property;

import java.util.Map;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.SettingsModel;

import net.openchrom.process.supplier.knime.ui.dialogfactory.SettingObjectSupplier;
import net.openchrom.process.supplier.knime.ui.dialogfactory.SettingsDialogFactory;
import net.openchrom.process.supplier.knime.ui.dialogfactory.SettingsObjectWrapper;

/**
 * Implementation of a {@link SettingsDialogFactory} that allows one to more conveniently add a predefined set of properties (see {@link PropertyCollector} and {@link PropertyProvider}). The dialog (and it's settings models) are then automatically generated from those.
 *
 * @author Martin Horn, University of Konstanz
 *
 * @param <SO>
 *            see {@link SettingsDialogFactory}
 */
public abstract class PropertyDialogFactory<SO> implements SettingsDialogFactory<SO>, SettingObjectSupplier<SO> {

	private PropertyAccess propertyAccess;
	private Class<? extends SO> settingsObjectClass;

	@Override
	public boolean conforms(Class<SO> settingsObjectClass) {

		return true;
	}

	@Override
	public boolean conforms(SO settingsObject) {

		return true;
	}

	@Override
	public NodeDialogPane createDialog() {

		DefaultNodeSettingsPane defaultNodeSettingsPane = new DefaultNodeSettingsPane();
		builtDialogPane(defaultNodeSettingsPane);
		return defaultNodeSettingsPane;
	}

	protected void builtDialogPane(DefaultNodeSettingsPane defaultNodeSettingsPane) {

		String defaultTab = null;
		for(Object object : getPropertyAccess(settingsObjectClass).dialogComponents) {
			if(object instanceof DialogComponent) {
				defaultNodeSettingsPane.addDialogComponent((DialogComponent)object);
			}
			if(object instanceof CreateNewGroup) {
				CreateNewGroup newGroup = (CreateNewGroup)object;
				defaultNodeSettingsPane.createNewGroup(newGroup.getTitle());
			}
			if(object instanceof CloseGroup) {
				defaultNodeSettingsPane.closeCurrentGroup();
			}
			if(object instanceof CreateNewTab) {
				CreateNewTab newTab = (CreateNewTab)object;
				defaultNodeSettingsPane.createNewTab(newTab.getTabName());
				if(newTab.isDefault()) {
					defaultTab = newTab.getTabName();
				}
			}
		}
		if(defaultTab != null) {
			defaultNodeSettingsPane.setDefaultTabTitle(defaultTab);
		}
	}

	@Override
	public Map<String, String> createDialogOptionDescriptions() {

		return getPropertyAccess(settingsObjectClass).descriptions;
	}

	@Override
	public SettingsObjectWrapper<SO> createSettingsObjectWrapper() {

		return new SettingsObjectWrapper<SO>() {

			private SO settingsObject;

			@Override
			public SO getObject() {

				if(settingsObject == null) {
					settingsObject = createSettingsObject(settingsObjectClass, getPropertyAccess(settingsObjectClass));
				}
				return settingsObject;
			}

			@Override
			public void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {

				for(SettingsModel sm : getPropertyAccess(settingsObjectClass).settingsModels.values()) {
					sm.loadSettingsFrom(settings);
				}
				// invalidate settings object;
				settingsObject = null;
			}

			@Override
			public void saveSettingsTo(NodeSettingsWO settings) {

				for(SettingsModel sm : getPropertyAccess(settingsObjectClass).settingsModels.values()) {
					sm.saveSettingsTo(settings);
				}
			}

			@Override
			public void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

				for(SettingsModel sm : getPropertyAccess(settingsObjectClass).settingsModels.values()) {
					sm.validateSettings(settings);
				}
			}
		};
	}

	@Override
	public int getPriority() {

		return 0;
	}

	private PropertyAccess getPropertyAccess(Class<? extends SO> settingsObjectClass) {

		if(propertyAccess == null) {
			propertyAccess = new PropertyAccess();
			extractProperties(settingsObjectClass, propertyAccess);
		}
		return propertyAccess;
	}

	@Override
	public void setSettingsObjectClass(Class<? extends SO> settingsObjectClass) {

		this.settingsObjectClass = settingsObjectClass;
	}
}
