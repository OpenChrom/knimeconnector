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
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.dialogfactory;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public interface SettingsObject<S> {
	
	void saveSettingsTo(NodeSettingsWO settings);
	
	void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException;
	
	void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException;
	
	S getObject();
	
}
