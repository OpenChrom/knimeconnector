/*******************************************************************************
 * Copyright (c) 2017 Jan Holy
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.support;

import org.eclipse.chemclipse.chromatogram.csd.filter.core.chromatogram.ChromatogramFilterCSD;
import org.eclipse.chemclipse.chromatogram.csd.peak.detector.core.PeakDetectorCSD;
import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.ChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.filter.exceptions.NoChromatogramFilterSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.msd.filter.core.chromatogram.ChromatogramFilterMSD;
import org.eclipse.chemclipse.chromatogram.msd.identifier.exceptions.NoIdentifierAvailableException;
import org.eclipse.chemclipse.chromatogram.msd.identifier.peak.PeakIdentifier;
import org.eclipse.chemclipse.chromatogram.msd.peak.detector.core.PeakDetectorMSD;
import org.eclipse.chemclipse.chromatogram.peak.detector.exceptions.NoPeakDetectorAvailableException;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.peaks.PeakIntegrator;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.exceptions.NoIntegratorAvailableException;

public class ChromatogramSelectionProcessing {

	private static boolean isFilter(String id) {

		try {
			return ChromatogramFilter.getChromatogramFilterSupport().getAvailableFilterIds().contains(id);
		} catch(NoChromatogramFilterSupplierAvailableException e) {
			return false;
		}
	}

	private static boolean isFilterCSD(String id) {

		try {
			return ChromatogramFilterCSD.getChromatogramFilterSupport().getAvailableFilterIds().contains(id);
		} catch(NoChromatogramFilterSupplierAvailableException e) {
			return false;
		}
	}

	private static boolean isFilterMSD(String id) {

		try {
			return ChromatogramFilterMSD.getChromatogramFilterSupport().getAvailableFilterIds().contains(id);
		} catch(NoChromatogramFilterSupplierAvailableException e) {
			return false;
		}
	}

	private static boolean isPeakDetectorCSD(String id) {

		try {
			return PeakDetectorCSD.getPeakDetectorSupport().getAvailablePeakDetectorIds().contains(id);
		} catch(NoPeakDetectorAvailableException e) {
			return false;
		}
	}

	private static boolean isPeakDetectorMSD(String id) {

		try {
			return PeakDetectorMSD.getPeakDetectorSupport().getAvailablePeakDetectorIds().contains(id);
		} catch(NoPeakDetectorAvailableException e) {
			return false;
		}
	}

	private static boolean isPeakIdentifierMSD(String id) {

		try {
			return PeakIdentifier.getPeakIdentifierSupport().getAvailableIdentifierIds().contains(id);
		} catch(NoIdentifierAvailableException e) {
			return false;
		}
	}

	private static boolean isPeakIntegrator(String id) {

		try {
			return PeakIntegrator.getPeakIntegratorSupport().getAvailableIntegratorIds().contains(id);
		} catch(NoIntegratorAvailableException e) {
			return false;
		}
	}
}
