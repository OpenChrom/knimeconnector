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

import org.eclipse.chemclipse.chromatogram.msd.peak.detector.core.PeakDetectorMSD;
import org.eclipse.chemclipse.chromatogram.msd.peak.detector.settings.IPeakDetectorCSDSettings;
import org.eclipse.chemclipse.chromatogram.peak.detector.exceptions.NoPeakDetectorAvailableException;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ProccesingPeakDetectorMSD extends AbstractChromatogramSelectionProcessing<IPeakDetectorCSDSettings, IChromatogramSelectionMSD> {

	/**
	 *
	 */
	private static final long serialVersionUID = 3849712472645916738L;

	protected ProccesingPeakDetectorMSD() {
		super();
	}

	public ProccesingPeakDetectorMSD(String id) {
		super(id);
	}

	public ProccesingPeakDetectorMSD(String id, IPeakDetectorCSDSettings settings) throws JsonProcessingException {
		super(id, settings);
	}

	@Override
	protected Class<? extends IPeakDetectorCSDSettings> getSettingsClass(String id) throws Exception {

		return PeakDetectorMSD.getPeakDetectorSupport().getPeakDetectorSupplier(id).getPeakDetectorSettingsClass();
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionMSD chromatogramSelection, String id, IPeakDetectorCSDSettings settings, IProgressMonitor monitor) throws Exception {

		return PeakDetectorMSD.detect(chromatogramSelection, settings, id, monitor);
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionMSD chromatogramSelection, String id, IProgressMonitor monitor) throws Exception {

		return PeakDetectorMSD.detect(chromatogramSelection, id, monitor);
	}

	@Override
	public String getName() {

		try {
			return PeakDetectorMSD.getPeakDetectorSupport().getPeakDetectorSupplier(getId()).getPeakDetectorName();
		} catch(NoPeakDetectorAvailableException e) {
		}
		return null;
	}

	@Override
	public String getDescription() {

		try {
			return PeakDetectorMSD.getPeakDetectorSupport().getPeakDetectorSupplier(getId()).getDescription();
		} catch(NoPeakDetectorAvailableException e) {
		}
		return null;
	}
}
