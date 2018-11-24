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
package net.openchrom.process.supplier.knime.support;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.chemclipse.model.core.IMeasurementInfo;
import org.eclipse.chemclipse.model.core.ISignal;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

public class TableTranslator {

	private static void createRow(String name, String value, int key, BufferedDataContainer bufferedDataContainer) {

		value = value == null ? "" : value;
		int columnCell = 0;
		RowKey rowKey = new RowKey(Integer.toString(key));
		DataCell[] cells = new DataCell[2];
		cells[columnCell++] = new StringCell(name);
		cells[columnCell++] = new StringCell(value);
		DataRow dataRow = new DefaultRow(rowKey, cells);
		bufferedDataContainer.addRowToTable(dataRow);
	}

	private static void createRow(String name, double value, int key, BufferedDataContainer bufferedDataContainer) {

		int columnCell = 0;
		RowKey rowKey = new RowKey(Integer.toString(key));
		DataCell[] cells = new DataCell[2];
		cells[columnCell++] = new StringCell(name);
		cells[columnCell++] = new StringCell(Double.toString(value));
		DataRow dataRow = new DefaultRow(rowKey, cells);
		bufferedDataContainer.addRowToTable(dataRow);
	}

	private static void createRow(String name, int value, int key, BufferedDataContainer bufferedDataContainer) {

		int columnCell = 0;
		RowKey rowKey = new RowKey(Integer.toString(key));
		DataCell[] cells = new DataCell[2];
		cells[columnCell++] = new StringCell(name);
		cells[columnCell++] = new StringCell(Integer.toString(value));
		DataRow dataRow = new DefaultRow(rowKey, cells);
		bufferedDataContainer.addRowToTable(dataRow);
	}

	public static DataTableSpec headerTranslatorTableSpec() {

		int numberOfColumns = 2; // Column x, column y
		//
		int columnSpec = 0;
		DataColumnSpec[] dataColumnSpec = new DataColumnSpec[numberOfColumns];
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator("Name", StringCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator("Value", StringCell.TYPE).createSpec();
		return new DataTableSpec(dataColumnSpec);
	}

	public static BufferedDataTable headerTranslator(IMeasurementInfo measurmentInfo, DataTableSpec dataTableSpec, ExecutionContext exec) throws CanceledExecutionException {

		int numberOfColumns = dataTableSpec.getNumColumns();
		BufferedDataContainer bufferedDataContainer = exec.createDataContainer(dataTableSpec);
		if(numberOfColumns != 2) {
			bufferedDataContainer.close();
			return bufferedDataContainer.getTable();
		}
		//
		int numRow = 0;
		createRow("Operator", measurmentInfo.getOperator(), numRow++, bufferedDataContainer);
		createRow("Date", measurmentInfo.getDate().toString(), numRow++, bufferedDataContainer);
		createRow("Short Info", measurmentInfo.getShortInfo(), numRow++, bufferedDataContainer);
		createRow("Detail Info", measurmentInfo.getDetailedInfo(), numRow++, bufferedDataContainer);
		createRow("Sample group", measurmentInfo.getSampleGroup(), numRow++, bufferedDataContainer);
		createRow("Barcode", measurmentInfo.getBarcode(), numRow++, bufferedDataContainer);
		createRow("Sample Weight", measurmentInfo.getSampleWeight(), numRow++, bufferedDataContainer);
		createRow("Weight Unit", measurmentInfo.getSampleWeightUnit(), numRow++, bufferedDataContainer);
		createRow("Date Name", measurmentInfo.getDataName(), numRow++, bufferedDataContainer);
		Iterator<Entry<String, String>> it = measurmentInfo.getHeaderDataMap().entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, String> e = it.next();
			createRow(e.getKey(), e.getValue(), numRow++, bufferedDataContainer);
		}
		//
		bufferedDataContainer.close();
		return bufferedDataContainer.getTable();
	}

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

	public static DataTableSpec scanToDataTabSpac(String columnX, String columnY) {

		int numberOfColumns = 2; // Column x, column y
		//
		int columnSpec = 0;
		DataColumnSpec[] dataColumnSpec = new DataColumnSpec[numberOfColumns];
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(columnX, DoubleCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(columnY, DoubleCell.TYPE).createSpec();
		DataTableSpec dataTableSpec = new DataTableSpec(dataColumnSpec);
		return dataTableSpec;
	}

	public static BufferedDataTable scanToTable(Collection<? extends ISignal> scan, DataTableSpec dataTableSpec, ExecutionContext exec) throws CanceledExecutionException {

		int numberOfColumns = dataTableSpec.getNumColumns();
		BufferedDataContainer bufferedDataContainer = exec.createDataContainer(dataTableSpec);
		if(numberOfColumns != 2) {
			bufferedDataContainer.close();
			return bufferedDataContainer.getTable();
		}
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

	public static BufferedDataTable filesToTable(Collection<File> files, ExecutionContext exec) {

		DataColumnSpec[] dcs = new DataColumnSpec[3];
		dcs[0] = new DataColumnSpecCreator("Location", StringCell.TYPE).createSpec();
		dcs[1] = new DataColumnSpecCreator("URL", StringCell.TYPE).createSpec();
		dcs[2] = new DataColumnSpecCreator("File Name", StringCell.TYPE).createSpec();
		DataTableSpec dataTableSpec = new DataTableSpec(dcs);
		BufferedDataContainer bufferedDataContainer = exec.createDataContainer(dataTableSpec);
		int rowNumber = 1;
		for(File file : files) {
			DataCell[] row = new DataCell[3];
			row[0] = new StringCell(file.getAbsolutePath());
			try {
				row[1] = new StringCell(file.getAbsoluteFile().toURI().toURL().toString());
			} catch(MalformedURLException e) {
				row[1] = new StringCell("");
			}
			row[2] = new StringCell(file.getName());
			bufferedDataContainer.addRowToTable(new DefaultRow("Row " + rowNumber, row));
			rowNumber++;
		}
		bufferedDataContainer.close();
		return bufferedDataContainer.getTable();
	}
}
