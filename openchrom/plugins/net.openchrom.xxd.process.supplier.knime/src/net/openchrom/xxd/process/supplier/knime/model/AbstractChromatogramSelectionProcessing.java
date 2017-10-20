/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractChromatogramSelectionProcessing<Settings, ChromatogramSelection extends IChromatogramSelection> implements IChromatogramSelectionProcessing<ChromatogramSelection> {

	private final static ObjectMapper mapper = new ObjectMapper();
	/**
	 *
	 */
	private static final long serialVersionUID = 6076099451477368555L;
	String id;
	Class<?> settingClass;
	String settings;

	protected AbstractChromatogramSelectionProcessing() {
	}

	public AbstractChromatogramSelectionProcessing(String id) throws JsonProcessingException {
		this();
		this.id = id;
	}

	public AbstractChromatogramSelectionProcessing(String id, Settings settings) throws JsonProcessingException {
		this(id);
		if(id == null) {
			throw new NullPointerException("Parameter ID cannot be null");
		}
		this.id = id;
		this.settings = mapper.writeValueAsString(settings);
		this.settingClass = settings.getClass();
	}

	@SuppressWarnings("unchecked")
	@Override
	public IProcessingInfo process(ChromatogramSelection chromatogramSelection) throws Exception {

		if(settings == null) {
			return process(chromatogramSelection, id);
		} else {
			Settings settingsObject = (Settings)mapper.readValue(settings, settingClass);
			return process(chromatogramSelection, id, settingsObject);
		}
	}

	protected abstract IProcessingInfo process(ChromatogramSelection chromatogramSelection, String id) throws Exception;

	protected abstract IProcessingInfo process(ChromatogramSelection chromatogramSelection, String id, Settings settings) throws Exception;
}
