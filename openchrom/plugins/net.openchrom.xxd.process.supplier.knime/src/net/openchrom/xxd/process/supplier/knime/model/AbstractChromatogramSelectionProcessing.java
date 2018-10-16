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
package net.openchrom.xxd.process.supplier.knime.model;

import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import net.openchrom.process.supplier.knime.dialogfactory.SettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;
import net.openchrom.process.supplier.knime.dialogfactory.property.ProperySettingsSerializable;

public abstract class AbstractChromatogramSelectionProcessing<Settings, ChromatogramSelection extends IChromatogramSelection> implements IChromatogramSelectionProcessing<ChromatogramSelection> {

	private String id;
	private ProperySettingsSerializable properySettings;

	protected AbstractChromatogramSelectionProcessing() {

	}

	public AbstractChromatogramSelectionProcessing(String id) {

		this();
		if(id == null) {
			throw new NullPointerException("Parameter ID cannot be null");
		}
		this.id = id;
	}

	public AbstractChromatogramSelectionProcessing(String id, PropertyProvider prov) throws Exception {

		this(id);
		if(prov == null) {
			throw new NullPointerException("Parameter Settings cannot be null");
		}
		properySettings = new ProperySettingsSerializable(prov);
	}

	protected abstract Class<? extends Settings> getSettingsClass(String id) throws Exception;

	protected abstract SettingObjectSupplier<? extends Settings> getSettingsClassSupplier();

	@Override
	public IProcessingInfo process(ChromatogramSelection chromatogramSelection, IProgressMonitor monitor) throws Exception {

		if(properySettings == null) {
			return process(chromatogramSelection, id, monitor);
		} else {
			Class<? extends Settings> settingClass = getSettingsClass(id);
			SettingObjectSupplier settingsClassSupplier = getSettingsClassSupplier();
			Settings settingsObject = (Settings)settingsClassSupplier.createSettingsObject(settingClass, properySettings);
			return process(chromatogramSelection, id, settingsObject, monitor);
		}
	}

	@Override
	public String getId() {

		return id;
	}

	protected abstract IProcessingInfo process(ChromatogramSelection chromatogramSelection, String id, IProgressMonitor monitor) throws Exception;

	protected abstract IProcessingInfo process(ChromatogramSelection chromatogramSelection, String id, Settings settings, IProgressMonitor monitor) throws Exception;
}
