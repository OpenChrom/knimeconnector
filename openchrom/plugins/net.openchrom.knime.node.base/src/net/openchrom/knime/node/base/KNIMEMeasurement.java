/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *Alexander Kerner - initial API and implementation
 *******************************************************************************/
package net.openchrom.knime.node.base;

import java.util.Map;

import org.eclipse.chemclipse.model.core.IMeasurement;

/**
 * Default implementation for {@link IMeasurement}
 * 
 * @author Alexander Kerner
 *
 */
public interface KNIMEMeasurement extends IMeasurement {

	void setHeaderDataMap(Map<String, String> h);

}
