/*******************************************************************************
 * Copyright (c) 2017 Martin Horn.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Martin Horn - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.filter.dialogfactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.openchrom.xxd.process.supplier.knime.ui.filter.dialogfactory.property.JacksonPropertyDialogFactory;

/**
 * TODO not used yet - will be of use of the {@link SettingsDialogFactory} is exposed as extension point.
 * 
 * Collects the extension points and provides the logics to get the right settings dialog factory.
 * 
 * @author Martin Horn, University of Konstanz
 *
 */
public class SettingsDialogManager {

	/**
	 * TODO fill list from extension point ..
	 */
	@SuppressWarnings("rawtypes")
	private static final List<SettingsDialogFactory> SETTINGS_DIALOG_FACTORIES = Arrays.asList(new JacksonPropertyDialogFactory<>());

	/**
	 * 
	 * @param settingsObjectClass
	 * @return an empty optional if no settings dialog factory conforms with the settings object class
	 */
	@SuppressWarnings("unchecked")
	public static <S> Optional<SettingsDialogFactory<S>> getSettingsDialogFactoryFor(Class<?> settingsObjectClass) {

		// TODO get the dialog factory with the highest priority that conform with the given settings object class
		return Optional.of(SETTINGS_DIALOG_FACTORIES.get(0));
	}
}
