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
package net.openchrom.xxd.process.supplier.knime.model.scan;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.chemclipse.model.core.ISignal;
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

public class ScanTableTranslator {

	public static BufferedDataTable doubleArrayToTable(double[] data, String columnX, ExecutionContext exec) throws CanceledExecutionException {

		int numberOfColumns = 1; // Column x, column y
		//
		int columnSpec = 0;
		DataColumnSpec[] dataColumnSpec = new DataColumnSpec[numberOfColumns];
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(columnX, DoubleCell.TYPE).createSpec();
		DataTableSpec dataTableSpec = new DataTableSpec(dataColumnSpec);
		BufferedDataContainer bufferedDataContainer = exec.createDataContainer(dataTableSpec);
		//
		if(data != null) {
			int totalNumSignals = data.length;
			for(int i = 0; i < totalNumSignals; i++) {
				RowKey rowKey = new RowKey(Integer.toString(i));
				DataCell[] cells = new DataCell[numberOfColumns];
				cells[0] = new DoubleCell(data[i]);
				DataRow dataRow = new DefaultRow(rowKey, cells);
				bufferedDataContainer.addRowToTable(dataRow);
				exec.checkCanceled();
				exec.setProgress(i / totalNumSignals, "Adding Signal: " + i);
			}
		}
		//
		bufferedDataContainer.close();
		return bufferedDataContainer.getTable();
	}

	public static BufferedDataTable scanToTable(Collection<? extends ISignal> scan, String columnX, String columnY, ExecutionContext exec) throws CanceledExecutionException {

		int numberOfColumns = 2; // Column x, column y
		//
		int columnSpec = 0;
		DataColumnSpec[] dataColumnSpec = new DataColumnSpec[numberOfColumns];
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(columnX, DoubleCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(columnY, DoubleCell.TYPE).createSpec();
		DataTableSpec dataTableSpec = new DataTableSpec(dataColumnSpec);
		BufferedDataContainer bufferedDataContainer = exec.createDataContainer(dataTableSpec);
		//
		int totalNumSignals = scan.size();
		int numSingnal = 0;
		Iterator<? extends ISignal> it = scan.iterator();
		while(it.hasNext()) {
			int columnCell = 0;
			ISignal signal = it.next();
			RowKey rowKey = new RowKey(Integer.toString(numSingnal));
			DataCell[] cells = new DataCell[numberOfColumns];
			cells[columnCell++] = new DoubleCell(signal.getX());
			cells[columnCell++] = new DoubleCell(signal.getY());
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
