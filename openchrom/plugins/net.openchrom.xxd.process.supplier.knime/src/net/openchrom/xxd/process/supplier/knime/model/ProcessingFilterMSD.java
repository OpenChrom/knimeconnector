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
import java.io.ObjectInputStream;

import org.eclipse.chemclipse.chromatogram.filter.exceptions.NoChromatogramFilterSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.chromatogram.msd.filter.core.chromatogram.ChromatogramFilterMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import net.openchrom.process.supplier.knime.dialogfactory.JacksonSettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.SettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;
import net.openchrom.process.supplier.knime.model.AbstractDataProcessing;

public class ProcessingFilterMSD extends AbstractDataProcessing<IChromatogramFilterSettings, IChromatogramSelectionMSD> {

	/**
	 *
	 */
	private static final long serialVersionUID = 2187975630300838445L;
	private transient SettingObjectSupplier<? extends IChromatogramFilterSettings> settingsClassSupplier = new JacksonSettingObjectSupplier<>();

	protected ProcessingFilterMSD() {

		super();
	}

	public ProcessingFilterMSD(String id) {

		super(id);
	}

	public ProcessingFilterMSD(String id, PropertyProvider prov) throws Exception {

		super(id, prov);
	}

	@Override
	protected Class<? extends IChromatogramFilterSettings> getSettingsClass(String id) throws Exception {

		return ChromatogramFilterMSD.getChromatogramFilterSupport().getFilterSupplier(id).getSettingsClass();
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionMSD chromatogramSelection, String id, IChromatogramFilterSettings setting, IProgressMonitor monitor) {

		return ChromatogramFilterMSD.applyFilter(chromatogramSelection, setting, id, monitor);
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionMSD chromatogramSelection, String id, IProgressMonitor monitor) throws Exception {

		return ChromatogramFilterMSD.applyFilter(chromatogramSelection, id, monitor);
	}

	@Override
	public String getName() {

		try {
			return ChromatogramFilterMSD.getChromatogramFilterSupport().getFilterSupplier(getId()).getFilterName();
		} catch(NoChromatogramFilterSupplierAvailableException e) {
		}
		return null;
	}

	@Override
	public String getDescription() {

		try {
			return ChromatogramFilterMSD.getChromatogramFilterSupport().getFilterSupplier(getId()).getDescription();
		} catch(NoChromatogramFilterSupplierAvailableException e) {
		}
		return null;
	}

	@Override
	protected SettingObjectSupplier<? extends IChromatogramFilterSettings> getSettingsClassSupplier() {

		return settingsClassSupplier;
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {

		in.defaultReadObject();
		settingsClassSupplier = new JacksonSettingObjectSupplier<>();
	}
}
