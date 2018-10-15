/*******************************************************************************
 * Copyright (c) 2017 hornm.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * hornm - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.portobject;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.ModelContentWO;
import org.knime.core.node.port.AbstractSimplePortObjectSpec;

public class ChromatogramSelectionMSDPortObjectSpec extends AbstractSimplePortObjectSpec {

	public static final class Serializer extends AbstractSimplePortObjectSpecSerializer<ChromatogramSelectionMSDPortObjectSpec> {
	}

	public static final String MODE_IMMEDIATE_PROCESSING = "MODE_IMMEDIATE_PROCESSING";
	public static final String MODE_POSTPONED_PROCESSING = "MODE_POSTPONED_PROCESSING";
	public static final String PROCESSING_MODE = "PROCESSING_MODE";
	private String processingMode;

	public ChromatogramSelectionMSDPortObjectSpec() {
		processingMode = MODE_IMMEDIATE_PROCESSING;
	}

	public String getProcessingMode() {

		return processingMode;
	}

	@Override
	protected void load(ModelContentRO model) throws InvalidSettingsException {

		processingMode = model.getString(PROCESSING_MODE, MODE_IMMEDIATE_PROCESSING);
	}

	@Override
	protected void save(ModelContentWO model) {

		model.addString(PROCESSING_MODE, processingMode);
	}

	public void setProcessingMode(String processingMode) {

		assert processingMode != null && (processingMode.equals(MODE_IMMEDIATE_PROCESSING) || processingMode.equals(MODE_POSTPONED_PROCESSING)) : "Invalid processing mode";
		this.processingMode = processingMode;
	}
}
