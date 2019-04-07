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
package net.openchrom.xxd.process.supplier.knime.model.settingssupplier;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.IIntegrationSettings;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.IAreaSupport;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.IIntegrationSupport;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.IPeakIntegrationSettings;
import org.eclipse.chemclipse.support.util.IonSettingUtil;

import net.openchrom.process.supplier.knime.dialogfactory.JacksonSettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.SettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyCollector;
import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;

public class IntegratorSettingsObjectSupplier<SO extends IIntegrationSettings> implements SettingObjectSupplier<SO> {

	private final String SELECTED_ION_NAME = "Selected ions (default empty list 0 = TIC)";
	private final String MINIMUM_PEAK_WIDTH_NAME = "Minimum peak width (minutes)";
	private final String MINIMUM_SN_RATIO = "Minimum S/N ratio";
	private final String MINIMUM_PEAK_AREA_NAME = "Minimum peak area";
	private SettingObjectSupplier<SO> settingObjectSupplier;

	public IntegratorSettingsObjectSupplier() {

		settingObjectSupplier = new JacksonSettingObjectSupplier<SO>();
	}

	@Override
	public SO createSettingsObject(Class<? extends SO> settingsObjectClass, PropertyProvider prov) {

		SO settingsObject = settingObjectSupplier.createSettingsObject(settingsObjectClass, prov);
		if(IPeakIntegrationSettings.class.isAssignableFrom(settingsObjectClass)) {
			IPeakIntegrationSettings peakIntegrationSettings = (IPeakIntegrationSettings)settingsObject;
			String ions = prov.getStringProperty(SELECTED_ION_NAME);
			int minimumPeakWidth = prov.getIntProperty(MINIMUM_PEAK_WIDTH_NAME);
			int minimumSNRetio = prov.getIntProperty(MINIMUM_SN_RATIO);
			int minimumArea = prov.getIntProperty(MINIMUM_PEAK_AREA_NAME);
			IonSettingUtil ionSettingUtil = new IonSettingUtil();
			peakIntegrationSettings.getSelectedIons().add(ionSettingUtil.extractIons(ionSettingUtil.deserialize(ions)));
			// Integrator support
			IIntegrationSupport integratorSuppoert = peakIntegrationSettings.getIntegrationSupport();
			integratorSuppoert.setMinimumPeakWidth(minimumPeakWidth);
			integratorSuppoert.setMinimumSignalToNoiseRatio(minimumSNRetio);
			// Peak area support
			IAreaSupport areaSupport = peakIntegrationSettings.getAreaSupport();
			areaSupport.setMinimumArea(minimumArea);
		}
		return settingsObject;
	}

	@Override
	public void extractProperties(Class<? extends SO> settingsObjectClass, PropertyCollector coll) {

		if(IPeakIntegrationSettings.class.isAssignableFrom(settingsObjectClass)) {
			coll.addIonSelectionProperty(SELECTED_ION_NAME, SELECTED_ION_NAME, "", "");
			coll.addRetentionTimeMinutesProperty(MINIMUM_PEAK_WIDTH_NAME, MINIMUM_PEAK_WIDTH_NAME, 0, "", 6000, 0, Integer.MAX_VALUE);
			coll.addIntProperty(MINIMUM_SN_RATIO, MINIMUM_SN_RATIO, 0, "", 1, 0, Integer.MAX_VALUE);
			coll.addIntProperty(MINIMUM_PEAK_AREA_NAME, MINIMUM_PEAK_AREA_NAME, 0, "", 1, 0, Integer.MAX_VALUE);
		}
		settingObjectSupplier.extractProperties(settingsObjectClass, coll);
	}
}
