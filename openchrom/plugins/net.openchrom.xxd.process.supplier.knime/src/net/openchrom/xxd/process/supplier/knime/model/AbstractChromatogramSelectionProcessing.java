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
package net.openchrom.xxd.process.supplier.knime.model;

import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public abstract class AbstractChromatogramSelectionProcessing<Settings, ChromatogramSelection extends IChromatogramSelection> implements IChromatogramSelectionProcessing<ChromatogramSelection> {

	private final static ObjectMapper mapper;
	/**
	 *
	 */
	private static final long serialVersionUID = 6076099451477368555L;
	static {
		mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}
	private String id;
	private String settings;

	protected AbstractChromatogramSelectionProcessing() {
	}

	public AbstractChromatogramSelectionProcessing(String id) {
		this();
		if(id == null) {
			throw new NullPointerException("Parameter ID cannot be null");
		}
		this.id = id;
	}

	public AbstractChromatogramSelectionProcessing(String id, Settings settings) throws JsonProcessingException {
		this(id);
		if(settings == null) {
			throw new NullPointerException("Parameter Settings cannot be null");
		}
		this.settings = mapper.writeValueAsString(settings);
	}

	protected abstract Class<? extends Settings> getSettingsClass(String id) throws Exception;

	@Override
	public IProcessingInfo process(ChromatogramSelection chromatogramSelection, IProgressMonitor monitor) throws Exception {

		if(settings == null) {
			return process(chromatogramSelection, id, monitor);
		} else {
			Class<? extends Settings> settingClass = getSettingsClass(id);
			Settings settingsObject = mapper.readValue(settings, settingClass);
			return process(chromatogramSelection, id, settingsObject, monitor);
		}
	}

	protected abstract IProcessingInfo process(ChromatogramSelection chromatogramSelection, String id, IProgressMonitor monitor) throws Exception;

	protected abstract IProcessingInfo process(ChromatogramSelection chromatogramSelection, String id, Settings settings, IProgressMonitor monitor) throws Exception;
}
