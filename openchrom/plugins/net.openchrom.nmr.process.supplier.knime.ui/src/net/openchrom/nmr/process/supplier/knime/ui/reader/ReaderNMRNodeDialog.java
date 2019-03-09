/*******************************************************************************
 * Copyright (c) 2017, 2019 Lablicate GmbH.
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
 * Jan Holy - implementation
 *******************************************************************************/
package net.openchrom.nmr.process.supplier.knime.ui.reader;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.chemclipse.converter.core.ISupplier;
import org.eclipse.chemclipse.converter.scan.IScanConverterSupport;
import org.eclipse.chemclipse.nmr.converter.core.ScanConverterNMR;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;

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
public class ReaderNMRNodeDialog extends DefaultNodeSettingsPane {

	protected ReaderNMRNodeDialog() {

		super();
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
		DialogComponentFileChooser dialogComponentFileChooser = new DialogComponentFileChooser(ReaderNMRNodeModel.getSettingsFileInput(), "", validExtensions);
		addDialogComponent(dialogComponentFileChooser);
		createNewTab("Debuging");
		addDialogComponent(new DialogComponentBoolean(ReaderNMRNodeModel.getSettingsDebuggingMode(), "Activate debuging mode"));
		addDialogComponent(new DialogComponentNumber(ReaderNMRNodeModel.getSettingsMaxNumberOfPoints(), "Maximum Numeber of points", 100));
	}
}
