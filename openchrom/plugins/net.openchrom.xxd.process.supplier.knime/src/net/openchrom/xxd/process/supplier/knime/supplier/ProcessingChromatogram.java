/*******************************************************************************
 * Copyright (c) 2017 Jan Holy
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.supplier;

import org.eclipse.chemclipse.chromatogram.csd.filter.core.chromatogram.ChromatogramFilterCSD;
import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.ChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.IChromatogramFilterSupplier;
import org.eclipse.chemclipse.chromatogram.filter.exceptions.NoChromatogramFilterSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.filter.processing.IChromatogramFilterProcessingInfo;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.chromatogram.msd.filter.core.chromatogram.ChromatogramFilterMSD;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.core.runtime.IProgressMonitor;

public class ProcessingChromatogram {

	public static IChromatogramFilterProcessingInfo apply(IChromatogramSelection chromatogramSelection, IChromatogramFilterSettings setting, String id, IProgressMonitor monitor) throws NoChromatogramFilterSupplierAvailableException {

		if(isFilter(id)) {
			return ChromatogramFilter.applyFilter(chromatogramSelection, id, monitor);
		} else if(isFilterMSD(id)) {
			return ChromatogramFilterMSD.applyFilter((IChromatogramSelectionMSD)chromatogramSelection, setting, id, monitor);
		} else if(isFilterCSD(id)) {
			return ChromatogramFilterCSD.applyFilter((IChromatogramSelectionCSD)chromatogramSelection, setting, id, monitor);
		}
		throw new NoChromatogramFilterSupplierAvailableException();
	}

	public static Class<? extends IChromatogramFilterSettings> getFilterSettingsClass(String id) throws NoChromatogramFilterSupplierAvailableException {

		if(isFilter(id)) {
			return ChromatogramFilter.getChromatogramFilterSupport().getFilterSupplier(id).getFilterSettingsClass();
		} else if(isFilterMSD(id)) {
			return ChromatogramFilterMSD.getChromatogramFilterSupport().getFilterSupplier(id).getFilterSettingsClass();
		} else if(isFilterCSD(id)) {
			return ChromatogramFilterCSD.getChromatogramFilterSupport().getFilterSupplier(id).getFilterSettingsClass();
		}
		throw new NoChromatogramFilterSupplierAvailableException();
	}

	public static IChromatogramFilterSupplier getSupplier(String id) throws NoChromatogramFilterSupplierAvailableException {

		if(isFilter(id)) {
			return ChromatogramFilter.getChromatogramFilterSupport().getFilterSupplier(id);
		} else if(isFilterMSD(id)) {
			return ChromatogramFilterMSD.getChromatogramFilterSupport().getFilterSupplier(id);
		} else if(isFilterCSD(id)) {
			return ChromatogramFilterCSD.getChromatogramFilterSupport().getFilterSupplier(id);
		}
		throw new NoChromatogramFilterSupplierAvailableException();
	}

	private static boolean isFilter(String id) {

		try {
			return ChromatogramFilter.getChromatogramFilterSupport().getAvailableFilterIds().contains(id);
		} catch(NoChromatogramFilterSupplierAvailableException e) {
			return false;
		}
	}

	private static boolean isFilterCSD(String id) {

		try {
			return ChromatogramFilterCSD.getChromatogramFilterSupport().getAvailableFilterIds().contains(id);
		} catch(NoChromatogramFilterSupplierAvailableException e) {
			return false;
		}
	}

	private static boolean isFilterMSD(String id) {

		try {
			return ChromatogramFilterMSD.getChromatogramFilterSupport().getAvailableFilterIds().contains(id);
		} catch(NoChromatogramFilterSupplierAvailableException e) {
			return false;
		}
	}
}
