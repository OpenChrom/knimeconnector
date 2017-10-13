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
import org.eclipse.chemclipse.chromatogram.csd.peak.detector.settings.IPeakDetectorCSDSettings;
import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.ChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.IChromatogramFilterSupplier;
import org.eclipse.chemclipse.chromatogram.filter.exceptions.NoChromatogramFilterSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.filter.processing.IChromatogramFilterProcessingInfo;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.chromatogram.msd.filter.core.chromatogram.ChromatogramFilterMSD;
import org.eclipse.chemclipse.chromatogram.msd.identifier.core.ISupplier;
import org.eclipse.chemclipse.chromatogram.msd.identifier.exceptions.NoIdentifierAvailableException;
import org.eclipse.chemclipse.chromatogram.msd.identifier.peak.PeakIdentifier;
import org.eclipse.chemclipse.chromatogram.msd.peak.detector.core.PeakDetectorMSD;
import org.eclipse.chemclipse.chromatogram.msd.peak.detector.settings.IPeakDetectorMSDSettings;
import org.eclipse.chemclipse.chromatogram.peak.detector.core.IPeakDetectorSupplier;
import org.eclipse.chemclipse.chromatogram.peak.detector.exceptions.NoPeakDetectorAvailableException;
import org.eclipse.chemclipse.chromatogram.peak.detector.settings.IPeakDetectorSettings;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.peaks.IPeakIntegratorSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.peaks.PeakIntegrator;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.IPeakIntegrationSettings;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.exceptions.NoIntegratorAvailableException;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.processing.IPeakIntegratorProcessingInfo;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramSelectionProcessing {

	public static IChromatogramFilterProcessingInfo applyFilter(IChromatogramSelection chromatogramSelection, IChromatogramFilterSettings setting, String id, IProgressMonitor monitor) throws NoChromatogramFilterSupplierAvailableException {

		if(isFilter(id)) {
			return ChromatogramFilter.applyFilter(chromatogramSelection, setting, id, monitor);
		} else if(isFilterMSD(id)) {
			return ChromatogramFilterMSD.applyFilter((IChromatogramSelectionMSD)chromatogramSelection, setting, id, monitor);
		} else if(isFilterCSD(id)) {
			return ChromatogramFilterCSD.applyFilter((IChromatogramSelectionCSD)chromatogramSelection, setting, id, monitor);
		}
		throw new NoChromatogramFilterSupplierAvailableException();
	}

	public static IProcessingInfo detectPeaks(IChromatogramSelection chromatogramSelection, IPeakDetectorSettings settings, String id, IProgressMonitor monitor) throws NoPeakDetectorAvailableException {

		if(isPeakDetectorCSD(id)) {
			return PeakDetectorCSD.detect((IChromatogramSelectionCSD)chromatogramSelection, (IPeakDetectorCSDSettings)settings, id, monitor);
		} else if(isPeakDetectorMSD(id)) {
			return PeakDetectorMSD.detect((IChromatogramSelectionMSD)chromatogramSelection, (IPeakDetectorMSDSettings)settings, id, monitor);
		} else {
			throw new NoPeakDetectorAvailableException();
		}
	}

	public static IChromatogramFilterSupplier getFilterSupplier(String id) throws NoChromatogramFilterSupplierAvailableException {

		if(isFilter(id)) {
			return ChromatogramFilter.getChromatogramFilterSupport().getFilterSupplier(id);
		} else if(isFilterMSD(id)) {
			return ChromatogramFilterMSD.getChromatogramFilterSupport().getFilterSupplier(id);
		} else if(isFilterCSD(id)) {
			return ChromatogramFilterCSD.getChromatogramFilterSupport().getFilterSupplier(id);
		}
		throw new NoChromatogramFilterSupplierAvailableException();
	}

	public static IPeakDetectorSupplier getPeakDetectorSupplier(String id) throws NoPeakDetectorAvailableException {

		if(isPeakDetectorCSD(id)) {
			return PeakDetectorCSD.getPeakDetectorSupport().getPeakDetectorSupplier(id);
		} else if(isPeakDetectorMSD(id)) {
			return PeakDetectorMSD.getPeakDetectorSupport().getPeakDetectorSupplier(id);
		} else {
			throw new NoPeakDetectorAvailableException();
		}
	}

	public static ISupplier getPeakIdentifierSupplier(String id) throws NoIdentifierAvailableException {

		if(isPeakIdentifierMSD(id)) {
			return PeakIdentifier.getPeakIdentifierSupport().getIdentifierSupplier(id);
		} else {
			throw new NoIdentifierAvailableException();
		}
	}

	public static IPeakIntegratorSupplier getPeakIntegratorSupplier(String id) throws NoIntegratorAvailableException {

		if(isPeakIntegrator(id)) {
			return PeakIntegrator.getPeakIntegratorSupport().getIntegratorSupplier(id);
		} else {
			throw new NoIntegratorAvailableException();
		}
	}

	public static IPeakIntegratorProcessingInfo integrate(IChromatogramSelection chromatogramSelection, IPeakIntegrationSettings settings, String id, IProgressMonitor monitor) throws NoIntegratorAvailableException {

		if(isPeakIntegrator(id)) {
			return PeakIntegrator.integrate(chromatogramSelection, settings, id, monitor);
		} else {
			throw new NoIntegratorAvailableException();
		}
	}

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
