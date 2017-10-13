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
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.node.BufferedDataTable;

public abstract class ChoromatogramTableTranslator implements IChoromatogramTableTranslator {

	protected final String RETENTION_INDEX = "RI";
	protected final String RETENTION_TIME = "RT (milliseconds)";

	protected ChoromatogramTableTranslator() {
	}

	protected int getNumberOfRows(BufferedDataTable bufferedDataTable) {

		int counter = 0;
		CloseableRowIterator iterator = bufferedDataTable.iterator();
		while(iterator.hasNext()) {
			iterator.next();
			counter++;
		}
		return counter;
	}

	protected boolean checkRetentionTimes(BufferedDataTable bufferedDataTable, int columnNumber) {

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
		}
		return true;
	}

	protected boolean checkRetentionTimesIndexColumn(BufferedDataTable bufferedDataTable, int columnNumber) {

		if(!bufferedDataTable.getDataTableSpec().getColumnSpec(columnNumber).getType().equals(DoubleCell.TYPE)) {
			return false;
		}
		return true;
	}
}
