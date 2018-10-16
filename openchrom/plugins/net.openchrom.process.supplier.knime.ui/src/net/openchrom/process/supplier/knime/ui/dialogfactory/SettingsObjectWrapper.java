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

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;

/**
 * Wraps a settings object (i.e. one class/instance that represents all settings in a way, usually a bean-like class) and adds the functionality to save/load the settings within a KNIME node.
 *
 * @author Martin Horn, University of Konstanz
 *
 * @param <S>
 *            mutable object with an empty constructor that represents all settings
 */
public interface SettingsObjectWrapper<S> {

	/**
	 *
	 * @return the actual settings object, initialized either with default values or loaded ones
	 */
	S getObject();

	/**
	 * Used by KNIME nodes for persistence.
	 *
	 * @param settings
	 * @throws InvalidSettingsException
	 */
	void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException;

	/**
	 * Used by KNIME nodes for persistence.
	 *
	 * @param settings
	 */
	void saveSettingsTo(NodeSettingsWO settings);

	/**
	 * Used by KNIME nodes for persistence.
	 *
	 * @param settings
	 * @throws InvalidSettingsException
	 */
	void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException;

	PropertyProvider getPropertyProvider();
}
