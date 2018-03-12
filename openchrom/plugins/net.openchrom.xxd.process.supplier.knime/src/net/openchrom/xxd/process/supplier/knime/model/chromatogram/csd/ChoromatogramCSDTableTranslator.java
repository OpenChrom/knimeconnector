/*******************************************************************************
 * Copyright (c) 2017 Jan Holy
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * jan - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.model.chromatogram.csd;

import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.csd.model.core.IScanCSD;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.csd.model.implementation.ChromatogramCSD;
import org.eclipse.chemclipse.csd.model.implementation.ScanCSD;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import net.openchrom.xxd.process.supplier.knime.model.chromatogram.TableValidation;

public class ChoromatogramCSDTableTranslator implements IChoromatogramCSDTableTranslator {

	protected final String ABUNDANCE = "Abundence";

	ChoromatogramCSDTableTranslator() {
	}

	@Override
	public BufferedDataTable getBufferedDataTable(IChromatogramSelectionCSD chromatogramSelection, final ExecutionContext exec) throws CanceledExecutionException, NoExtractedIonSignalStoredException {

		if(!(chromatogramSelection instanceof IChromatogramSelectionCSD)) {
			return null;
		}
		IChromatogramSelectionCSD chromatogramSelectionCSD = chromatogramSelection;
		/*
		 * Specification
		 */
		int numberOfColumns = 2; // RT, Abundance
		//
		int columnSpec = 0;
		DataColumnSpec[] dataColumnSpec = new DataColumnSpec[numberOfColumns];
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.RETENTION_TIME, IntCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.RETENTION_INDEX, DoubleCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(ABUNDANCE, DoubleCell.TYPE).createSpec();
		DataTableSpec dataTableSpec = new DataTableSpec(dataColumnSpec);
		/*
		 * Data
		 */
		int startRetentionTime = chromatogramSelection.getStartRetentionTime();
		int stopRetentionTime = chromatogramSelection.getStopRetentionTime();
		BufferedDataContainer bufferedDataContainer = exec.createDataContainer(dataTableSpec);
		//
		int selectedScanNumber = 0;
		for(IScan scan : chromatogramSelectionCSD.getChromatogramCSD().getScans()) {
			/*
			 * Set the cell data.
			 */
			IScanCSD scanCSD = (IScanCSD)scan;
			float totalSignal = scanCSD.getTotalSignal();
			int retentioTime = scanCSD.getRetentionTime();
			if(retentioTime >= startRetentionTime && stopRetentionTime <= retentioTime) {
				RowKey rowKey = new RowKey(Integer.toString(selectedScanNumber));
				int columnCell = 0;
				DataCell[] cells = new DataCell[numberOfColumns];
				cells[columnCell++] = new IntCell(retentioTime);
				cells[columnCell++] = new DoubleCell(totalSignal);
				DataRow dataRow = new DefaultRow(rowKey, cells);
				bufferedDataContainer.addRowToTable(dataRow);
				selectedScanNumber++;
			}
			//
			exec.checkCanceled();
			exec.setProgress("Adding Scan: " + selectedScanNumber);
		}
		//
		bufferedDataContainer.close();
		return bufferedDataContainer.getTable();
	}

	@Override
	public IChromatogramCSD getChromatogramCSD(BufferedDataTable bufferedDataTable, final ExecutionContext exec) throws Exception {

		//
		CloseableRowIterator iterator = bufferedDataTable.iterator();
		int scan = 1;
		int scanCount = TableValidation.getNumberOfRows(bufferedDataTable);
		//
		boolean check = checkTable(bufferedDataTable, exec);
		if(!check) {
			// TODO: throw any exception??
			return null;
		}
		IChromatogramCSD chromatogramCSD = new ChromatogramCSD();
		while(iterator.hasNext()) {
			DataRow dataRow = iterator.next();
			int retentionTime = Integer.parseInt(dataRow.getCell(0).toString());
			float abudance = Float.parseFloat(dataRow.getCell(1).toString());
			ScanCSD scanCSD = new ScanCSD(retentionTime, abudance);
			chromatogramCSD.addScan(scanCSD);
			//
			exec.checkCanceled();
			exec.setProgress(scan / scanCount, "Get scan from table: " + scan);
			scan++;
		}
		//
		return chromatogramCSD;
	}

	protected boolean checkTable(BufferedDataTable bufferedDataTable, final ExecutionContext exec) {

		if(bufferedDataTable.getDataTableSpec().getNumColumns() != 2) {
			return false;
		}
		return TableValidation.checkRetentionTimes(bufferedDataTable, 0) && TableValidation.chechAbundence(bufferedDataTable, 1);
	}
}
