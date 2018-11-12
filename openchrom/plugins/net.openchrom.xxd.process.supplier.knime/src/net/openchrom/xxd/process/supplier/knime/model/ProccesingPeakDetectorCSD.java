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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.eclipse.chemclipse.chromatogram.csd.peak.detector.core.PeakDetectorCSD;
import org.eclipse.chemclipse.chromatogram.csd.peak.detector.settings.IPeakDetectorSettingsCSD;
import org.eclipse.chemclipse.chromatogram.peak.detector.exceptions.NoPeakDetectorAvailableException;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import net.openchrom.process.supplier.knime.dialogfactory.JacksonSettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.SettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;
import net.openchrom.process.supplier.knime.model.AbstractDataProcessing;

public class ProccesingPeakDetectorCSD extends AbstractDataProcessing<IPeakDetectorSettingsCSD, IChromatogramSelectionCSD> {

	private static final int INTERNAL_VERSION_ID = 1;
	private transient SettingObjectSupplier<? extends IPeakDetectorSettingsCSD> settingsClassSupplier = new JacksonSettingObjectSupplier<>();

	public ProccesingPeakDetectorCSD() {
		super();
	}

	public ProccesingPeakDetectorCSD(String id) {
		super(id);
	}

	public ProccesingPeakDetectorCSD(String id, PropertyProvider prov) throws Exception {
		super(id, prov);
	}

	@Override
	protected Class<? extends IPeakDetectorSettingsCSD> getSettingsClass(String id) throws NoPeakDetectorAvailableException {

		return PeakDetectorCSD.getPeakDetectorSupport().getPeakDetectorSupplier(id).getSettingsClass();
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionCSD chromatogramSelection, String id, IPeakDetectorSettingsCSD settings, IProgressMonitor monitor) throws Exception {

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
	protected SettingObjectSupplier<? extends IPeakDetectorSettingsCSD> getSettingsClassSupplier() {

		return settingsClassSupplier;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

		super.readExternal(in);
		int version = in.read();
		switch(version) {
			case 1:
				settingsClassSupplier = new JacksonSettingObjectSupplier<>();
				break;
			default:
				break;
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {

		super.writeExternal(out);
		out.writeInt(INTERNAL_VERSION_ID);
	}
}
