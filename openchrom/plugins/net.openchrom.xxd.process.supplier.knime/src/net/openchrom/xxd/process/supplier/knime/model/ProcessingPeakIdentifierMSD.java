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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.identifier.peak.PeakIdentifier;
import org.eclipse.chemclipse.chromatogram.msd.identifier.settings.IPeakIdentifierSettings;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ProcessingPeakIdentifierMSD extends AbstractChromatogramSelectionProcessing<IPeakIdentifierSettings, IChromatogramSelectionMSD> {

	/**
	 *
	 */
	private static final long serialVersionUID = -1128763603573631815L;

	protected ProcessingPeakIdentifierMSD() {
		super();
	}

	public ProcessingPeakIdentifierMSD(String id) throws JsonProcessingException {
		super(id);
	}

	public ProcessingPeakIdentifierMSD(String id, IPeakIdentifierSettings settings) throws JsonProcessingException {
		super(id, settings);
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionMSD chromatogramSelection, String id, IPeakIdentifierSettings settings, IProgressMonitor monitor) throws Exception {

		List<IChromatogramPeakMSD> peaks = chromatogramSelection.getChromatogramMSD().getPeaks(chromatogramSelection);
		List<IPeakMSD> peakList = new ArrayList<IPeakMSD>();
		for(IChromatogramPeakMSD chromatogramPeak : peaks) {
			peakList.add(chromatogramPeak);
		}
		return PeakIdentifier.identify(peakList, settings, id, monitor);
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionMSD chromatogramSelection, String id, IProgressMonitor monitor) throws Exception {

		List<IChromatogramPeakMSD> peaks = chromatogramSelection.getChromatogramMSD().getPeaks(chromatogramSelection);
		List<IPeakMSD> peakList = new ArrayList<IPeakMSD>();
		for(IChromatogramPeakMSD chromatogramPeak : peaks) {
			peakList.add(chromatogramPeak);
		}
		return PeakIdentifier.identify(peakList, id, monitor);
	}
}
