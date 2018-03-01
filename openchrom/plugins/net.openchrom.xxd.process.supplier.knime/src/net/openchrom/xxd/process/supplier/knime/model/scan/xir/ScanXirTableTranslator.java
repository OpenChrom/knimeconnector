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
package net.openchrom.xxd.process.supplier.knime.model.scan.xir;

import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.eclipse.chemclipse.xir.model.core.IScanXIR;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import net.openchrom.xxd.process.supplier.knime.model.scan.ScanTableTranslator;

public class ScanXirTableTranslator implements IScanXirTableTranslator {

	private String type;
	private final String rawColumnName = "";
	private final String scanColumnX = "";
	private final String scanColumnY = "";

	public ScanXirTableTranslator() {
		this.type = IScanXirTableTranslator.TYPE_SIGANALS;
	}

	@Override
	public BufferedDataTable getBufferedDataTableTIC(IScanXIR scanXIR, ExecutionContext exec) throws CanceledExecutionException, NoExtractedIonSignalStoredException {

		switch(type) {
			case IScanXirTableTranslator.TYPE_RAW_DATA:
				return ScanTableTranslator.rawDataToTable(scanXIR.getRawSignals(), rawColumnName, exec);
			case IScanXirTableTranslator.TYPE_SIGANALS:
				return ScanTableTranslator.scanToTable(scanXIR, scanColumnX, scanColumnY, exec);
			default:
				return null;
		}
	}

	@Override
	public void setType(String type) {

		if(type.equals(IScanXirTableTranslator.TYPE_RAW_DATA) || type.equals(IScanXirTableTranslator.TYPE_SIGANALS)) {
			this.type = type;
		}
	}

	@Override
	public String getType() {

		return this.type;
	}
}
