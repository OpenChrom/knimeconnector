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

import org.eclipse.chemclipse.chromatogram.csd.identifier.peak.PeakIdentifierCSD;
import org.eclipse.chemclipse.chromatogram.csd.identifier.settings.IPeakIdentifierSettingsCSD;
import org.eclipse.chemclipse.csd.model.core.IChromatogramPeakCSD;
import org.eclipse.chemclipse.csd.model.core.IPeakCSD;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.model.exceptions.NoIdentifierAvailableException;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import net.openchrom.process.supplier.knime.dialogfactory.JacksonSettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.SettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;
import net.openchrom.process.supplier.knime.model.AbstractDataProcessing;
import net.openchrom.xxd.process.supplier.knime.model.settingssupplier.IdentifierSettingsObjectSupplier;

public class ProcessingPeakIdentifierCSD extends AbstractDataProcessing<IPeakIdentifierSettingsCSD, IChromatogramSelectionCSD> {

	private static final int INTERNAL_VERSION_ID = 1;
	private transient SettingObjectSupplier<? extends IPeakIdentifierSettingsCSD> settingsClassSupplier = new JacksonSettingObjectSupplier<>();

	public ProcessingPeakIdentifierCSD() {

		super();
	}

	public ProcessingPeakIdentifierCSD(String id) {

		super(id);
	}

	public ProcessingPeakIdentifierCSD(String id, PropertyProvider prov) throws Exception {

		super(id, prov);
	}

	@Override
	protected Class<? extends IPeakIdentifierSettingsCSD> getSettingsClass(String id) throws Exception {

		return PeakIdentifierCSD.getPeakIdentifierSupport().getIdentifierSupplier(id).getSettingsClass();
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionCSD chromatogramSelection, String id, IPeakIdentifierSettingsCSD settings, IProgressMonitor monitor) throws Exception {

		List<IChromatogramPeakCSD> peaks = chromatogramSelection.getChromatogramCSD().getPeaks(chromatogramSelection);
		List<IPeakCSD> peakList = new ArrayList<IPeakCSD>();
		for(IChromatogramPeakCSD chromatogramPeak : peaks) {
			peakList.add(chromatogramPeak);
		}
		return PeakIdentifierCSD.identify(peakList, settings, id, monitor);
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelectionCSD chromatogramSelection, String id, IProgressMonitor monitor) throws Exception {

		List<IChromatogramPeakCSD> peaks = chromatogramSelection.getChromatogramCSD().getPeaks(chromatogramSelection);
		List<IPeakCSD> peakList = new ArrayList<IPeakCSD>();
		for(IChromatogramPeakCSD chromatogramPeak : peaks) {
			peakList.add(chromatogramPeak);
		}
		return PeakIdentifierCSD.identify(peakList, id, monitor);
	}

	@Override
	public String getName() {

		try {
			return PeakIdentifierCSD.getPeakIdentifierSupport().getIdentifierSupplier(getId()).getIdentifierName();
		} catch(NoIdentifierAvailableException e) {
			// TODO:
		}
		return null;
	}

	@Override
	public String getDescription() {

		try {
			return PeakIdentifierCSD.getPeakIdentifierSupport().getIdentifierSupplier(getId()).getDescription();
		} catch(NoIdentifierAvailableException e) {
			// TODO:
		}
		return null;
	}

	@Override
	protected SettingObjectSupplier<? extends IPeakIdentifierSettingsCSD> getSettingsClassSupplier() {

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
