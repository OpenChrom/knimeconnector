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
package net.openchrom.xxd.process.supplier.knime.ui.identifier.model;

import org.eclipse.chemclipse.chromatogram.msd.identifier.settings.GenereteIdentifierSettings;
import org.eclipse.chemclipse.chromatogram.msd.identifier.settings.IIdentifierSettings;
import org.knime.core.node.util.ButtonGroupEnumInterface;

import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.SettingObjectSupplier;
import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.property.JacksonSettingObjectSupplier;
import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.property.PropertyCollector;
import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.property.PropertyDialogFactory;
import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.property.PropertyProvider;

public class IdentifierPropertyDialogFactory<SO extends IIdentifierSettings> extends PropertyDialogFactory<SO> {

	private final String PENALITY_CALCULATION = "Force Match Quality Penalty calculation";
	private final String RETENTION_TIME_WINDOW = "Retention Time Window (minutes)";
	private final String RETENTION_INDEX_WINDOW = "Retention Index Window";
	private final String PENALITY_CALCULATION_LEVEL_FACTOR = "Penalty Calculation Level Factor";
	private final String MAX_PENALITY = "Max Penalty";
	//
	private SettingObjectSupplier<SO> settingObjectSupplier;

	public IdentifierPropertyDialogFactory() {
		settingObjectSupplier = new JacksonSettingObjectSupplier<SO>();
	}

	@Override
	public SO createSettingsObject(Class<? extends SO> settingsObjectClass, PropertyProvider prov) {

		SO settingsObject = settingObjectSupplier.createSettingsObject(settingsObjectClass, prov);
		return settingsObject;
	}

	@Override
	public void extractProperties(Class<? extends SO> settingsObjectClass, PropertyCollector coll) {

		GenereteIdentifierSettings genereteIdentifierSettings = settingsObjectClass.getAnnotation(GenereteIdentifierSettings.class);
		if(genereteIdentifierSettings != null && genereteIdentifierSettings.isGenereted()) {
			String[][] penalityCalculations = IIdentifierSettings.PENALTY_CALCULATION_OPTIONS;
			ButtonGroupEnumInterface[] buttons = new ButtonGroupEnumInterface[penalityCalculations.length];
			int i = 0;
			for(String[] penalityCalculation : penalityCalculations) {
				ButtonGroupEnumInterface button = new ButtonGroupEnumInterface() {

					@Override
					public boolean isDefault() {

						return penalityCalculations[0][1].equals(penalityCalculation[1]);
					}

					@Override
					public String getToolTip() {

						return null;
					}

					@Override
					public String getText() {

						return penalityCalculation[0];
					}

					@Override
					public String getActionCommand() {

						return penalityCalculation[1];
					}
				};
				buttons[i] = button;
				i++;
			}
			coll.addStringProperty(PENALITY_CALCULATION, PENALITY_CALCULATION, penalityCalculations[0][1], "", buttons);
			coll.addIntProperty(MAX_PENALITY, MAX_PENALITY, 20, "", 10, 0, 100);
			coll.addIntProperty(PENALITY_CALCULATION_LEVEL_FACTOR, PENALITY_CALCULATION_LEVEL_FACTOR, 5, "", 10, 1, 1000);
			coll.addRetentionTimeMinutesProperty(RETENTION_TIME_WINDOW, RETENTION_TIME_WINDOW, 12000, "", 6000, 6, 60000);
			coll.addDoubleProperty(RETENTION_INDEX_WINDOW, RETENTION_INDEX_WINDOW, 10.0, "", 1.0, 10.0, 20.0);
		}
		settingObjectSupplier.extractProperties(settingsObjectClass, coll);
	}
}
