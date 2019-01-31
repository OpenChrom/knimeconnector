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
package net.openchrom.process.supplier.knime.ui.node.listfiles;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import net.openchrom.process.supplier.knime.ui.dialog.DialogComponentMultiFileChooser;

public class ListFilesNodeDialog extends DefaultNodeSettingsPane {

	public ListFilesNodeDialog(String fileType, String[] suffixes) {

		super();
		DialogComponentMultiFileChooser dialogComponentMultiFileChooser = new DialogComponentMultiFileChooser(ListFilesNodeModel.getSettingsChromatogamFileInput(), fileType, suffixes);
		addDialogComponent(dialogComponentMultiFileChooser);
	}
}