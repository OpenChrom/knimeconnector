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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.identifier.peak.PeakIdentifierMSD;
import org.eclipse.chemclipse.chromatogram.msd.identifier.settings.IPeakIdentifierSettingsMSD;
import org.eclipse.chemclipse.model.exceptions.NoIdentifierAvailableException;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import net.openchrom.process.supplier.knime.dialogfactory.JacksonSettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.SettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;
import net.openchrom.process.supplier.knime.model.AbstractDataProcessing;
import net.openchrom.xxd.process.supplier.knime.model.settingssupplier.IdentifierSettingsObjectSupplier;

public class ProcessingPeakIdentifierMSD extends AbstractDataProcessing<IPeakIdentifierSettingsMSD, IChromatogramSelectionMSD> {

	private static final int INTERNAL_VERSION_ID = 1;
	private transient SettingObjectSupplier<? extends IPeakIdentifierSettingsMSD> settingsClassSupplier = new JacksonSettingObjectSupplier<>();

	public ProcessingPeakIdentifierMSD() {

		super();
	}

	public ProcessingPeakIdentifierMSD(String id) {

		super(id);
	}

	public ProcessingPeakIdentifierMSD(String id, PropertyProvider prov) throws Exception {

		super(id, prov);
	}

	@Override
	protected Class<? extends IPeakIdentifierSettingsMSD> getSettingsClass(String id) throws Exception {

		return PeakIdentifierMSD.getPeakIdentifierSupport().getIdentifierSupplier(id).getIdentifierSettingsClass();
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionMSD chromatogramSelection, String id, IPeakIdentifierSettingsMSD settings, IProgressMonitor monitor) throws Exception {

		List<IChromatogramPeakMSD> peaks = chromatogramSelection.getChromatogramMSD().getPeaks(chromatogramSelection);
		List<IPeakMSD> peakList = new ArrayList<IPeakMSD>();
		for(IChromatogramPeakMSD chromatogramPeak : peaks) {
			peakList.add(chromatogramPeak);
		}
		return PeakIdentifierMSD.identify(peakList, settings, id, monitor);
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionMSD chromatogramSelection, String id, IProgressMonitor monitor) throws Exception {

		List<IChromatogramPeakMSD> peaks = chromatogramSelection.getChromatogramMSD().getPeaks(chromatogramSelection);
		List<IPeakMSD> peakList = new ArrayList<IPeakMSD>();
		for(IChromatogramPeakMSD chromatogramPeak : peaks) {
			peakList.add(chromatogramPeak);
		}
		return PeakIdentifierMSD.identify(peakList, id, monitor);
	}

	@Override
	public String getName() {

		try {
			return PeakIdentifierMSD.getPeakIdentifierSupport().getIdentifierSupplier(getId()).getIdentifierName();
		} catch(NoIdentifierAvailableException e) {
			// TODO:
		}
		return null;
	}

	@Override
	public String getDescription() {

		try {
			return PeakIdentifierMSD.getPeakIdentifierSupport().getIdentifierSupplier(getId()).getDescription();
		} catch(NoIdentifierAvailableException e) {
			// TODO:
		}
		return null;
	}

	@Override
	protected SettingObjectSupplier<? extends IPeakIdentifierSettingsMSD> getSettingsClassSupplier() {

		return settingsClassSupplier;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

		super.readExternal(in);
		int version = in.read();
		switch(version) {
			case 1:
				settingsClassSupplier = new IdentifierSettingsObjectSupplier<>();
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
