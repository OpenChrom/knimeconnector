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

import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.ChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.filter.settings.ChromatogramFilterSettings;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ProcessingFilter extends AbstractChromatogramSelectionProcessing<ChromatogramFilterSettings, IChromatogramSelection> {

	/**
	 *
	 */
	private static final long serialVersionUID = -6108017542495304306L;

	protected ProcessingFilter() {
		super();
	}

	public ProcessingFilter(String id) throws JsonProcessingException {
		super(id);
	}

	public ProcessingFilter(String id, ChromatogramFilterSettings settings) throws JsonProcessingException {
		super(id, settings);
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelection chromatogramSelection, String id) throws Exception {

		return ChromatogramFilter.applyFilter(chromatogramSelection, id, new NullProgressMonitor());
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelection chromatogramSelection, String id, ChromatogramFilterSettings setting) throws Exception {

		return ChromatogramFilter.applyFilter(chromatogramSelection, setting, id, new NullProgressMonitor());
	}
}
