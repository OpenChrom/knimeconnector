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
package net.openchrom.xxd.process.supplier.knime.ui.peakdetectors.support;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.peak.detector.core.IPeakDetectorMSDSupplier;
import org.eclipse.chemclipse.chromatogram.msd.peak.detector.core.PeakDetectorMSD;
import org.eclipse.chemclipse.chromatogram.msd.peak.detector.processing.IPeakDetectorMSDProcessingInfo;
import org.eclipse.chemclipse.chromatogram.msd.peak.detector.settings.IPeakDetectorMSDSettings;
import org.eclipse.chemclipse.chromatogram.peak.detector.exceptions.NoPeakDetectorAvailableException;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.core.runtime.IProgressMonitor;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.openchrom.xxd.process.supplier.knime.model.IChromatogramSelectionProcessing;
import net.openchrom.xxd.process.supplier.knime.model.ProccesingPeakDetectorMSD;

public class PeakDetektorsSupport {

	private static boolean containsPeakDetectorMSD(String id) {

		try {
			return PeakDetectorMSD.getPeakDetectorSupport().getAvailablePeakDetectorIds().contains(id);
		} catch(NoPeakDetectorAvailableException e) {
			return false;
		}
	}

	public static IPeakDetectorMSDProcessingInfo detectMSD(IChromatogramSelectionMSD chromatogramSelection, IPeakDetectorMSDSettings peakDetectorSettings, String peakDetectorId, IProgressMonitor monitor) throws NoPeakDetectorAvailableException {

		if(containsPeakDetectorMSD(peakDetectorId)) {
			return PeakDetectorMSD.detect(chromatogramSelection, peakDetectorSettings, peakDetectorId, monitor);
		}
		throw new NoPeakDetectorAvailableException();
	}

	public static List<String> getIDsPeakDectorsMSD() throws NoPeakDetectorAvailableException {

		return PeakDetectorMSD.getPeakDetectorSupport().getAvailablePeakDetectorIds();
	}

	public static IChromatogramSelectionProcessing<IChromatogramSelectionMSD> getProcessingPeakDetector(String id) throws NoPeakDetectorAvailableException {

		if(containsPeakDetectorMSD(id)) {
			return new ProccesingPeakDetectorMSD(id);
		}
		throw new NoPeakDetectorAvailableException();
	}

	public static IChromatogramSelectionProcessing<IChromatogramSelectionMSD> getProcessingPeakDetector(String id, IPeakDetectorMSDSettings detectorMSDSettings) throws NoPeakDetectorAvailableException, JsonProcessingException {

		if(containsPeakDetectorMSD(id)) {
			return new ProccesingPeakDetectorMSD(id, detectorMSDSettings);
		}
		throw new NoPeakDetectorAvailableException();
	}

	public static IPeakDetectorMSDSupplier getSupplier(String id) throws NoPeakDetectorAvailableException {

		if(containsPeakDetectorMSD(id)) {
			return PeakDetectorMSD.getPeakDetectorSupport().getPeakDetectorSupplier(id);
		}
		throw new NoPeakDetectorAvailableException();
	}
}
