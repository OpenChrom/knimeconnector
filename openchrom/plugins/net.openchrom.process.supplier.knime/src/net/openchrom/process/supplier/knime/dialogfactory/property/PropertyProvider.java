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
package net.openchrom.process.supplier.knime.dialogfactory.property;

import java.util.Map;

/**
 *
 * @author Martin Horn, University of Konstanz
 *
 */
public interface PropertyProvider {

	boolean getBooleanProperty(String id);

	double getDoubleProperty(String id);

	float getFloatProperty(String id);

	int getIntProperty(String id);

	String getStringProperty(String id);

	/*
	 * as value object could be stored double, String, boolean or integer (float is save as double)
	 */
	Map<String, Object> values();
}
