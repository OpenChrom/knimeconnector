/*******************************************************************************
 * 
 * Copyright (c) 2017 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.model.chromatogram.msd;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.eclipse.chemclipse.msd.model.xic.ITotalIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.TotalIonSignalExtractor;
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

public class ChoromatogramMSDTableTranslatorTIC implements IChoromatogramMSDTableTranslator {

	private static final String TIC = "TIC";

	public ChoromatogramMSDTableTranslatorTIC() {
		super();
	}

	@Override
	public BufferedDataTable getBufferedDataTable(IChromatogramSelectionMSD chromatogramSelection, ExecutionContext exec) throws CanceledExecutionException, NoExtractedIonSignalStoredException {

		return getBufferedDataTableTIC(chromatogramSelection, exec);
	}

	private BufferedDataTable getBufferedDataTableTIC(IChromatogramSelectionMSD chromatogramSelection, final ExecutionContext exec) throws CanceledExecutionException, NoExtractedIonSignalStoredException {

		/*
		 * Specification
		 */
		IChromatogramMSD chromatogramMSD = chromatogramSelection.getChromatogramMSD();
		ITotalIonSignalExtractor totalIonSignalExtractor = new TotalIonSignalExtractor(chromatogramMSD);
		ITotalScanSignals totalScanSignals = totalIonSignalExtractor.getTotalScanSignals(chromatogramSelection);
		int numberOfColumns = 3; // RT, RI, TIC
		//
		int columnSpec = 0;
		DataColumnSpec[] dataColumnSpec = new DataColumnSpec[numberOfColumns];
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.RETENTION_TIME, IntCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.RETENTION_INDEX, DoubleCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TIC, DoubleCell.TYPE).createSpec();
		DataTableSpec dataTableSpec = new DataTableSpec(dataColumnSpec);
		/*
		 * Data
		 */
		int startScan = totalScanSignals.getStartScan();
		int stopScan = totalScanSignals.getStopScan();
		int scanCount = stopScan - startScan + 1;
		BufferedDataContainer bufferedDataContainer = exec.createDataContainer(dataTableSpec);
		//
		for(int scan = startScan; scan <= stopScan; scan++) {
			/*
			 * Set the cell data.
			 */
			ITotalScanSignal totalScanSignal = totalScanSignals.getTotalScanSignal(scan);
			RowKey rowKey = new RowKey(Integer.toString(scan));
			int columnCell = 0;
			DataCell[] cells = new DataCell[numberOfColumns];
			cells[columnCell++] = new IntCell(totalScanSignal.getRetentionTime());
			cells[columnCell++] = new DoubleCell(totalScanSignal.getRetentionIndex());
			cells[columnCell++] = new DoubleCell(totalScanSignal.getTotalSignal());
			DataRow dataRow = new DefaultRow(rowKey, cells);
			bufferedDataContainer.addRowToTable(dataRow);
			//
			exec.checkCanceled();
			exec.setProgress(scan / scanCount, "Adding Scan: " + scan);
		}
		//
		bufferedDataContainer.close();
		return bufferedDataContainer.getTable();
	}

	@Override
	public IChromatogramMSD getChromatogram(BufferedDataTable bufferedDataTable, final ExecutionContext exec) throws Exception {

		DataTableSpec dataTableSpec = bufferedDataTable.getSpec();
		Map<Integer, Double> mzTable = new HashMap<Integer, Double>();
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
		IVendorChromatogramMSD chromatogramMSD = new VendorChromatogramMSD();
		while(iterator.hasNext()) {
			DataRow dataRow = iterator.next();
			//
			IVendorScanMSD vendorScanMSD = new VendorScanMSD();
			vendorScanMSD.setRetentionTime(Integer.parseInt(dataRow.getCell(0).toString()));
			vendorScanMSD.setRetentionIndex(Float.parseFloat(dataRow.getCell(1).toString()));
			//
			int numberOfCells = dataRow.getNumCells();
			for(int i = 2; i < numberOfCells; i++) {
				float abundance = Float.parseFloat(dataRow.getCell(i).toString());
				if(abundance > 0) {
					double mz;
					if(mzTable.containsKey(i)) {
						mz = mzTable.get(i);
					} else {
						mz = Double.parseDouble(dataTableSpec.getColumnSpec(i).getName().toString());
						mzTable.put(i, mz);
					}
					/*
					 * Add the ion.
					 */
					if(mz > 0) {
						vendorScanMSD.addIon(new VendorIon(mz, abundance));
					}
				}
			}
			chromatogramMSD.addScan(vendorScanMSD);
			//
			exec.checkCanceled();
			exec.setProgress(scan / scanCount, "Get scan from table: " + scan);
			scan++;
		}
		//
		return chromatogramMSD;
	}

	private boolean checkAbudanceTIC(BufferedDataTable bufferedDataTable, int startColumnNumber) {

		String columnName = bufferedDataTable.getDataTableSpec().getColumnSpec(startColumnNumber).getName();
		try {
			Integer.parseInt(columnName);
			return false;
		} catch(Exception e) {
		}
		return TableValidation.checkAbudanceNotNegative(bufferedDataTable, startColumnNumber, 1);
	}

	@Override
	public boolean checkTable(BufferedDataTable bufferedDataTable, final ExecutionContext exec) {

		// test column
		DataTableSpec dataTableSpec = bufferedDataTable.getDataTableSpec();
		int numColumns = dataTableSpec.getNumColumns();
		if(numColumns == 3) {
			return TableValidation.checkRetentionTimes(bufferedDataTable, 0) && TableValidation.checkRetentionTimesIndexColumn(bufferedDataTable, 1) && checkAbudanceTIC(bufferedDataTable, 2);
		}
		return false;
	}
}
