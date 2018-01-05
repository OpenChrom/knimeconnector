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

import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.peaks.PeakIntegrator;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.IPeakIntegrationSettings;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ProcessingPeakIntegrator extends AbstractChromatogramSelectionProcessing<IPeakIntegrationSettings, IChromatogramSelection> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6755275500252238458L;

	protected ProcessingPeakIntegrator() {
		super();
	}

	public ProcessingPeakIntegrator(String id) {
		super(id);
	}

	public ProcessingPeakIntegrator(String id, IPeakIntegrationSettings settings) throws JsonProcessingException {
		super(id, settings);
	}

	@Override
	protected Class<? extends IPeakIntegrationSettings> getSettingsClass(String id) throws Exception {

		return PeakIntegrator.getPeakIntegratorSupport().getIntegratorSupplier(id).getPeakIntegrationSettingsClass();
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelection chromatogramSelection, String id, IPeakIntegrationSettings settings, IProgressMonitor monitor) throws Exception {

		return PeakIntegrator.integrate(chromatogramSelection, settings, id, monitor);
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelection chromatogramSelection, String id, IProgressMonitor monitor) throws Exception {

		return PeakIntegrator.integrate(chromatogramSelection, id, monitor);
	}
}
