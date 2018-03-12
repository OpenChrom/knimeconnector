/*******************************************************************************
 * Copyright (c) 2017 Jan Holy.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.model.chromatogram;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.node.BufferedDataTable;

public class TableValidation {

	public static final String RETENTION_INDEX = "RI";
	public static final String RETENTION_TIME = "RT (milliseconds)";
	public static final String RETENTION_TIME_MIN = "RT (min)";
	public static final String PEAK_IS_ACTIVE_FOR_ANALYSIS = "Active for Analysis";
	public static final String PEAK_AREA = "Area";
	public static final String PEAK_START_RETENTION_TIME = "Start RT";
	public static final String PEAK_STOP_RETENTION_TIME = "Stop RT";
	public static final String PEAK_WIDTH = "Width";
	public static final String PEAK_SCAN_NUMBER_AT_PEAK_MAXIMUM = "Scan# at Peak Maximum";
	public static final String PEAK_SIGNAL_TO_NOISE_RETION = "S/N";
	public static final String PEAK_LEADING = "Leading";
	public static final String PEAK_TAILING = "Tailing";
	public static final String PEAK_MODEL_DESCRIPTION = "Model Description";
	public static final String PEAK_SUGGERSTION_COMPONENTS = "Suggested Components";
	public static final String PEAK_NAME = "Name";

	protected TableValidation() {
	}

	public static int getNumberOfRows(BufferedDataTable bufferedDataTable) {

		int counter = 0;
		CloseableRowIterator iterator = bufferedDataTable.iterator();
		while(iterator.hasNext()) {
			iterator.next();
			counter++;
		}
		return counter;
	}

	public static boolean checkRetentionTimes(BufferedDataTable bufferedDataTable, int columnNumber) {

		if(!bufferedDataTable.getDataTableSpec().getColumnSpec(columnNumber).getType().equals(IntCell.TYPE)) {
			return false;
		}
		CloseableRowIterator iterator = bufferedDataTable.iterator();
		if(iterator.hasNext()) {
			DataCell cell = iterator.next().getCell(columnNumber);
			if(cell.isMissing()) {
				iterator.close();
				return false;
			}
			try {
				int retentionTimeOld = Integer.parseInt(cell.toString());
				if(retentionTimeOld < 0) {
					iterator.close();
					return false;
				}
				while(iterator.hasNext()) {
					cell = iterator.next().getCell(columnNumber);
					if(cell.isMissing()) {
						iterator.close();
						return false;
					}
					int retentionTimeNew = Integer.parseInt(cell.toString());
					if(retentionTimeOld > retentionTimeNew) {
						iterator.close();
						return false;
					}
					retentionTimeOld = retentionTimeNew;
				}
			} catch(NumberFormatException e) {
				return false;
			}
		}
		return true;
	}

	public static boolean checkAbudanceNotNegative(BufferedDataTable bufferedDataTable, int startColumnNumber, int numberOfColumn) {

		for(int i = 0; i < numberOfColumn; i++) {
			if(!bufferedDataTable.getDataTableSpec().getColumnSpec(startColumnNumber + i).getType().equals(DoubleCell.TYPE)) {
				return false;
			}
		}
		CloseableRowIterator iterator = bufferedDataTable.iterator();
		while(iterator.hasNext()) {
			DataRow row = iterator.next();
			for(int i = 0; i < numberOfColumn; i++) {
				DataCell cell = row.getCell(startColumnNumber + i);
				if(cell.isMissing()) {
					iterator.close();
					return false;
				}
				double abundance = Double.parseDouble(cell.toString());
				if(abundance < 0) {
					iterator.close();
					return false;
				}
			}
		}
		return true;
	}

	public static boolean chechAbundence(BufferedDataTable bufferedDataTable, int columnNumber) {

		if(bufferedDataTable.getDataTableSpec().getColumnSpec(columnNumber).getType() != DoubleCell.TYPE) {
			return false;
		} else {
			CloseableRowIterator iterator = bufferedDataTable.iterator();
			if(iterator.hasNext()) {
				DataCell cell = iterator.next().getCell(columnNumber);
				if(cell.isMissing()) {
					iterator.close();
					return false;
				}
			}
			return true;
		}
	}

	public static boolean checkRetentionTimesIndexColumn(BufferedDataTable bufferedDataTable, int columnNumber) {

		if(!bufferedDataTable.getDataTableSpec().getColumnSpec(columnNumber).getType().equals(DoubleCell.TYPE)) {
			return false;
		}
		return true;
	}
}
