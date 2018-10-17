/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.process.supplier.knime.model;

import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import net.openchrom.process.supplier.knime.dialogfactory.SettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;
import net.openchrom.process.supplier.knime.dialogfactory.property.ProperySettingsSerializable;

public abstract class AbstractDataProcessing<Settings, Data> implements IDataProcessing<Data> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6352759936523585244L;
	private String id;
	private ProperySettingsSerializable properySettings;

	protected AbstractDataProcessing() {

	}

	public AbstractDataProcessing(String id) {

		this();
		if(id == null) {
			throw new NullPointerException("Parameter ID cannot be null");
		}
		this.id = id;
	}

	public AbstractDataProcessing(String id, PropertyProvider prov) throws Exception {

		this(id);
		if(prov == null) {
			throw new NullPointerException("Parameter Settings cannot be null");
		}
		properySettings = new ProperySettingsSerializable(prov);
	}

	protected abstract Class<? extends Settings> getSettingsClass(String id) throws Exception;

	protected abstract SettingObjectSupplier<? extends Settings> getSettingsClassSupplier();

	@Override
	public IProcessingInfo process(Data data, IProgressMonitor monitor) throws Exception {

		if(properySettings == null) {
			return process(data, id, monitor);
		} else {
			Class<? extends Settings> settingClass = getSettingsClass(id);
			SettingObjectSupplier settingsClassSupplier = getSettingsClassSupplier();
			Settings settingsObject = (Settings)settingsClassSupplier.createSettingsObject(settingClass, properySettings);
			return process(data, id, settingsObject, monitor);
		}
	}

	@Override
	public String getId() {

		return id;
	}

	protected abstract IProcessingInfo process(Data data, String id, IProgressMonitor monitor) throws Exception;

	protected abstract IProcessingInfo process(Data data, String id, Settings settings, IProgressMonitor monitor) throws Exception;
}
