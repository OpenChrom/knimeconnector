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
 * Provides (and defines) the possible properties from a {@link PropertyDialogFactory}. It's usually used in order to create the final settings object (via {@link PropertyDialogFactory#createSettingsObject(Class, PropertyProvider)}).
 * 
 * @author Martin Horn, University of Konstanz
 *
 */
public interface PropertyProvider {

	int getIntProperty(String id);

	float getFloatProperty(String id);
}
