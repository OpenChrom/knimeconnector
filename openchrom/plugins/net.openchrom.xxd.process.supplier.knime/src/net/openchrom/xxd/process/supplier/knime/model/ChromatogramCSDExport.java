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
import org.eclipse.chemclipse.csd.converter.chromatogram.ChromatogramConverterCSD;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import net.openchrom.process.supplier.knime.model.AbstractDataExport;

public class ChromatogramCSDExport extends AbstractDataExport<IChromatogramCSD> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2218922389085563577L;

	public ChromatogramCSDExport(String id, File directory) {

		super(id, directory);
	}

	@Override
	public IProcessingInfo process(IChromatogramCSD chromatogramCSD, IProgressMonitor monitor) throws Exception {

		String filePath = generateFilePath(chromatogramCSD);
		return ChromatogramConverterCSD.convert(new File(filePath), chromatogramCSD, getId(), new NullProgressMonitor());
	}

	@Override
	public String getName() {

		try {
			return ChromatogramConverterCSD.getChromatogramConverterSupport().getSupplier(getId()).getFilterName();
		} catch(NoConverterAvailableException e) {
		}
		return null;
	}

	@Override
	public String getDescription() {

		try {
			return ChromatogramConverterCSD.getChromatogramConverterSupport().getSupplier(getId()).getDescription();
		} catch(NoConverterAvailableException e) {
		}
		return null;
	}
}
