/*******************************************************************************
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
package net.openchrom.xxd.process.supplier.knime.model.table.msd;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.ScanMSD;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignals;
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

import net.openchrom.xxd.process.supplier.knime.model.table.TableValidation;

public class ChoromatogramMSDTableTranslatorXIC implements IChoromatogramMSDTableTranslator {

	public ChoromatogramMSDTableTranslatorXIC() {

		super();
	}

	@Override
	public BufferedDataTable getBufferedDataTable(IChromatogramSelectionMSD chromatogramSelection, ExecutionContext exec) throws CanceledExecutionException, NoExtractedIonSignalStoredException {

		return getBufferedDataTableXIC(chromatogramSelection, exec);
	}

	private BufferedDataTable getBufferedDataTableXIC(IChromatogramSelectionMSD chromatogramSelection, final ExecutionContext exec) throws CanceledExecutionException, NoExtractedIonSignalStoredException {

		/*
		 * Specification
		 */
		IChromatogramMSD chromatogramMSD = chromatogramSelection.getChromatogramMSD();
		IExtractedIonSignalExtractor extractedIonSignalExtractor = new ExtractedIonSignalExtractor(chromatogramMSD);
		IExtractedIonSignals extractedIonSignals = extractedIonSignalExtractor.getExtractedIonSignals(chromatogramSelection);
		int startIon = extractedIonSignals.getStartIon();
		int stopIon = extractedIonSignals.getStopIon();
		int numberOfColumns = 2 + (stopIon - startIon + 1); // RT, RI, m/z values
		//
		int columnSpec = 0;
		DataColumnSpec[] dataColumnSpec = new DataColumnSpec[numberOfColumns];
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.RETENTION_TIME, IntCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.RETENTION_INDEX, DoubleCell.TYPE).createSpec();
		for(int ion = startIon; ion <= stopIon; ion++) {
			dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(Integer.toString(ion), DoubleCell.TYPE).createSpec();
		}
		DataTableSpec dataTableSpec = new DataTableSpec(dataColumnSpec);
		/*
		 * Data
		 */
		int startScan = extractedIonSignals.getStartScan();
		int stopScan = extractedIonSignals.getStopScan();
		int scanCount = stopScan - startScan + 1;
		BufferedDataContainer bufferedDataContainer = exec.createDataContainer(dataTableSpec);
		//
		for(int scan = startScan; scan <= stopScan; scan++) {
			/*
			 * getBufferedDataTableTICgetBufferedDataTableTIC
			 * Set the cell data.
			 */
			IExtractedIonSignal extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
			RowKey rowKey = new RowKey(Integer.toString(scan));
			int columnCell = 0;
			DataCell[] cells = new DataCell[numberOfColumns];
			cells[columnCell++] = new IntCell(extractedIonSignal.getRetentionTime());
			cells[columnCell++] = new DoubleCell(extractedIonSignal.getRetentionIndex());
			for(int ion = startIon; ion <= stopIon; ion++) {
				cells[columnCell++] = new DoubleCell(extractedIonSignal.getAbundance(ion));
			}
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
		IChromatogramMSD chromatogramMSD = new ChromatogramMSD();
		while(iterator.hasNext()) {
			DataRow dataRow = iterator.next();
			//
			IScanMSD vendorScanMSD = new ScanMSD();
			vendorScanMSD.setRetentionTime(Integer.parseInt(dataRow.getCell(0).toString()));
			vendorScanMSD.setRetentionIndex(Float.parseFloat(dataRow.getCell(1).toString()));
			//
			int numberOfCells = dataRow.getNumCells();
			/*
			 * XIC
			 */
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
						vendorScanMSD.addIon(new Ion(mz, abundance));
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

	protected boolean checkAbudance(BufferedDataTable bufferedDataTable, int startColumnNumber, int numberOfColumn) {

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

	private boolean checkAbudanceXIC(BufferedDataTable bufferedDataTable, int startColumnNumber, int numberOfColumn) {

		for(int i = 0; i < numberOfColumn; i++) {
			String columnName = bufferedDataTable.getDataTableSpec().getColumnSpec(startColumnNumber + i).getName();
			try {
				Integer.parseInt(columnName);
				// TODO: have to be sorted or shell I sorted it ?
			} catch(Exception e) {
				return false;
			}
		}
		return checkAbudance(bufferedDataTable, startColumnNumber, numberOfColumn);
	}

	@Override
	public boolean checkTable(BufferedDataTable bufferedDataTable, final ExecutionContext exec) {

		// test column
		DataTableSpec dataTableSpec = bufferedDataTable.getDataTableSpec();
		int numColumns = dataTableSpec.getNumColumns();
		if(numColumns >= 3) {
			return TableValidation.checkRetentionTimes(bufferedDataTable, 0) && TableValidation.checkRetentionTimesIndexColumn(bufferedDataTable, 1) && checkAbudanceXIC(bufferedDataTable, 2, numColumns - 2);
		}
		return false;
	}
}
