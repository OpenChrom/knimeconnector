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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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

	private static final int INTERNAL_VERSION_ID = 1;
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

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

		super.readExternal(in);
		int version = in.read();
		switch(version) {
			case 1:
				settingsClassSupplier = new JacksonSettingObjectSupplier<>();
				break;
			default:
				break;
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {

		super.writeExternal(out);
		out.writeInt(INTERNAL_VERSION_ID);
	}

	public static List<ChromatogramReport> readString(String data) throws IOException, ClassNotFoundException {

		List<ChromatogramReport> reports = new ArrayList<>();
		byte[] byteData = Base64.getDecoder().decode(data);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(byteData));
		int size = ois.readInt();
		for(int i = 0; i < size; i++) {
			reports.add((ChromatogramReport)ois.readObject());
		}
		ois.close();
		return reports;
	}

	public static String writeToString(List<ChromatogramReport> reports) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		oos = new ObjectOutputStream(baos);
		oos.writeInt(reports.size());
		for(ChromatogramReport report : reports) {
			oos.writeObject(report);
		}
		oos.close();
		return Base64.getEncoder().encodeToString(baos.toByteArray());
	}
}
