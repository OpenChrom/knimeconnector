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
import java.io.IOException;
import java.io.ObjectInputStream;

import org.eclipse.chemclipse.chromatogram.xxd.report.core.ChromatogramReports;
import org.eclipse.chemclipse.chromatogram.xxd.report.exceptions.NoReportSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.xxd.report.settings.IChromatogramReportSettings;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.openchrom.process.supplier.knime.dialogfactory.JacksonSettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.SettingObjectSupplier;
import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;
import net.openchrom.process.supplier.knime.model.AbstractDataReport;

public class ChromatogramReport extends AbstractDataReport<IChromatogramReportSettings, IChromatogram> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2897126156184922271L;
	private transient SettingObjectSupplier<IChromatogramReportSettings> settingsClassSupplier = new JacksonSettingObjectSupplier<>();

	public ChromatogramReport() {

		super();
	}

	public ChromatogramReport(String id, File directory, PropertyProvider prov) throws JsonProcessingException {

		super(id, directory, prov);
	}

	public ChromatogramReport(String id, File directory, String fileName, PropertyProvider prov) throws JsonProcessingException {

		super(id, directory, fileName);
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
	protected IProcessingInfo process(IChromatogram chromatogram, File file, String id, boolean append, IProgressMonitor monitor) throws Exception {

		return ChromatogramReports.generate(file, append, chromatogram, id, monitor);
	}

	@Override
	protected IProcessingInfo process(IChromatogram chromatogram, File file, String id, boolean append, IChromatogramReportSettings settings, IProgressMonitor monitor) throws Exception {

		return ChromatogramReports.generate(file, append, chromatogram, settings, id, monitor);
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

	@Override
	protected SettingObjectSupplier<? extends IChromatogramReportSettings> getSettingsClassSupplier() {

		return settingsClassSupplier;
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {

		in.defaultReadObject();
		settingsClassSupplier = new JacksonSettingObjectSupplier<>();
	}
}
