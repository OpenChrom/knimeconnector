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
package net.openchrom.xxd.process.supplier.knime.ui.manipulator;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;

public class ChromatogramSelectionModifierNodeDialog extends DefaultNodeSettingsPane {

	protected ChromatogramSelectionModifierNodeDialog() {
		super();
		//
		DialogComponentNumber dialogComponentStartRT = new DialogComponentNumber(ChromatogramSelectionModifierNodeModel.createSettingsModelStartRetentionTime(), ChromatogramSelectionModifierNodeModel.START_RETENTION_TIME, ChromatogramSelectionModifierNodeModel.DEF_START_RETENTION_TIME);
		addDialogComponent(dialogComponentStartRT);
		//
		DialogComponentNumber dialogComponentStopRT = new DialogComponentNumber(ChromatogramSelectionModifierNodeModel.createSettingsModelStopRetentionTime(), ChromatogramSelectionModifierNodeModel.STOP_RETENTION_TIME, ChromatogramSelectionModifierNodeModel.DEF_STOP_RETENTION_TIME);
		addDialogComponent(dialogComponentStopRT);
	}
}
