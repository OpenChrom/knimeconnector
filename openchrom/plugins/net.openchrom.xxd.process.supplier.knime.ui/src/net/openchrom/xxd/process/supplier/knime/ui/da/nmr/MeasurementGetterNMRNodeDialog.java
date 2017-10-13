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
package net.openchrom.xxd.process.supplier.knime.ui.da.nmr;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;

/**
 * <code>NodeDialog</code> for the "MeasurementReaderNMR" Node.
 * This node is reads chromatographic raw data.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 *
 * @author OpenChrom
 */
public class MeasurementGetterNMRNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring MeasurementReaderNMR node dialog.
	 * This is just a suggestion to demonstrate possible default dialog
	 * components.
	 */
	protected MeasurementGetterNMRNodeDialog() {
		super();
		addDialogComponent(new DialogComponentNumber(new SettingsModelIntegerBounded(MeasurementGetterNMRNodeModel.CFGKEY_COUNT, MeasurementGetterNMRNodeModel.DEFAULT_COUNT, Integer.MIN_VALUE, Integer.MAX_VALUE), "Counter:", /* step */ 1, /* componentwidth */ 5));
	}
}
