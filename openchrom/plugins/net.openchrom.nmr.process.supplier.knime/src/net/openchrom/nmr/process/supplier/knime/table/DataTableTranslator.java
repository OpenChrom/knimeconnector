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

import java.util.Iterator;

import org.apache.commons.math3.complex.Complex;
import org.eclipse.chemclipse.nmr.model.core.IScanFID;
import org.eclipse.chemclipse.nmr.model.core.IScanNMR;
import org.eclipse.chemclipse.nmr.model.core.ISignalFID;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import net.openchrom.process.supplier.knime.support.TableTranslator;

public class DataTableTranslator {

	private static final String SCAN_NMR_COLUMN_X = "Chemical Shift";
	private static final String SCAN_NMR_COLUMN_Y = "Intensity";
	private static final String SCAN_FID_COLUMN_X = "Time";
	private static final String SCAN_FID_COLUMN_Y_REAL = "Intensity Real Part";
	private static final String SCAN_FID_COLUMN_Y_IMAG = "Intensity Imaginary Part";

	public static DataTableSpec getBufferedDataTableSpeceNMR() {

		return TableTranslator.scanToDataTabSpac(SCAN_NMR_COLUMN_X, SCAN_NMR_COLUMN_Y);
	}

	public static BufferedDataTable getBufferedDataTableNMR(IScanNMR scanNMR, DataTableSpec dataTableSpec, ExecutionContext exec) throws CanceledExecutionException {

		return TableTranslator.scanToTable(scanNMR.getSignalsNMR(), dataTableSpec, exec);
	}

	public static DataTableSpec getBufferedDataTableSpeceFID() {

		int numberOfColumns = 3; // Column x, column y
		//
		int columnSpec = 0;
		DataColumnSpec[] dataColumnSpec = new DataColumnSpec[numberOfColumns];
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(SCAN_FID_COLUMN_X, DoubleCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(SCAN_FID_COLUMN_Y_REAL, DoubleCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(SCAN_FID_COLUMN_Y_IMAG, DoubleCell.TYPE).createSpec();
		DataTableSpec dataTableSpec = new DataTableSpec(dataColumnSpec);
		return dataTableSpec;
	}

	public static BufferedDataTable getBufferedDataTableFID(IScanFID scanFID, DataTableSpec dataTableSpec, ExecutionContext exec) throws CanceledExecutionException {

		int numberOfColumns = dataTableSpec.getNumColumns();
		BufferedDataContainer bufferedDataContainer = exec.createDataContainer(dataTableSpec);
		if(numberOfColumns != 3) {
			bufferedDataContainer.close();
			return bufferedDataContainer.getTable();
		}
		//
		int totalNumSignals = scanFID.getSignalsFID().size();
		int numSingnal = 0;
		Iterator<ISignalFID> it = scanFID.getSignalsFID().iterator();
		while(it.hasNext()) {
			int columnCell = 0;
			ISignalFID signal = it.next();
			RowKey rowKey = new RowKey(Integer.toString(numSingnal));
			DataCell[] cells = new DataCell[numberOfColumns];
			cells[columnCell++] = new DoubleCell(signal.getX());
			Complex complex = signal.getIntensity();
			cells[columnCell++] = new DoubleCell(complex.getReal());
			cells[columnCell++] = new DoubleCell(complex.getImaginary());
			DataRow dataRow = new DefaultRow(rowKey, cells);
			bufferedDataContainer.addRowToTable(dataRow);
			exec.checkCanceled();
			exec.setProgress(numSingnal / totalNumSignals, "Adding Signal: " + ++numSingnal);
		}
		//
		bufferedDataContainer.close();
		return bufferedDataContainer.getTable();
	}
}
