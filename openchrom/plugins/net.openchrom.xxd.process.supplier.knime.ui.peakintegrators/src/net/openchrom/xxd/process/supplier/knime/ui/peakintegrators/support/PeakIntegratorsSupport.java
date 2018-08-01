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
package net.openchrom.xxd.process.supplier.knime.ui.peakintegrators.support;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.peaks.IPeakIntegratorSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.peaks.PeakIntegrator;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.IPeakIntegrationSettings;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.exceptions.NoIntegratorAvailableException;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.openchrom.xxd.process.supplier.knime.model.IChromatogramSelectionProcessing;
import net.openchrom.xxd.process.supplier.knime.model.ProcessingPeakIntegrator;

public class PeakIntegratorsSupport {

	private static boolean conteinsIntegrators(String id) {

		try {
			return PeakIntegrator.getPeakIntegratorSupport().getAvailableIntegratorIds().contains(id);
		} catch(NoIntegratorAvailableException e) {
			return false;
		}
	}

	public static List<String> getIdsPeakIntegratorsMSD() throws NoIntegratorAvailableException {

		return PeakIntegrator.getPeakIntegratorSupport().getAvailableIntegratorIds();
	}

	public static IChromatogramSelectionProcessing<? super IChromatogramSelectionMSD> getProcessingPeakIntegratoMSD(String id, IPeakIntegrationSettings detectorMSDSettings) throws NoIntegratorAvailableException, JsonProcessingException {

		if(conteinsIntegrators(id)) {
			return new ProcessingPeakIntegrator(id, detectorMSDSettings);
		}
		throw new NoIntegratorAvailableException();
	}

	public static IPeakIntegratorSupplier getSupplierMSD(String id) throws NoIntegratorAvailableException {

		if(conteinsIntegrators(id)) {
			return PeakIntegrator.getPeakIntegratorSupport().getIntegratorSupplier(id);
		}
		throw new NoIntegratorAvailableException();
	}

	public static IProcessingInfo integrateMSD(IChromatogramSelection chromatogramSelection, IPeakIntegrationSettings peakIntegrationSettings, String integratorId, IProgressMonitor monitor) throws NoIntegratorAvailableException {

		if(conteinsIntegrators(integratorId)) {
			return PeakIntegrator.integrate(chromatogramSelection, peakIntegrationSettings, integratorId, monitor);
		}
		throw new NoIntegratorAvailableException();
	}

	public static List<String> getIdsPeakIntegratorsCSD() throws NoIntegratorAvailableException {

		return PeakIntegrator.getPeakIntegratorSupport().getAvailableIntegratorIds();
	}

	public static IChromatogramSelectionProcessing<? super IChromatogramSelectionCSD> getProcessingPeakIntegratoCSD(String id, IPeakIntegrationSettings detectorMSDSettings) throws NoIntegratorAvailableException, JsonProcessingException {

		if(conteinsIntegrators(id)) {
			return new ProcessingPeakIntegrator(id, detectorMSDSettings);
		}
		throw new NoIntegratorAvailableException();
	}

	public static IPeakIntegratorSupplier getSupplierCSD(String id) throws NoIntegratorAvailableException {

		if(conteinsIntegrators(id)) {
			return PeakIntegrator.getPeakIntegratorSupport().getIntegratorSupplier(id);
		}
		throw new NoIntegratorAvailableException();
	}

	public static IProcessingInfo integrateCSD(IChromatogramSelection chromatogramSelection, IPeakIntegrationSettings peakIntegrationSettings, String integratorId, IProgressMonitor monitor) throws NoIntegratorAvailableException {

		if(conteinsIntegrators(integratorId)) {
			return PeakIntegrator.integrate(chromatogramSelection, peakIntegrationSettings, integratorId, monitor);
		}
		throw new NoIntegratorAvailableException();
	}
}
