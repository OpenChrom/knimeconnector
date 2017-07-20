/*******************************************************************************
 * Copyright (c) 2017 hornm.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * hornm - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.dialogfactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * TODO
 * 
 * Collects the extenion points and provides the logics to get the right settings dialog factory.
 * 
 * @author hornm
 *
 */
public class SettingsDialogManager {

	/**
	 * TODO fill list from extension point ..
	 */
	private static final List<SettingsDialogFactory> SETTINGS_DIALOG_FACTORIES = Arrays.asList(new GetterSetterSettingsDialogFactory());

	/**
	 * 
	 * @param settingsObjectClass
	 * @return an empty optional if no settings dialog factory conforms with the settings object class
	 */
	public static <S> Optional<SettingsDialogFactory<S>> getSettingsDialogFactoryFor(Class<?> settingsObjectClass) {

		// TODO get the dialog factory with the highest priority that conform with the given settings object class
		return Optional.of(SETTINGS_DIALOG_FACTORIES.get(0));
	}
}
