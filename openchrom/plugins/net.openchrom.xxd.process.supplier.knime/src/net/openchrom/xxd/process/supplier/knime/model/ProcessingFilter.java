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

import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.ChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.filter.exceptions.NoChromatogramFilterSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ProcessingFilter extends AbstractChromatogramSelectionProcessing<IChromatogramFilterSettings, IChromatogramSelection> {

	/**
	 *
	 */
	private static final long serialVersionUID = -6108017542495304306L;

	protected ProcessingFilter() {
		super();
	}

	public ProcessingFilter(String id) {
		super(id);
	}

	public ProcessingFilter(String id, IChromatogramFilterSettings settings) throws JsonProcessingException {
		super(id, settings);
	}

	@Override
	protected Class<? extends IChromatogramFilterSettings> getSettingsClass(String id) throws Exception {

		return ChromatogramFilter.getChromatogramFilterSupport().getFilterSupplier(id).getFilterSettingsClass();
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelection chromatogramSelection, String id, IChromatogramFilterSettings setting, IProgressMonitor monitor) throws Exception {

		return ChromatogramFilter.applyFilter(chromatogramSelection, setting, id, monitor);
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelection chromatogramSelection, String id, IProgressMonitor monitor) throws Exception {

		return ChromatogramFilter.applyFilter(chromatogramSelection, id, monitor);
	}

	@Override
	public String getName() {

		try {
			return ChromatogramFilter.getChromatogramFilterSupport().getFilterSupplier(getId()).getFilterName();
		} catch(NoChromatogramFilterSupplierAvailableException e) {
		}
		return null;
	}

	@Override
	public String getDescription() {

		try {
			return ChromatogramFilter.getChromatogramFilterSupport().getFilterSupplier(getId()).getDescription();
		} catch(NoChromatogramFilterSupplierAvailableException e) {
		}
		return null;
	}
}
