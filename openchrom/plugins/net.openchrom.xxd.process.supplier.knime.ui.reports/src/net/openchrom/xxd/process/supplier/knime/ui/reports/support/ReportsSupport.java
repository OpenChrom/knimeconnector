/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.reports.support;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.chromatogram.filter.exceptions.NoChromatogramFilterSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.xxd.report.core.ChromatogramReports;
import org.eclipse.chemclipse.chromatogram.xxd.report.core.IChromatogramReportSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.report.exceptions.NoReportSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.xxd.report.processing.IChromatogramReportProcessingInfo;
import org.eclipse.chemclipse.chromatogram.xxd.report.settings.IChromatogramReportSettings;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.core.runtime.IProgressMonitor;

public class ReportsSupport {

	public static IChromatogramReportProcessingInfo generate(File file, IChromatogram chromatogram, IChromatogramReportSettings chromatogramReportSettings, String reportSupplierId, IProgressMonitor monitor) throws NoReportSupplierAvailableException {

		if(isReport(reportSupplierId)) {
			return ChromatogramReports.generate(file, false, chromatogram, chromatogramReportSettings, reportSupplierId, monitor);
		}
		throw new NoReportSupplierAvailableException();
	}

	public static List<String> getIDsReportMSD() throws NoChromatogramFilterSupplierAvailableException {

		List<String> ids = ChromatogramReports.getChromatogramReportSupplierSupport().getReportSupplier().stream().map(s -> s.getId()).collect(Collectors.toList());
		return ids;
	}

	public static IChromatogramReportSupplier getSupplier(String id) throws NoReportSupplierAvailableException {

		if(isReport(id)) {
			return ChromatogramReports.getChromatogramReportSupplierSupport().getReportSupplier(id);
		}
		throw new NoReportSupplierAvailableException();
	}

	private static boolean isReport(String id) {

		return ChromatogramReports.getChromatogramReportSupplierSupport().getReportSupplier().stream().map(s -> s.getId()).anyMatch(s -> s.equals(id));
	}
}
