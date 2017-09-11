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
package net.openchrom.xxd.process.supplier.knime.ui.filter.dialogfactory.property;

/**
 * Collects and defines the possible properties that can be added to the {@link PropertyDialogFactory}.
 * 
 * @author Martin Horn, University of Konstanz
 *
 */
public interface PropertyCollector {

	void addIntProperty(String id, String name, int defaultValue);

	void addFloatProperty(String id, String name, float defaultValue);

	void addDoubleProperty(String id, String name, double defaultValue);

	void addStringProperty(String id, String name, String defaultValue);

	void addPropertyDescriptions(String name, String description);
}
