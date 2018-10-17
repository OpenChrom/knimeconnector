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

import org.eclipse.chemclipse.chromatogram.csd.peak.detector.core.IPeakDetectorCSDSupplier;
import org.eclipse.chemclipse.chromatogram.csd.peak.detector.core.PeakDetectorCSD;
import org.eclipse.chemclipse.chromatogram.csd.peak.detector.settings.IPeakDetectorCSDSettings;
import org.eclipse.chemclipse.chromatogram.msd.peak.detector.core.IPeakDetectorMSDSupplier;
import org.eclipse.chemclipse.chromatogram.msd.peak.detector.core.PeakDetectorMSD;
import org.eclipse.chemclipse.chromatogram.msd.peak.detector.settings.IPeakDetectorMSDSettings;
import org.eclipse.chemclipse.chromatogram.peak.detector.exceptions.NoPeakDetectorAvailableException;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;
import net.openchrom.process.supplier.knime.model.IDataProcessing;
import net.openchrom.xxd.process.supplier.knime.model.ProccesingPeakDetectorCSD;
import net.openchrom.xxd.process.supplier.knime.model.ProccesingPeakDetectorMSD;

public class PeakDetektorsSupport {

	private static boolean containsPeakDetectorMSD(String id) {

		try {
			return PeakDetectorMSD.getPeakDetectorSupport().getAvailablePeakDetectorIds().contains(id);
		} catch(NoPeakDetectorAvailableException e) {
			return false;
		}
	}

	private static boolean containsPeakDetectorCSD(String id) {

		try {
			return PeakDetectorCSD.getPeakDetectorSupport().getAvailablePeakDetectorIds().contains(id);
		} catch(NoPeakDetectorAvailableException e) {
			return false;
		}
	}

	public static IProcessingInfo detectMSD(IChromatogramSelectionMSD chromatogramSelection, IPeakDetectorMSDSettings peakDetectorSettings, String peakDetectorId, IProgressMonitor monitor) throws NoPeakDetectorAvailableException {

		if(containsPeakDetectorMSD(peakDetectorId)) {
			return PeakDetectorMSD.detect(chromatogramSelection, peakDetectorSettings, peakDetectorId, monitor);
		}
		throw new NoPeakDetectorAvailableException();
	}

	public static IProcessingInfo detectCSD(IChromatogramSelectionCSD chromatogramSelection, IPeakDetectorCSDSettings peakDetectorSettings, String peakDetectorId, IProgressMonitor monitor) throws NoPeakDetectorAvailableException {

		if(containsPeakDetectorCSD(peakDetectorId)) {
			return PeakDetectorCSD.detect(chromatogramSelection, peakDetectorSettings, peakDetectorId, monitor);
		}
		throw new NoPeakDetectorAvailableException();
	}

	public static List<String> getIDsPeakDectorsMSD() throws NoPeakDetectorAvailableException {

		return PeakDetectorMSD.getPeakDetectorSupport().getAvailablePeakDetectorIds();
	}

	public static List<String> getIDsPeakDectorsCSD() throws NoPeakDetectorAvailableException {

		return PeakDetectorCSD.getPeakDetectorSupport().getAvailablePeakDetectorIds();
	}

	public static IDataProcessing<IChromatogramSelectionMSD> getProcessingPeakDetectorMSD(String id) throws NoPeakDetectorAvailableException {

		if(containsPeakDetectorMSD(id)) {
			return new ProccesingPeakDetectorMSD(id);
		}
		throw new NoPeakDetectorAvailableException();
	}

	public static IDataProcessing<IChromatogramSelectionCSD> getProcessingPeakDetectorCSD(String id) throws NoPeakDetectorAvailableException {

		if(containsPeakDetectorMSD(id)) {
			return new ProccesingPeakDetectorCSD(id);
		}
		throw new NoPeakDetectorAvailableException();
	}

	public static IDataProcessing<IChromatogramSelectionMSD> getProcessingPeakDetectorMSD(String id, PropertyProvider prov) throws Exception {

		if(containsPeakDetectorMSD(id)) {
			return new ProccesingPeakDetectorMSD(id, prov);
		}
		throw new NoPeakDetectorAvailableException();
	}

	public static IDataProcessing<IChromatogramSelectionCSD> getProcessingPeakDetectorCSD(String id, PropertyProvider prov) throws Exception {

		if(containsPeakDetectorMSD(id)) {
			return new ProccesingPeakDetectorCSD(id, prov);
		}
		throw new NoPeakDetectorAvailableException();
	}

	public static IPeakDetectorMSDSupplier getSupplierMSD(String id) throws NoPeakDetectorAvailableException {

		if(containsPeakDetectorMSD(id)) {
			return PeakDetectorMSD.getPeakDetectorSupport().getPeakDetectorSupplier(id);
		}
		throw new NoPeakDetectorAvailableException();
	}

	public static IPeakDetectorCSDSupplier getSupplierCSD(String id) throws NoPeakDetectorAvailableException {

		if(containsPeakDetectorCSD(id)) {
			return PeakDetectorCSD.getPeakDetectorSupport().getPeakDetectorSupplier(id);
		}
		throw new NoPeakDetectorAvailableException();
	}
}
