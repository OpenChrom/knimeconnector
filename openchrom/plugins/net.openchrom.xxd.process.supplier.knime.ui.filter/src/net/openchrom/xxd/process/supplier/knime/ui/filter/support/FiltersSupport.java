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
package net.openchrom.xxd.process.supplier.knime.ui.filter.support;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.csd.filter.core.chromatogram.ChromatogramFilterCSD;
import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.ChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.IChromatogramFilterSupplier;
import org.eclipse.chemclipse.chromatogram.filter.exceptions.NoChromatogramFilterSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.chromatogram.msd.filter.core.chromatogram.ChromatogramFilterMSD;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;
import net.openchrom.xxd.process.supplier.knime.model.IChromatogramSelectionProcessing;
import net.openchrom.xxd.process.supplier.knime.model.ProcessingFilter;
import net.openchrom.xxd.process.supplier.knime.model.ProcessingFilterCSD;
import net.openchrom.xxd.process.supplier.knime.model.ProcessingFilterMSD;

public class FiltersSupport {

	public static IProcessingInfo apply(IChromatogramSelection chromatogramSelection, IChromatogramFilterSettings setting, String id, IProgressMonitor monitor) throws NoChromatogramFilterSupplierAvailableException {

		if(isFilter(id)) {
			return ChromatogramFilter.applyFilter(chromatogramSelection, setting, id, monitor);
		} else if(isFilterMSD(id)) {
			return ChromatogramFilterMSD.applyFilter((IChromatogramSelectionMSD)chromatogramSelection, setting, id, monitor);
		} else if(isFilterCSD(id)) {
			return ChromatogramFilterCSD.applyFilter((IChromatogramSelectionCSD)chromatogramSelection, setting, id, monitor);
		}
		throw new NoChromatogramFilterSupplierAvailableException();
	}

	public static List<String> getIDsFilterChromatogramCSD() throws NoChromatogramFilterSupplierAvailableException {

		List<String> ids = new ArrayList<>();
		try {
			ids.addAll(ChromatogramFilter.getChromatogramFilterSupport().getAvailableFilterIds());
		} catch(NoChromatogramFilterSupplierAvailableException e) {
		}
		try {
			ids.addAll(ChromatogramFilterCSD.getChromatogramFilterSupport().getAvailableFilterIds());
		} catch(NoChromatogramFilterSupplierAvailableException e) {
		}
		if(ids.isEmpty()) {
			throw new NoChromatogramFilterSupplierAvailableException();
		}
		return ids;
	}

	public static List<String> getIDsFilterChromatogramMSD() throws NoChromatogramFilterSupplierAvailableException {

		List<String> ids = new ArrayList<>();
		try {
			ids.addAll(ChromatogramFilter.getChromatogramFilterSupport().getAvailableFilterIds());
		} catch(NoChromatogramFilterSupplierAvailableException e) {
		}
		try {
			ids.addAll(ChromatogramFilterMSD.getChromatogramFilterSupport().getAvailableFilterIds());
		} catch(NoChromatogramFilterSupplierAvailableException e) {
		}
		if(ids.isEmpty()) {
			throw new NoChromatogramFilterSupplierAvailableException();
		}
		return ids;
	}

	public static IChromatogramSelectionProcessing<? super IChromatogramSelectionCSD> getProcessingFilterChromatogramCSD(String id) throws NoChromatogramFilterSupplierAvailableException {

		if(isFilter(id)) {
			return new ProcessingFilter(id);
		} else if(isFilterMSD(id)) {
			return new ProcessingFilterCSD(id);
		}
		throw new NoChromatogramFilterSupplierAvailableException();
	}

	public static IChromatogramSelectionProcessing<? super IChromatogramSelectionCSD> getProcessingFilterChromatogramCSD(String id, PropertyProvider prov) throws Exception {

		if(isFilter(id)) {
			return new ProcessingFilter(id, prov);
		} else if(isFilterMSD(id)) {
			return new ProcessingFilterCSD(id, prov);
		}
		throw new NoChromatogramFilterSupplierAvailableException();
	}

	public static IChromatogramSelectionProcessing<? super IChromatogramSelectionMSD> getProcessingFilterChromatogramMSD(String id) throws NoChromatogramFilterSupplierAvailableException {

		if(isFilter(id)) {
			return new ProcessingFilter(id);
		} else if(isFilterMSD(id)) {
			return new ProcessingFilterMSD(id);
		}
		throw new NoChromatogramFilterSupplierAvailableException();
	}

	public static IChromatogramSelectionProcessing<? super IChromatogramSelectionMSD> getProcessingFilterChromatogramMSD(String id, PropertyProvider prov) throws Exception {

		if(isFilter(id)) {
			return new ProcessingFilter(id, prov);
		} else if(isFilterMSD(id)) {
			return new ProcessingFilterMSD(id, prov);
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
