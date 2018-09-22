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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.identifier.peak.PeakIdentifierMSD;
import org.eclipse.chemclipse.chromatogram.msd.identifier.settings.IPeakIdentifierSettingsMSD;
import org.eclipse.chemclipse.model.exceptions.NoIdentifierAvailableException;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ProcessingPeakIdentifierMSD extends AbstractChromatogramSelectionProcessing<IPeakIdentifierSettingsMSD, IChromatogramSelectionMSD> {

	/**
	 *
	 */
	private static final long serialVersionUID = -1128763603573631815L;

	protected ProcessingPeakIdentifierMSD() {

		super();
	}

	public ProcessingPeakIdentifierMSD(String id) {

		super(id);
	}

	public ProcessingPeakIdentifierMSD(String id, IPeakIdentifierSettingsMSD settings) throws JsonProcessingException {

		super(id, settings);
	}

	@Override
	protected Class<? extends IPeakIdentifierSettingsMSD> getSettingsClass(String id) throws Exception {

		return PeakIdentifierMSD.getPeakIdentifierSupport().getIdentifierSupplier(id).getIdentifierSettingsClass();
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionMSD chromatogramSelection, String id, IPeakIdentifierSettingsMSD settings, IProgressMonitor monitor) throws Exception {

		List<IChromatogramPeakMSD> peaks = chromatogramSelection.getChromatogramMSD().getPeaks(chromatogramSelection);
		List<IPeakMSD> peakList = new ArrayList<IPeakMSD>();
		for(IChromatogramPeakMSD chromatogramPeak : peaks) {
			peakList.add(chromatogramPeak);
		}
		return PeakIdentifierMSD.identify(peakList, settings, id, monitor);
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionMSD chromatogramSelection, String id, IProgressMonitor monitor) throws Exception {

		List<IChromatogramPeakMSD> peaks = chromatogramSelection.getChromatogramMSD().getPeaks(chromatogramSelection);
		List<IPeakMSD> peakList = new ArrayList<IPeakMSD>();
		for(IChromatogramPeakMSD chromatogramPeak : peaks) {
			peakList.add(chromatogramPeak);
		}
		return PeakIdentifierMSD.identify(peakList, id, monitor);
	}

	@Override
	public String getName() {

		try {
			return PeakIdentifierMSD.getPeakIdentifierSupport().getIdentifierSupplier(getId()).getIdentifierName();
		} catch(NoIdentifierAvailableException e) {
			// TODO:
		}
		return null;
	}

	@Override
	public String getDescription() {

		try {
			return PeakIdentifierMSD.getPeakIdentifierSupport().getIdentifierSupplier(getId()).getDescription();
		} catch(NoIdentifierAvailableException e) {
			// TODO:
		}
		return null;
	}
}
