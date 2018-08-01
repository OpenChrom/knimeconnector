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

import org.eclipse.chemclipse.chromatogram.xxd.report.core.ChromatogramReports;
import org.eclipse.chemclipse.chromatogram.xxd.report.exceptions.NoReportSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.xxd.report.settings.IChromatogramReportSettings;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ChromatogramReport extends AbstractChromatogramReport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2897126156184922271L;

	public ChromatogramReport() {
		super();
	}

	public ChromatogramReport(String id, File directory, IChromatogramReportSettings settings) throws JsonProcessingException {
		super(id, directory, settings);
	}

	public ChromatogramReport(String id, File directory, String fileName, IChromatogramReportSettings settings) throws JsonProcessingException {
		super(id, directory, fileName, settings);
	}

	public ChromatogramReport(String id, File directory, String fileName) {
		super(id, directory, fileName);
	}

	public ChromatogramReport(String id, File directory) {
		super(id, directory);
	}

	@Override
	protected Class<? extends IChromatogramReportSettings> getSettingsClass(String id) throws Exception {

		return ChromatogramReports.getChromatogramReportSupplierSupport().getReportSupplier(id).getChromatogramReportSettingsClass();
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelection chromatogramSelection, File file, String id, boolean append, IProgressMonitor monitor) throws Exception {

		return ChromatogramReports.generate(file, append, chromatogramSelection.getChromatogram(), id, monitor);
	}

	@Override
	protected IProcessingInfo process(IChromatogramSelection chromatogramSelection, File file, String id, boolean append, IChromatogramReportSettings settings, IProgressMonitor monitor) throws Exception {

		return ChromatogramReports.generate(file, append, chromatogramSelection.getChromatogram(), settings, id, monitor);
	}

	@Override
	public String getName() {

		try {
			return ChromatogramReports.getChromatogramReportSupplierSupport().getReportSupplier(getId()).getReportName();
		} catch(NoReportSupplierAvailableException e) {
		}
		return null;
	}

	@Override
	public String getDescription() {

		try {
			return ChromatogramReports.getChromatogramReportSupplierSupport().getReportSupplier(getId()).getDescription();
		} catch(NoReportSupplierAvailableException e) {
		}
		return null;
	}
}
