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

import org.eclipse.chemclipse.chromatogram.xxd.report.settings.IChromatogramReportSettings;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public abstract class AbstractChromatogramReport extends AbstractChromatogramOutput<IChromatogramSelection> implements IChromatogramReport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6944357547302545583L;
	private final static ObjectMapper mapper;
	private boolean append = false;
	static {
		mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}
	private String settings;

	protected AbstractChromatogramReport() {
		super();
	}

	public AbstractChromatogramReport(String id, File directory) {
		super(id, directory);
		append = false;
	}

	public AbstractChromatogramReport(String id, File directory, IChromatogramReportSettings settings) throws JsonProcessingException {
		super(id, directory);
		this.settings = mapper.writeValueAsString(settings);
		append = false;
	}

	public AbstractChromatogramReport(String id, File directory, String fileName) {
		super(id, directory, fileName);
		append = true;
	}

	public AbstractChromatogramReport(String id, File directory, String fileName, IChromatogramReportSettings settings) throws JsonProcessingException {
		super(id, directory, fileName);
		this.settings = mapper.writeValueAsString(settings);
		append = true;
	}

	@Override
	public boolean isAppend() {

		return append;
	}

	@Override
	public void setAppend(boolean append) {

		this.append = append;
	}

	protected abstract Class<? extends IChromatogramReportSettings> getSettingsClass(String id) throws Exception;

	@Override
	public IProcessingInfo process(IChromatogramSelection chromatogramSelection, IProgressMonitor monitor) throws Exception {

		String id = getId();
		File file = new File(generateFilePath(chromatogramSelection));
		if(settings == null) {
			return process(chromatogramSelection, file, id, append, monitor);
		} else {
			Class<? extends IChromatogramReportSettings> settingClass = getSettingsClass(id);
			IChromatogramReportSettings settingsObject = mapper.readValue(settings, settingClass);
			return process(chromatogramSelection, file, id, append, settingsObject, monitor);
		}
	}

	protected abstract IProcessingInfo process(IChromatogramSelection chromatogramSelection, File file, String id, boolean append, IProgressMonitor monitor) throws Exception;

	protected abstract IProcessingInfo process(IChromatogramSelection chromatogramSelection, File file, String id, boolean append, IChromatogramReportSettings settings, IProgressMonitor monitor) throws Exception;
}
