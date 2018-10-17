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
package net.openchrom.nmr.process.supplier.knime.model;

import java.io.File;

import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.nmr.converter.core.ScanConverterNMR;
import org.eclipse.chemclipse.nmr.model.core.IScanNMR;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import net.openchrom.process.supplier.knime.model.AbstractDataExport;

public class ScanNMRExport extends AbstractDataExport<IScanNMR> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2218922389085563577L;

	public ScanNMRExport(String id, File directory) {

		super(id, directory);
	}

	@Override
	public IProcessingInfo process(IScanNMR scanNmr, IProgressMonitor monitor) throws Exception {

		String filePath = generateFilePath(scanNmr);
		return ScanConverterNMR.convert(new File(filePath), scanNmr, getId(), new NullProgressMonitor());
	}

	@Override
	public String getName() {

		try {
			return ScanConverterNMR.getScanConverterSupport().getSupplier(getId()).getFilterName();
		} catch(NoConverterAvailableException e) {
		}
		return null;
	}

	@Override
	public String getDescription() {

		try {
			return ScanConverterNMR.getScanConverterSupport().getSupplier(getId()).getDescription();
		} catch(NoConverterAvailableException e) {
		}
		return null;
	}
}
