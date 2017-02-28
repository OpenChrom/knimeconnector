/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 * 
 * This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.io.msd;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.msd.converter.chromatogram.ChromatogramConverterMSD;
import org.eclipse.chemclipse.msd.converter.processing.chromatogram.IChromatogramMSDImportConverterProcessingInfo;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignals;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class ChromatogramReaderMSDNodeModel extends NodeModel {

	private static final NodeLogger logger = NodeLogger.getLogger(ChromatogramReaderMSDNodeModel.class);
	//
	private static final String CHROMATOGRAM_FILE_INPUT = "ChromatgramFileInput";
	protected static final SettingsModelString SETTING_CHROMATOGRAM_FILE_INPUT = new SettingsModelString(CHROMATOGRAM_FILE_INPUT, "");
	//
	private static final String RT = "RT (milliseconds)";
	private static final String RI = "RI";

	protected ChromatogramReaderMSDNodeModel() {
		super(0, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec) throws Exception {

		logger.info("Read the chromatographic raw data.");
		/*
		 * Get the chromatogram.
		 */
		IChromatogramMSD chromatogramMSD = loadChromatogram(SETTING_CHROMATOGRAM_FILE_INPUT.getStringValue());
		BufferedDataTable bufferedDataTable = null;
		if(chromatogramMSD != null) {
			/*
			 * Specification
			 */
			IExtractedIonSignalExtractor extractedIonSignalExtractor = new ExtractedIonSignalExtractor(chromatogramMSD);
			IExtractedIonSignals extractedIonSignals = extractedIonSignalExtractor.getExtractedIonSignals();
			int startIon = extractedIonSignals.getStartIon();
			int stopIon = extractedIonSignals.getStopIon();
			int numberOfColumns = 2 + (stopIon - startIon + 1); // RT, RI, m/z values
			//
			int columnSpec = 0;
			DataColumnSpec[] dataColumnSpec = new DataColumnSpec[numberOfColumns];
			dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(RT, IntCell.TYPE).createSpec();
			dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(RI, DoubleCell.TYPE).createSpec();
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
			bufferedDataTable = bufferedDataContainer.getTable();
		}
		/*
		 * Ready, return the table.
		 */
		if(bufferedDataTable != null) {
			return new BufferedDataTable[]{bufferedDataTable};
		} else {
			return new BufferedDataTable[]{};
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {

		return new DataTableSpec[]{null};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		SETTING_CHROMATOGRAM_FILE_INPUT.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

		SETTING_CHROMATOGRAM_FILE_INPUT.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		SETTING_CHROMATOGRAM_FILE_INPUT.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	private IChromatogramMSD loadChromatogram(String pathChromatogram) {

		File file = new File(pathChromatogram);
		IChromatogramMSDImportConverterProcessingInfo processingInfo = ChromatogramConverterMSD.convert(file, new NullProgressMonitor());
		return processingInfo.getChromatogram();
	}
}
