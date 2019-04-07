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
package net.openchrom.process.supplier.knime.model;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.eclipse.chemclipse.model.core.IMeasurementInfo;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.openchrom.process.supplier.knime.dialogfactory.SettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;
import net.openchrom.process.supplier.knime.dialogfactory.property.ProperySettingsSerializable;

public abstract class AbstractDataReport<Settings, Data extends IMeasurementInfo> extends AbstractDataOutput<Data> implements IDataReport<Data> {

	private static final int INTERNAL_VERSION_ID = 1;
	//
	private boolean append = false;
	private ProperySettingsSerializable properySettings;

	public AbstractDataReport() {

		super();
	}

	public AbstractDataReport(String id, File directory) {

		super(id, directory);
		append = false;
	}

	public AbstractDataReport(String id, File directory, PropertyProvider prov) throws JsonProcessingException {

		super(id, directory);
		properySettings = new ProperySettingsSerializable(prov);
		append = false;
	}

	public AbstractDataReport(String id, File directory, String fileName) {

		super(id, directory, fileName);
		append = true;
	}

	public AbstractDataReport(String id, File directory, String fileName, PropertyProvider prov) throws JsonProcessingException {

		super(id, directory, fileName);
		properySettings = new ProperySettingsSerializable(prov);
		append = true;
	}

	@Override
	public boolean isAppend() {

		return append;
	}

	@Override
	public void setAppend(boolean append) {

		this.append = append;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

		super.readExternal(in);
		int version = in.readInt();
		switch(version) {
			case 1:
				append = in.readBoolean();
				properySettings = (ProperySettingsSerializable)in.readObject();
				break;
			default:
				break;
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {

		super.writeExternal(out);
		out.writeInt(INTERNAL_VERSION_ID);
		out.writeBoolean(append);
		out.writeObject(properySettings);
	}

	protected abstract Class<? extends Settings> getSettingsClass(String id) throws Exception;

	protected abstract SettingObjectSupplier<? extends Settings> getSettingsClassSupplier();

	@Override
	public IProcessingInfo process(Data data, IProgressMonitor monitor) throws Exception {

		String id = getId();
		File file = new File(generateFilePath(data));
		if(properySettings == null) {
			return process(data, file, id, append, monitor);
		} else {
			Class<? extends Settings> settingClass = getSettingsClass(id);
			SettingObjectSupplier settingsClassSupplier = getSettingsClassSupplier();
			Settings settingsObject = (Settings)settingsClassSupplier.createSettingsObject(settingClass, properySettings);
			return process(data, file, id, append, settingsObject, monitor);
		}
	}

	protected abstract IProcessingInfo process(Data data, File file, String id, boolean append, IProgressMonitor monitor) throws Exception;

	protected abstract IProcessingInfo process(Data data, File file, String id, boolean append, Settings settings, IProgressMonitor monitor) throws Exception;
}
