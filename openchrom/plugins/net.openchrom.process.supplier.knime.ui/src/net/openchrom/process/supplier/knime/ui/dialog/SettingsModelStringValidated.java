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
package net.openchrom.process.supplier.knime.ui.dialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class SettingsModelStringValidated extends SettingsModelString {

	private String regExpression;

	public SettingsModelStringValidated(String configName, String defaultValue, String regExpression) {
		super(configName, defaultValue);
		this.regExpression = regExpression;
	}

	@Override
	public void setStringValue(String newValue) {

		checkValue(newValue);
		super.setStringValue(newValue);
	}

	@Override
	protected void validateSettingsForModel(NodeSettingsRO settings) throws InvalidSettingsException {

		super.validateSettingsForModel(settings);
		String value = settings.getString(getKey());
		try {
			checkValue(value);
		} catch(IllegalArgumentException iae) {
			throw new InvalidSettingsException(iae.getMessage());
		}
	}

	private void checkValue(String value) {

		Pattern p = Pattern.compile(regExpression);
		Matcher m = p.matcher(value);
		if(!m.matches()) {
			// throw new IllegalArgumentException("Input string is not valid");
		}
	}
}
