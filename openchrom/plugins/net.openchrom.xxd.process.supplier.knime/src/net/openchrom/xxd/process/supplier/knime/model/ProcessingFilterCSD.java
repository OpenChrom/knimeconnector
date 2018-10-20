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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.eclipse.chemclipse.chromatogram.csd.filter.core.chromatogram.ChromatogramFilterCSD;
import org.eclipse.chemclipse.chromatogram.filter.exceptions.NoChromatogramFilterSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import net.openchrom.process.supplier.knime.dialogfactory.JacksonSettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.SettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;
import net.openchrom.process.supplier.knime.model.AbstractDataProcessing;

public class ProcessingFilterCSD extends AbstractDataProcessing<IChromatogramFilterSettings, IChromatogramSelectionCSD> {

	private static final int INTERNAL_VERSION_ID = 1;
	private transient SettingObjectSupplier<? extends IChromatogramFilterSettings> settingsClassSupplier = new JacksonSettingObjectSupplier<>();;

	public ProcessingFilterCSD() {

		super();
	}

	public ProcessingFilterCSD(String id) {

		super(id);
	}

	public ProcessingFilterCSD(String id, PropertyProvider prov) throws Exception {

		super(id, prov);
	}

	@Override
	protected Class<? extends IChromatogramFilterSettings> getSettingsClass(String id) throws Exception {

		return ChromatogramFilterCSD.getChromatogramFilterSupport().getFilterSupplier(id).getSettingsClass();
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionCSD chromatogramSelection, String id, IChromatogramFilterSettings setting, IProgressMonitor monitor) throws Exception {

		return ChromatogramFilterCSD.applyFilter(chromatogramSelection, setting, id, monitor);
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionCSD chromatogramSelection, String id, IProgressMonitor monitor) throws Exception {

		return ChromatogramFilterCSD.applyFilter(chromatogramSelection, id, monitor);
	}

	@Override
	public String getDescription() {

		try {
			return ChromatogramFilterCSD.getChromatogramFilterSupport().getFilterSupplier(getId()).getDescription();
		} catch(NoChromatogramFilterSupplierAvailableException e) {
		}
		return null;
	}

	@Override
	public String getName() {

		try {
			return ChromatogramFilterCSD.getChromatogramFilterSupport().getFilterSupplier(getId()).getFilterName();
		} catch(NoChromatogramFilterSupplierAvailableException e) {
		}
		return null;
	}

	@Override
	protected SettingObjectSupplier<? extends IChromatogramFilterSettings> getSettingsClassSupplier() {

		return settingsClassSupplier;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

		super.readExternal(in);
		int version = in.read();
		switch(version) {
			case 1:
				settingsClassSupplier = new JacksonSettingObjectSupplier<>();
				break;
			default:
				break;
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {

		super.writeExternal(out);
		out.writeInt(INTERNAL_VERSION_ID);
	}
}
