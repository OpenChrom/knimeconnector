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
package net.openchrom.xxd.process.supplier.knime.model;

import java.io.File;

import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.msd.converter.chromatogram.ChromatogramConverterMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

public class ChromatogramMSDExport extends AbstractChromatogramExport<IChromatogramSelectionMSD> implements IChromatogramMSDExport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2218922389085563577L;

	public ChromatogramMSDExport(String id, File directory) {
		super(id, directory);
	}

	@Override
	public IProcessingInfo process(IChromatogramSelectionMSD chromatogramSelection, IProgressMonitor monitor) throws Exception {

		IChromatogramMSD chromatogramMSD = chromatogramSelection.getChromatogramMSD();
		String filePath = generateFilePath(chromatogramSelection);
		return ChromatogramConverterMSD.convert(new File(filePath), chromatogramMSD, getId(), new NullProgressMonitor());
	}

	@Override
	public String getName() {

		try {
			return ChromatogramConverterMSD.getChromatogramConverterSupport().getSupplier(getId()).getFilterName();
		} catch(NoConverterAvailableException e) {
		}
		return null;
	}

	@Override
	public String getDescription() {

		try {
			return ChromatogramConverterMSD.getChromatogramConverterSupport().getSupplier(getId()).getDescription();
		} catch(NoConverterAvailableException e) {
		}
		return null;
	}
}
