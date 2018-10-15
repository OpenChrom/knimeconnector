/*******************************************************************************
 * Copyright (c) 2017 Martin Horn.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Martin Horn, University of Konstanz - initial API and implementation
 *******************************************************************************/
package net.openchrom.process.supplier.knime.ui.dialogfactory;

import java.util.Map;

import org.knime.core.node.NodeDialogPane;

/**
 * Generates a KNIME-dialog, i.e. {@link NodeDialogPane}, and the respective settings models (for persistence) from arbitrary classes that define the dialogs fields (e.g. by jackons-annotations).
 *
 * TODO: might be expose as an extension point eventually.
 * TODO: make it stateless? (such that, e.g., settingsObjectClass is passed to every method call instead of using 'setSettingsObjectClass')
 *
 * @author Martin Horn, University of Konstanz
 *
 * @param <SO>
 *            the settings object - usually a bean-like class that represents all settings for the dialog to be generated. It must have an empty constructor and is usually mutable.
 */
public interface SettingsDialogFactory<SO> {

	/**
	 * Used to check whether this settings dialog factory is suitable for the given class.
	 *
	 * @param settingsObjectClass
	 * @return <code>true</code> if the dialog can be created for the given class, otherwise <code>false</code>
	 */
	boolean conforms(Class<SO> settingsObjectClass);

	/**
	 * Same as {@link #conforms(Class)} but checks the object instance instead of the class only.
	 *
	 * @param settingsObject
	 * @return <code>true</code> if the dialog can be created for the given class, otherwise <code>false</code>
	 */
	boolean conforms(SO settingsObject);

	/**
	 * Creates the actual dialog based on the set settings object class ({@link #setSettingsObjectClass(Class)}).
	 *
	 * @return the newly created dialog
	 */
	NodeDialogPane createDialog();

	/**
	 * Creates a map of the options available in the created dialog (usually maps the option name to it's description). The descriptions are, e.g., added to the node description.
	 * The description are generated from the previously set settings object class ({@link #setSettingsObjectClass(Class)}).
	 *
	 * @return map of the option names and their descriptions
	 */
	Map<String, String> createDialogOptionDescriptions();

	/**
	 * Creates an object that wraps the actual settings object class instance. It usually also contains the settings models for persisting the dialog settings.
	 * The settings object class needs to be set before calling this method ({@link #setSettingsObjectClass(Class)}).
	 *
	 * @return the settings object wrapper
	 */
	SettingsObjectWrapper<SO> createSettingsObjectWrapper();

	/**
	 * The priority to use this dialog factory if multiple factories are registered at the (to be added) extension point.
	 *
	 * TODO could also be defined as an extension point attribute
	 *
	 * @return the priority of this dialog factory
	 */
	int getPriority();

	/**
	 * To be called before any of the {@link #createDialog()}, {@link #createSettingsObjectWrapper()} or {@link #createDialogOptionDescriptions()}-methods are called.
	 *
	 * @param settingsObjectClass
	 */
	void setSettingsObjectClass(Class<? extends SO> settingsObjectClass);
}
