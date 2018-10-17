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

import org.eclipse.chemclipse.chromatogram.csd.identifier.peak.IPeakIdentifierSupplierCSD;
import org.eclipse.chemclipse.chromatogram.csd.identifier.peak.PeakIdentifierCSD;
import org.eclipse.chemclipse.chromatogram.csd.identifier.settings.IPeakIdentifierSettingsCSD;
import org.eclipse.chemclipse.chromatogram.msd.identifier.peak.IPeakIdentifierSupplierMSD;
import org.eclipse.chemclipse.chromatogram.msd.identifier.peak.PeakIdentifierMSD;
import org.eclipse.chemclipse.chromatogram.msd.identifier.settings.IPeakIdentifierSettingsMSD;
import org.eclipse.chemclipse.csd.model.core.IPeakCSD;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.model.exceptions.NoIdentifierAvailableException;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;
import net.openchrom.process.supplier.knime.model.IDataProcessing;
import net.openchrom.xxd.process.supplier.knime.model.ProcessingPeakIdentifierCSD;
import net.openchrom.xxd.process.supplier.knime.model.ProcessingPeakIdentifierMSD;

public class IdentifierSupport {

	private static boolean containsIdentifierMSDId(String id) {

		try {
			return PeakIdentifierMSD.getPeakIdentifierSupport().getAvailableIdentifierIds().contains(id);
		} catch(NoIdentifierAvailableException e) {
			return false;
		}
	}

	public static List<String> getIDsPeakIdentifierMSD() throws NoIdentifierAvailableException {

		return PeakIdentifierMSD.getPeakIdentifierSupport().getAvailableIdentifierIds();
	}

	public static IDataProcessing<? super IChromatogramSelectionMSD> getProceessingIdentifierMSD(String id, PropertyProvider prov) throws Exception {

		if(containsIdentifierMSDId(id)) {
			try {
				return new ProcessingPeakIdentifierMSD(id, prov);
			} catch(JsonProcessingException e) {
				throw new NoIdentifierAvailableException();
			}
		} else {
			throw new NoIdentifierAvailableException();
		}
	}

	public static IPeakIdentifierSupplierMSD getSupplierMSD(String identifierId) throws NoIdentifierAvailableException {

		return PeakIdentifierMSD.getPeakIdentifierSupport().getIdentifierSupplier(identifierId);
	}

	public static IProcessingInfo identifyMSD(List<IPeakMSD> peaks, IPeakIdentifierSettingsMSD identifierSettings, String identifierId, IProgressMonitor monitor) throws NoIdentifierAvailableException {

		if(containsIdentifierMSDId(identifierId)) {
			return PeakIdentifierMSD.identify(peaks, identifierSettings, identifierId, monitor);
		} else {
			throw new NoIdentifierAvailableException();
		}
	}

	private static boolean containsIdentifierCSDId(String id) {

		try {
			return PeakIdentifierCSD.getPeakIdentifierSupport().getAvailableIdentifierIds().contains(id);
		} catch(NoIdentifierAvailableException e) {
			return false;
		}
	}

	public static List<String> getIDsPeakIdentifierCSD() throws NoIdentifierAvailableException {

		return PeakIdentifierCSD.getPeakIdentifierSupport().getAvailableIdentifierIds();
	}

	public static IDataProcessing<? super IChromatogramSelectionCSD> getProceessingIdentifierCSD(String id, PropertyProvider prov) throws NoIdentifierAvailableException {

		if(containsIdentifierCSDId(id)) {
			try {
				return new ProcessingPeakIdentifierCSD(id, prov);
			} catch(Exception e) {
				throw new NoIdentifierAvailableException();
			}
		} else {
			throw new NoIdentifierAvailableException();
		}
	}

	public static IPeakIdentifierSupplierCSD getSupplierCSD(String identifierId) throws NoIdentifierAvailableException {

		return PeakIdentifierCSD.getPeakIdentifierSupport().getIdentifierSupplier(identifierId);
	}

	public static IProcessingInfo identifyCSD(List<IPeakCSD> peaks, IPeakIdentifierSettingsCSD identifierSettings, String identifierId, IProgressMonitor monitor) throws NoIdentifierAvailableException {

		if(containsIdentifierMSDId(identifierId)) {
			return PeakIdentifierCSD.identify(peaks, identifierSettings, identifierId, monitor);
		} else {
			throw new NoIdentifierAvailableException();
		}
	}
}
