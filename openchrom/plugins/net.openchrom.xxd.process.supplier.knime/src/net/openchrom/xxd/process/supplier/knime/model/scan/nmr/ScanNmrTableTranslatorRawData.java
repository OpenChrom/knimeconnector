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
package net.openchrom.xxd.process.supplier.knime.model.scan.nmr;

import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.eclipse.chemclipse.nmr.model.core.IScanNMR;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import net.openchrom.xxd.process.supplier.knime.model.scan.ScanTableTranslator;

public class ScanNmrTableTranslatorRawData implements IScanNmrTableTranslator {

	private final String rawColumnName = "Raw Data";

	public ScanNmrTableTranslatorRawData() {
	}

	@Override
	public BufferedDataTable getBufferedDataTable(IScanNMR scanNMR, ExecutionContext exec) throws CanceledExecutionException, NoExtractedIonSignalStoredException {

		return ScanTableTranslator.doubleArrayToTable(scanNMR.getRawSignals(), rawColumnName, exec);
	}
}
