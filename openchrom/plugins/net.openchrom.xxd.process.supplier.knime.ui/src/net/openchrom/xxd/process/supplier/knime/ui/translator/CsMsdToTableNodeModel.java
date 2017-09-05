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
package net.openchrom.xxd.process.supplier.knime.ui.translator;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignals;
import org.eclipse.chemclipse.msd.model.xic.ITotalIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.TotalIonSignalExtractor;
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
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionMSDPortObject;
import net.openchrom.xxd.process.supplier.knime.model.PortObjectSupport;

public class CsMsdToTableNodeModel extends NodeModel {

	private static final NodeLogger logger = NodeLogger.getLogger(CsMsdToTableNodeModel.class);
	//
	protected static final String USE_TIC = "Use TIC";
	protected static final boolean DEF_USE_TIC = false;
	//
	private static final String RETENTION_TIME = "RT (milliseconds)";
	private static final String RETENTION_INDEX = "RI";
	private static final String TIC = "TIC";

	protected static SettingsModelBoolean createSettingsModelUseTic() {

		return new SettingsModelBoolean(USE_TIC, DEF_USE_TIC);
	}

	private final SettingsModelBoolean settingsModelUseTic = createSettingsModelUseTic();

	protected CsMsdToTableNodeModel() {
		super(new PortType[]{PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionMSDPortObject.class)}, new PortType[]{PortTypeRegistry.getInstance().getPortType(BufferedDataTable.class)});
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		ChromatogramSelectionMSDPortObject chromatogramSelectionMSDPortObject = PortObjectSupport.getChromatogramSelectionMSDPortObject(inObjects);
		if(chromatogramSelectionMSDPortObject != null) {
			/*
			 * Convert the selection to table.
			 */
			logger.info("Convert chromatogram selection to table.");
			IChromatogramSelectionMSD chromatogramSelection = chromatogramSelectionMSDPortObject.getChromatogramSelectionMSD();
			BufferedDataTable bufferedDataTable = null;
			if(settingsModelUseTic.getBooleanValue()) {
				bufferedDataTable = getBufferedDataTableTIC(chromatogramSelection, exec);
			} else {
				bufferedDataTable = getBufferedDataTableXIC(chromatogramSelection, exec);
			}
			//
			return new PortObject[]{bufferedDataTable};
		} else {
			/*
			 * If things have gone wrong.
			 */
			return new PortObject[]{};
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {

	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		settingsModelUseTic.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

		settingsModelUseTic.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		settingsModelUseTic.validateSettings(settings);
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
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(RETENTION_TIME, IntCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(RETENTION_INDEX, DoubleCell.TYPE).createSpec();
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
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(RETENTION_TIME, IntCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(RETENTION_INDEX, DoubleCell.TYPE).createSpec();
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
		return bufferedDataContainer.getTable();
	}
}
