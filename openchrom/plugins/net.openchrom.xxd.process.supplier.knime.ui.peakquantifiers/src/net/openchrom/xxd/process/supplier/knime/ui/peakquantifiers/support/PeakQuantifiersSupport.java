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
package net.openchrom.xxd.process.supplier.knime.ui.peakquantifiers.support;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.quantitation.core.IPeakQuantifierSupplier;
import org.eclipse.chemclipse.chromatogram.msd.quantitation.core.PeakQuantifier;
import org.eclipse.chemclipse.chromatogram.msd.quantitation.exceptions.NoPeakQuantifierAvailableException;
import org.eclipse.chemclipse.chromatogram.msd.quantitation.settings.IPeakQuantifierSettings;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.openchrom.xxd.process.supplier.knime.model.ProcessingQuantifierMSD;

public class PeakQuantifiersSupport {

	public static List<String> getIdsPeakQuantifiersMSD() throws NoPeakQuantifierAvailableException {

		return PeakQuantifier.getPeakQuantifierSupport().getAvailablePeakQuantifierIds();
	}

	public static IPeakQuantifierSupplier getSupplierMSD(String id) throws NoPeakQuantifierAvailableException {

		return PeakQuantifier.getPeakQuantifierSupport().getPeakQuantifierSupplier(id);
	}

	public static ProcessingQuantifierMSD getProcessingPeakIntegratoMSD(String id, IPeakQuantifierSettings settings) throws JsonProcessingException {

		return new ProcessingQuantifierMSD(id, settings);
	}
}
