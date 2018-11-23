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
package net.openchrom.nmr.process.supplier.knime.table;

import org.eclipse.chemclipse.nmr.model.core.IScanNMR;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import net.openchrom.process.supplier.knime.support.TableTranslator;

public class DataTableTranslator {

	private static final String SCAN_NMR_COLUMN_X = "Chemical Shift";
	private static final String SCAN_NMR_COLUMN_Y = "Intensity";

	public static BufferedDataTable getBufferedDataTableNMR(IScanNMR scanNMR, ExecutionContext exec) throws CanceledExecutionException {

		return TableTranslator.scanToTable(scanNMR.getSignalsNMR(), SCAN_NMR_COLUMN_X, SCAN_NMR_COLUMN_Y, exec);
	}

	public static BufferedDataTable getBufferedDataTableFID(IScanNMR scanNMR, ExecutionContext exec) throws CanceledExecutionException {

		return TableTranslator.scanToTable(scanNMR.getSignalsFID(), SCAN_NMR_COLUMN_X, SCAN_NMR_COLUMN_Y, exec);
	}
}
