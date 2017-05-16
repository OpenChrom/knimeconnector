/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.knime.node;

import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.preferences.PreferenceSupplier;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;

public class ChromatogramFilterNodeDialog extends DefaultNodeSettingsPane {

	protected ChromatogramFilterNodeDialog() {
		super();
		//
		DialogComponentNumber dialogComponentDerivative = new DialogComponentNumber(ChromatogramFilterNodeModel.SETTING_DERIVATIVE, "Derivative", PreferenceSupplier.STEP_DERIVATIVE);
		addDialogComponent(dialogComponentDerivative);
		//
		DialogComponentNumber dialogComponentOrder = new DialogComponentNumber(ChromatogramFilterNodeModel.SETTING_ORDER, "Order", PreferenceSupplier.STEP_ORDER);
		addDialogComponent(dialogComponentOrder);
		//
		DialogComponentNumber dialogComponentWidth = new DialogComponentNumber(ChromatogramFilterNodeModel.SETTING_WIDTH, "Width", PreferenceSupplier.STEP_WIDTH);
		addDialogComponent(dialogComponentWidth);
	}
}
