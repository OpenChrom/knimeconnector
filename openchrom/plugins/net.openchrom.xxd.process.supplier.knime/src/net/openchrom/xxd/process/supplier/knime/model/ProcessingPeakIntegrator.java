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
import java.io.ObjectInputStream;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.peaks.PeakIntegrator;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.IPeakIntegrationSettings;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.exceptions.NoIntegratorAvailableException;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import net.openchrom.process.supplier.knime.dialogfactory.SettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;
import net.openchrom.process.supplier.knime.model.AbstractDataProcessing;
import net.openchrom.xxd.process.supplier.knime.model.settingssupplier.IntegratorSettingsObjectSupplier;

public class ProcessingPeakIntegrator extends AbstractDataProcessing<IPeakIntegrationSettings, IChromatogramSelection> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6755275500252238458L;
	private transient SettingObjectSupplier<? extends IPeakIntegrationSettings> settingsClassSupplier = new IntegratorSettingsObjectSupplier<>();

	protected ProcessingPeakIntegrator() {

		super();
	}

	public ProcessingPeakIntegrator(String id) {

		super(id);
	}

	public ProcessingPeakIntegrator(String id, PropertyProvider prov) throws Exception {

		super(id, prov);
	}

	@Override
	protected Class<? extends IPeakIntegrationSettings> getSettingsClass(String id) throws Exception {

		return PeakIntegrator.getPeakIntegratorSupport().getIntegratorSupplier(id).getSettingsClass();
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelection chromatogramSelection, String id, IPeakIntegrationSettings settings, IProgressMonitor monitor) throws Exception {

		return PeakIntegrator.integrate(chromatogramSelection, settings, id, monitor);
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelection chromatogramSelection, String id, IProgressMonitor monitor) throws Exception {

		return PeakIntegrator.integrate(chromatogramSelection, id, monitor);
	}

	@Override
	public String getName() {

		try {
			return PeakIntegrator.getPeakIntegratorSupport().getIntegratorSupplier(getId()).getIntegratorName();
		} catch(NoIntegratorAvailableException e) {
			// TODO:
		}
		return null;
	}

	@Override
	public String getDescription() {

		try {
			return PeakIntegrator.getPeakIntegratorSupport().getIntegratorSupplier(getId()).getDescription();
		} catch(NoIntegratorAvailableException e) {
			// TODO:
		}
		return null;
	}

	@Override
	protected SettingObjectSupplier<? extends IPeakIntegrationSettings> getSettingsClassSupplier() {

		return settingsClassSupplier;
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {

		in.defaultReadObject();
		settingsClassSupplier = new IntegratorSettingsObjectSupplier<>();
	}
}
