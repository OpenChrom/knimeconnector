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
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.dialogfactory.property;

import java.util.Map;

import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.dialogfactory.SettingsDialogFactory;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.dialogfactory.SettingsObjectWrapper;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.SettingsModel;

/**
 * Implementation of a {@link SettingsDialogFactory} that allows one to more conveniently add a predefined set of properties (see {@link PropertyCollector} and {@link PropertyProvider}). The dialog (and it's settings models) are then automatically generated from those.
 * 
 * @author Martin Horn, University of Konstanz
 *
 * @param <SO>
 *            see {@link SettingsDialogFactory}
 */
public abstract class PropertyDialogFactory<SO> implements SettingsDialogFactory<SO> {

	private PropertyAccess propertyAccess;
	private Class<? extends SO> settingsObjectClass;

	@Override
	public int getPriority() {

		return 0;
	}

	@Override
	public void setSettingsObjectClass(Class<? extends SO> settingsObjectClass) {

		this.settingsObjectClass = settingsObjectClass;
	}

	@Override
	public NodeDialogPane createDialog() {

		return new DefaultNodeSettingsPane() {

			{
				for(DialogComponent dc : getPropertyAccess(settingsObjectClass).dialogComponents) {
					addDialogComponent(dc);
				}
			}
		};
	}

	@Override
	public SettingsObjectWrapper<SO> createSettingsObjectWrapper() {

		return new SettingsObjectWrapper<SO>() {

			private SO settingsObject;

			@Override
			public void saveSettingsTo(NodeSettingsWO settings) {

				for(SettingsModel sm : getPropertyAccess(settingsObjectClass).settingsModels.values()) {
					sm.saveSettingsTo(settings);
				}
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
			public void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

				for(SettingsModel sm : getPropertyAccess(settingsObjectClass).settingsModels.values()) {
					sm.validateSettings(settings);
				}
			}

			@Override
			public SO getObject() {

				if(settingsObject == null) {
					settingsObject = createSettingsObject(settingsObjectClass, getPropertyAccess(settingsObjectClass));
				}
				return settingsObject;
			}
		};
	}

	@Override
	public Map<String, String> createDialogOptionDescriptions() {

		return getPropertyAccess(settingsObjectClass).descriptions;
	}

	private PropertyAccess getPropertyAccess(Class<? extends SO> settingsObjectClass) {

		if(propertyAccess == null) {
			propertyAccess = new PropertyAccess();
			extractProperties(settingsObjectClass, propertyAccess);
		}
		return propertyAccess;
	}

	@Override
	public boolean conforms(Class<SO> settingsObjectClass) {

		return true;
	}

	@Override
	public boolean conforms(SO settingsObject) {

		return true;
	}

	/**
	 * Extract the properties, e.g. via reflection represented by getter and setter methods, from the settings object class. The extracted properties are added to the provided {@link PropertyCollector}.
	 * 
	 * @param settingsObjectClass
	 * @param coll
	 */
	public abstract void extractProperties(Class<? extends SO> settingsObjectClass, PropertyCollector coll);

	/**
	 * Creates the actual settings object from the given settings object class and the {@link PropertyProvider}.
	 * 
	 * @param settingsObjectClass
	 * @param prov
	 * @return the settings object instance
	 */
	public abstract SO createSettingsObject(Class<? extends SO> settingsObjectClass, PropertyProvider prov);
}
