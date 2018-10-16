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

import org.eclipse.chemclipse.chromatogram.csd.peak.detector.core.PeakDetectorCSD;
import org.eclipse.chemclipse.chromatogram.csd.peak.detector.settings.IPeakDetectorCSDSettings;
import org.eclipse.chemclipse.chromatogram.peak.detector.exceptions.NoPeakDetectorAvailableException;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import net.openchrom.process.supplier.knime.dialogfactory.JacksonSettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.SettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;

public class ProccesingPeakDetectorCSD extends AbstractChromatogramSelectionProcessing<IPeakDetectorCSDSettings, IChromatogramSelectionCSD> {

	/**
	 *
	 */
	private static final long serialVersionUID = 3880295500567259045L;
	private transient JacksonSettingObjectSupplier<? extends IPeakDetectorCSDSettings> jacksonSettingObjectSupplier;

	protected ProccesingPeakDetectorCSD() {

		super();
		jacksonSettingObjectSupplier = new JacksonSettingObjectSupplier<>();
	}

	public ProccesingPeakDetectorCSD(String id) {

		super(id);
	}

	public ProccesingPeakDetectorCSD(String id, PropertyProvider prov) throws Exception {

		super(id, prov);
	}

	@Override
	protected Class<? extends IPeakDetectorCSDSettings> getSettingsClass(String id) throws NoPeakDetectorAvailableException {

		return PeakDetectorCSD.getPeakDetectorSupport().getPeakDetectorSupplier(id).getPeakDetectorSettingsClass();
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionCSD chromatogramSelection, String id, IPeakDetectorCSDSettings settings, IProgressMonitor monitor) throws Exception {

		return PeakDetectorCSD.detect(chromatogramSelection, settings, id, monitor);
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionCSD chromatogramSelection, String id, IProgressMonitor monitor) throws Exception {

		return PeakDetectorCSD.detect(chromatogramSelection, id, monitor);
	}

	@Override
	public String getName() {

		try {
			return PeakDetectorCSD.getPeakDetectorSupport().getPeakDetectorSupplier(getId()).getPeakDetectorName();
		} catch(NoPeakDetectorAvailableException e) {
		}
		return null;
	}

	@Override
	public String getDescription() {

		try {
			return PeakDetectorCSD.getPeakDetectorSupport().getPeakDetectorSupplier(getId()).getDescription();
		} catch(NoPeakDetectorAvailableException e) {
		}
		return null;
	}

	@Override
	protected SettingObjectSupplier<? extends IPeakDetectorCSDSettings> getSettingsClassSupplier() {

		if(jacksonSettingObjectSupplier == null) {
			jacksonSettingObjectSupplier = new JacksonSettingObjectSupplier<>();
		}
		return jacksonSettingObjectSupplier;
	}
}
