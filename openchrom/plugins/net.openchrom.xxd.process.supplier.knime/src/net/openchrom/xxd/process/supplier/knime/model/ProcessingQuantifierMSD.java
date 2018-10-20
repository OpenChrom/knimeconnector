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
package net.openchrom.xxd.process.supplier.knime.model;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.quantitation.core.PeakQuantifier;
import org.eclipse.chemclipse.chromatogram.msd.quantitation.exceptions.NoPeakQuantifierAvailableException;
import org.eclipse.chemclipse.chromatogram.msd.quantitation.settings.IPeakQuantifierSettings;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import net.openchrom.process.supplier.knime.dialogfactory.JacksonSettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.SettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;
import net.openchrom.process.supplier.knime.model.AbstractDataProcessing;
import net.openchrom.xxd.process.supplier.knime.model.settingssupplier.IdentifierSettingsObjectSupplier;

public class ProcessingQuantifierMSD extends AbstractDataProcessing<IPeakQuantifierSettings, IChromatogramSelectionMSD> {

	private static final int INTERNAL_VERSION_ID = 1;
	private transient SettingObjectSupplier<? extends IPeakQuantifierSettings> settingsClassSupplier = new IdentifierSettingsObjectSupplier<>();

	protected ProcessingQuantifierMSD() {

		super();
	}

	public ProcessingQuantifierMSD(String id, PropertyProvider prov) throws Exception {

		super(id, prov);
	}

	public ProcessingQuantifierMSD(String id) {

		super(id);
	}

	@Override
	public String getName() {

		try {
			return PeakQuantifier.getPeakQuantifierSupport().getPeakQuantifierSupplier(getId()).getPeakQuantifierName();
		} catch(NoPeakQuantifierAvailableException e) {
		}
		return null;
	}

	@Override
	public String getDescription() {

		try {
			return PeakQuantifier.getPeakQuantifierSupport().getPeakQuantifierSupplier(getId()).getDescription();
		} catch(NoPeakQuantifierAvailableException e) {
		}
		return null;
	}

	@Override
	protected Class<? extends IPeakQuantifierSettings> getSettingsClass(String id) throws Exception {

		try {
			return PeakQuantifier.getPeakQuantifierSupport().getPeakQuantifierSupplier(getId()).getQuantifierSettingsClass();
		} catch(NoPeakQuantifierAvailableException e) {
		}
		return null;
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionMSD chromatogramSelection, String id, IProgressMonitor monitor) throws Exception {

		List<IChromatogramPeakMSD> peaks = chromatogramSelection.getChromatogramMSD().getPeaks(chromatogramSelection);
		List<IPeak> peakList = new ArrayList<IPeak>();
		for(IChromatogramPeakMSD chromatogramPeak : peaks) {
			peakList.add(chromatogramPeak);
		}
		return PeakQuantifier.quantify(peakList, id, monitor);
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionMSD chromatogramSelection, String id, IPeakQuantifierSettings settings, IProgressMonitor monitor) throws Exception {

		List<IChromatogramPeakMSD> peaks = chromatogramSelection.getChromatogramMSD().getPeaks(chromatogramSelection);
		List<IPeak> peakList = new ArrayList<IPeak>();
		for(IChromatogramPeakMSD chromatogramPeak : peaks) {
			peakList.add(chromatogramPeak);
		}
		return PeakQuantifier.quantify(peakList, settings, id, monitor);
	}

	@Override
	protected SettingObjectSupplier<? extends IPeakQuantifierSettings> getSettingsClassSupplier() {

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
