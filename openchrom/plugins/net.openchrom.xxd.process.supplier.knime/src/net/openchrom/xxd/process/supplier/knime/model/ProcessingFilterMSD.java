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

import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.chromatogram.msd.filter.core.chromatogram.ChromatogramFilterMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ProcessingFilterMSD extends AbstractChromatogramSelectionProcessing<IChromatogramFilterSettings, IChromatogramSelectionMSD> {

	/**
	 *
	 */
	private static final long serialVersionUID = 2187975630300838445L;

	protected ProcessingFilterMSD() {
		super();
	}

	public ProcessingFilterMSD(String id) throws JsonProcessingException {
		super(id);
	}

	public ProcessingFilterMSD(String id, IChromatogramFilterSettings settings) throws JsonProcessingException {
		super(id, settings);
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionMSD chromatogramSelection, String id) throws Exception {

		return ChromatogramFilterMSD.applyFilter(chromatogramSelection, id, new NullProgressMonitor());
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionMSD chromatogramSelection, String id, IChromatogramFilterSettings setting) {

		return ChromatogramFilterMSD.applyFilter(chromatogramSelection, setting, id, new NullProgressMonitor());
	}
}
