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

import org.eclipse.chemclipse.chromatogram.msd.peak.detector.core.PeakDetectorMSD;
import org.eclipse.chemclipse.chromatogram.msd.peak.detector.settings.IPeakDetectorSettingsMSD;
import org.eclipse.chemclipse.chromatogram.peak.detector.exceptions.NoPeakDetectorAvailableException;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import net.openchrom.process.supplier.knime.dialogfactory.JacksonSettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.SettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;
import net.openchrom.process.supplier.knime.model.AbstractDataProcessing;

public class ProccesingPeakDetectorMSD extends AbstractDataProcessing<IPeakDetectorSettingsMSD, IChromatogramSelectionMSD> {

	private static final int INTERNAL_VERSION_ID = 1;
	private transient SettingObjectSupplier<? extends IPeakDetectorSettingsMSD> settingsClassSupplier = new JacksonSettingObjectSupplier<>();

	public ProccesingPeakDetectorMSD() {
		super();
	}

	public ProccesingPeakDetectorMSD(String id) {
		super(id);
	}

	public ProccesingPeakDetectorMSD(String id, PropertyProvider prov) throws Exception {
		super(id, prov);
	}

	@Override
	protected Class<? extends IPeakDetectorSettingsMSD> getSettingsClass(String id) throws Exception {

		return PeakDetectorMSD.getPeakDetectorSupport().getPeakDetectorSupplier(id).getSettingsClass();
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionMSD chromatogramSelection, String id, IPeakDetectorSettingsMSD settings, IProgressMonitor monitor) throws Exception {

		return PeakDetectorMSD.detect(chromatogramSelection, settings, id, monitor);
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionMSD chromatogramSelection, String id, IProgressMonitor monitor) throws Exception {

		return PeakDetectorMSD.detect(chromatogramSelection, id, monitor);
	}

	@Override
	public String getName() {

		try {
			return PeakDetectorMSD.getPeakDetectorSupport().getPeakDetectorSupplier(getId()).getPeakDetectorName();
		} catch(NoPeakDetectorAvailableException e) {
		}
		return null;
	}

	@Override
	public String getDescription() {

		try {
			return PeakDetectorMSD.getPeakDetectorSupport().getPeakDetectorSupplier(getId()).getDescription();
		} catch(NoPeakDetectorAvailableException e) {
		}
		return null;
	}

	@Override
	protected SettingObjectSupplier<? extends IPeakDetectorSettingsMSD> getSettingsClassSupplier() {

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
