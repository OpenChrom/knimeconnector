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
package net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.property;

/**
 * Collects and defines the possible properties that can be added to the {@link PropertyDialogFactory}.
 *
 * @author Martin Horn, University of Konstanz
 *
 */
public interface PropertyCollector {

	void addBooleanProperty(String id, String name, boolean defaultValue);

	void addDoubleProperty(String id, String name, double defaultValue);

	void addFloatProperty(String id, String name, float defaultValue);

	void addIntProperty(String id, String name, int defaultValue);

	void addPropertyDescriptions(String name, String description);

	void addStringProperty(String id, String name, String defaultValue);
}
