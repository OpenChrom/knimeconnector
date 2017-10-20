/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
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

import org.eclipse.chemclipse.chromatogram.csd.filter.core.chromatogram.ChromatogramFilterCSD;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ProcessingFilterCSD extends AbstractChromatogramSelectionProcessing<IChromatogramFilterSettings, IChromatogramSelectionCSD> {

	/**
	 *
	 */
	private static final long serialVersionUID = 619949636114922425L;

	protected ProcessingFilterCSD() {
		super();
	}

	public ProcessingFilterCSD(String id) {
		super(id);
	}

	public ProcessingFilterCSD(String id, IChromatogramFilterSettings settings) throws JsonProcessingException {
		super(id, settings);
	}

	@Override
	protected Class<? extends IChromatogramFilterSettings> getSettingsClass(String id) throws Exception {

		return ChromatogramFilterCSD.getChromatogramFilterSupport().getFilterSupplier(id).getFilterSettingsClass();
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionCSD chromatogramSelection, String id, IChromatogramFilterSettings setting, IProgressMonitor monitor) throws Exception {

		return ChromatogramFilterCSD.applyFilter(chromatogramSelection, setting, id, monitor);
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionCSD chromatogramSelection, String id, IProgressMonitor monitor) throws Exception {

		return ChromatogramFilterCSD.applyFilter(chromatogramSelection, id, monitor);
	}
}
