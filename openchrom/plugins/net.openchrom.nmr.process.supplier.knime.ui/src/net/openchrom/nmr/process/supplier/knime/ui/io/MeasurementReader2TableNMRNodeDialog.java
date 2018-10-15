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
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.nmr.process.supplier.knime.ui.io;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.chemclipse.converter.core.ISupplier;
import org.eclipse.chemclipse.converter.scan.IScanConverterSupport;
import org.eclipse.chemclipse.nmr.converter.core.ScanConverterNMR;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;

/**
 * <code>NodeDialog</code> for the "MeasurementWriterNMR" Node.
 * This node writes chromatographic data.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 *
 * @author OpenChrom
 */
public class MeasurementReader2TableNMRNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring MeasurementWriterNMR node dialog.
	 * This is just a suggestion to demonstrate possible default dialog
	 * components.
	 */
	protected MeasurementReader2TableNMRNodeDialog() {
		super();
		// select defaul file
		IScanConverterSupport converterSupport = ScanConverterNMR.getScanConverterSupport();
		Set<String> extensions = new HashSet<String>();
		for(ISupplier supplier : converterSupport.getSupplier()) {
			if(supplier.isImportable()) {
				extensions.add(supplier.getFileExtension());
			}
		}
		String[] validExtensions;
		if(extensions.size() > 0) {
			validExtensions = extensions.toArray(new String[extensions.size() + 1]);
			Arrays.sort(validExtensions, Comparator.nullsFirst(Comparator.naturalOrder()));
			StringBuilder sb = new StringBuilder();
			int i = 1;
			for(i = 1; i < validExtensions.length - 1; i++) {
				sb.append(validExtensions[i]);
				sb.append('|');
			}
			sb.append(validExtensions[i]);
			validExtensions[0] = sb.toString();
		} else {
			validExtensions = new String[]{};
		}
		//
		DialogComponentFileChooser dialogComponentFileChooser = new DialogComponentFileChooser(MeasurementReader2TableNMRNodeModel.SETTING_NMR_FILE_INPUT, "", validExtensions);
		addDialogComponent(dialogComponentFileChooser);
		// select table type
		DialogComponentButtonGroup componentButtonGroup = new DialogComponentButtonGroup(MeasurementReader2TableNMRNodeModel.SETTING_NMR_TABLE_OUTPUT, true, "Table OUTPUT", MeasurementReader2TableNMRNodeModel.CHEMCAL_SHIFT, MeasurementReader2TableNMRNodeModel.RAW_DATA);
		addDialogComponent(componentButtonGroup);
	}
}
