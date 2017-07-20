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
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * TODO: expose as extension point ...
 * 
 * @author Martin Horn, University of Konstanz
 *
 * @param <S>
 */
public interface SettingsDialogFactory<S> {
	
	/**
	 * TODO could also be defined in the extension point ..
	 * 
	 * @return
	 */
	int getPriority();
	
	NodeDialogPane createDialog(Class<? extends S> settingsObjectClass);
	
	SettingsObject<? extends S> createSettings(Class<? extends S> settingsObjectClass);
	
	boolean conforms(Class<S> settingsObjectClass);
	
	boolean conforms(S settingsObject);
	
	
	
}
