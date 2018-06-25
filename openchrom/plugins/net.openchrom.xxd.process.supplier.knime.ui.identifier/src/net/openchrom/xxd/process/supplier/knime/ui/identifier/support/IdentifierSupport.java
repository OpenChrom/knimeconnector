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
package net.openchrom.xxd.process.supplier.knime.ui.identifier.support;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.identifier.exceptions.NoIdentifierAvailableException;
import org.eclipse.chemclipse.chromatogram.msd.identifier.peak.IPeakIdentifierSupplier;
import org.eclipse.chemclipse.chromatogram.msd.identifier.peak.PeakIdentifier;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import net.openchrom.xxd.process.supplier.knime.model.IChromatogramSelectionProcessing;
import net.openchrom.xxd.process.supplier.knime.model.ProcessingPeakIdentifierMSD;

public class IdentifierSupport {

	private static boolean containsIdentifierMSDId(String id) {

		try {
			return PeakIdentifier.getPeakIdentifierSupport().getAvailableIdentifierIds().contains(id);
		} catch(NoIdentifierAvailableException e) {
			return false;
		}
	}

	public static List<String> getIDsPeakIdentifierMSD() throws NoIdentifierAvailableException {

		return PeakIdentifier.getPeakIdentifierSupport().getAvailableIdentifierIds();
	}

	public static IChromatogramSelectionProcessing<? super IChromatogramSelectionMSD> getProceessingIdentifierMSD(String id) throws NoIdentifierAvailableException {

		if(containsIdentifierMSDId(id)) {
			return new ProcessingPeakIdentifierMSD(id);
		} else {
			throw new NoIdentifierAvailableException();
		}
	}

	public static IPeakIdentifierSupplier getSupplierMSD(String identifierId) throws NoIdentifierAvailableException {

		return PeakIdentifier.getPeakIdentifierSupport().getIdentifierSupplier(identifierId);
	}

	public static IProcessingInfo identifyMSD(List<IPeakMSD> peaks, String identifierId, IProgressMonitor monitor) throws NoIdentifierAvailableException {

		if(containsIdentifierMSDId(identifierId)) {
			return PeakIdentifier.identify(peaks, identifierId, monitor);
		} else {
			throw new NoIdentifierAvailableException();
		}
	}
}
