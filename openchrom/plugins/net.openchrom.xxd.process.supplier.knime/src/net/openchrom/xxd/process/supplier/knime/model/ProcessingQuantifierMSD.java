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
package net.openchrom.xxd.process.supplier.knime.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.quantitation.core.PeakQuantifier;
import org.eclipse.chemclipse.chromatogram.msd.quantitation.exceptions.NoPeakQuantifierAvailableException;
import org.eclipse.chemclipse.chromatogram.msd.quantitation.settings.IPeakQuantifierSettings;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ProcessingQuantifierMSD extends AbstractChromatogramSelectionProcessing<IPeakQuantifierSettings, IChromatogramSelectionMSD> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5598830993160485997L;

	protected ProcessingQuantifierMSD() {

		super();
	}

	public ProcessingQuantifierMSD(String id, IPeakQuantifierSettings settings) throws JsonProcessingException {

		super(id, settings);
	}

	public ProcessingQuantifierMSD(String id) {

		super(id);
	}

	@Override
	public String getName() {

		try {
			return PeakQuantifier.getPeakQuantifierSupport().getPeakQuantifierSupplier(getId()).getPeakQuantifierName();
		} catch(NoPeakQuantifierAvailableException e) {
		}
		return null;
	}

	@Override
	public String getDescription() {

		try {
			return PeakQuantifier.getPeakQuantifierSupport().getPeakQuantifierSupplier(getId()).getDescription();
		} catch(NoPeakQuantifierAvailableException e) {
		}
		return null;
	}

	@Override
	protected Class<? extends IPeakQuantifierSettings> getSettingsClass(String id) throws Exception {

		try {
			return PeakQuantifier.getPeakQuantifierSupport().getPeakQuantifierSupplier(getId()).getQuantifierSettingsClass();
		} catch(NoPeakQuantifierAvailableException e) {
		}
		return null;
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionMSD chromatogramSelection, String id, IProgressMonitor monitor) throws Exception {

		List<IChromatogramPeakMSD> peaks = chromatogramSelection.getChromatogramMSD().getPeaks(chromatogramSelection);
		List<IPeak> peakList = new ArrayList<IPeak>();
		for(IChromatogramPeakMSD chromatogramPeak : peaks) {
			peakList.add(chromatogramPeak);
		}
		return PeakQuantifier.quantify(peakList, id, monitor);
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionMSD chromatogramSelection, String id, IPeakQuantifierSettings settings, IProgressMonitor monitor) throws Exception {

		List<IChromatogramPeakMSD> peaks = chromatogramSelection.getChromatogramMSD().getPeaks(chromatogramSelection);
		List<IPeak> peakList = new ArrayList<IPeak>();
		for(IChromatogramPeakMSD chromatogramPeak : peaks) {
			peakList.add(chromatogramPeak);
		}
		return PeakQuantifier.quantify(peakList, settings, id, monitor);
	}
}
