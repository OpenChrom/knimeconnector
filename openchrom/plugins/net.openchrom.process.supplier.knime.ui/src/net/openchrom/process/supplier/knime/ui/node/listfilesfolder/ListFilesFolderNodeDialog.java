/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.process.supplier.knime.ui.node.listfilesfolder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;

import org.eclipse.chemclipse.converter.core.ISupplier;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;

import net.openchrom.process.supplier.knime.ui.dialog.DialogComponentStringIdSelection;

public class ListFilesFolderNodeDialog extends DefaultNodeSettingsPane {

	public ListFilesFolderNodeDialog(List<ISupplier> suppliers) {

		super();
		DialogComponentFileChooser directoryChooser = new DialogComponentFileChooser(ListFilesFolderNodeModel.getSettingInputFolder(), ListFilesFolderNodeDialog.class.getName(), JFileChooser.OPEN_DIALOG, true);
		addDialogComponent(directoryChooser);
		DialogComponentBoolean recursive = new DialogComponentBoolean(ListFilesFolderNodeModel.getSettingRecursive(), "Include Subfolder");
		addDialogComponent(recursive);
		Map<String, String> ids = new LinkedHashMap<>();
		for(ISupplier supplier : suppliers) {
			ids.put(supplier.getDescription(), supplier.getId());
		}
		DialogComponentStringIdSelection dialogComponentStringIdSelection = new DialogComponentStringIdSelection(ListFilesFolderNodeModel.getSettingSupplierID(), "Select Importer", ids, false);
		addDialogComponent(dialogComponentStringIdSelection);
	}
}