/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 * 
 * This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
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
