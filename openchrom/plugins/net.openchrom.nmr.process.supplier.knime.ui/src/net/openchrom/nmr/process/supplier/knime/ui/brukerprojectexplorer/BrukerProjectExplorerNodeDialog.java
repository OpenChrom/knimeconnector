/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
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
 * Jan Holy- initial API and implementation
 *******************************************************************************/
package net.openchrom.nmr.process.supplier.knime.ui.brukerprojectexplorer;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;

import net.openchrom.process.supplier.knime.ui.node.listfilesfolder.ListFilesFolderNodeDialog;

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
public class BrukerProjectExplorerNodeDialog extends DefaultNodeSettingsPane {

	protected BrukerProjectExplorerNodeDialog() {

		super();
		DialogComponentFileChooser directoryChooser = new DialogComponentFileChooser(BrukerProjectExplorerNodeModel.getSettingsFileInput(), ListFilesFolderNodeDialog.class.getName(), JFileChooser.OPEN_DIALOG, true);
		addDialogComponent(directoryChooser);
		DialogComponentBoolean loadRawFile = new DialogComponentBoolean(BrukerProjectExplorerNodeModel.getSettingsLoadRawFile(), "Load raw File");
		addDialogComponent(loadRawFile);
	}
}
