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
package net.openchrom.xxd.process.supplier.knime.ui.io.msd;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.chemclipse.converter.chromatogram.ChromatogramConverterSupport;
import org.eclipse.chemclipse.converter.core.ISupplier;
import org.eclipse.chemclipse.msd.converter.chromatogram.ChromatogramConverterMSD;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;

public class ChromatogramReaderNodeDialog extends DefaultNodeSettingsPane {

	protected ChromatogramReaderNodeDialog() {
		super();
		//
		ChromatogramConverterSupport chromatogramConverterSupport = ChromatogramConverterMSD.getChromatogramConverterSupport();
		Set<String> extensions = new HashSet<String>();
		for(ISupplier supplier : chromatogramConverterSupport.getSupplier()) {
			if(supplier.isImportable()) {
				extensions.add(supplier.getFileExtension());
			}
		}
		//
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
		DialogComponentFileChooser dialogComponentFileChooser = new DialogComponentFileChooser(ChromatogramReaderNodeModel.getSettingsChromatogamFileInput(), "", validExtensions);
		addDialogComponent(dialogComponentFileChooser);
	}
}
