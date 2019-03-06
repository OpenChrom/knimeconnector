/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.nmr.process.supplier.knime.ui.processor;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.nmr.model.selection.IDataNMRSelection;
import org.eclipse.chemclipse.nmr.model.support.SignalExtractor;
import org.eclipse.chemclipse.nmr.processor.core.ScanProcessorNMR;
import org.eclipse.chemclipse.nmr.processor.settings.IProcessorSettings;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.knime.base.node.util.DataArray;
import org.knime.base.node.util.DefaultDataArray;
import org.knime.base.node.viz.plotter.DataProvider;
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
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import net.openchrom.nmr.process.supplier.knime.portobject.ScanNMRPortObject;
import net.openchrom.nmr.process.supplier.knime.portobject.ScanNMRPortObjectSpec;
import net.openchrom.process.supplier.knime.ui.dialogfactory.SettingsObjectWrapper;
import net.openchrom.process.supplier.knime.ui.dialoggeneration.DialogGenerationNodeModel;
import net.openchrom.xxd.process.supplier.knime.portobject.ChromatogramSelectionCSDPortObject;

/**
 * Concatenates chromatogram filters (see {@link ChromatogramFilterPortObject}) and optionally executes them on a given chromatogram selection (see {@link ChromatogramSelectionCSDPortObject}).
 *
 * @author Martin Horn, University of Konstanz
 *
 */
public class ScanProcessorNodeModel extends DialogGenerationNodeModel<IProcessorSettings> implements DataProvider {

	public static final String DISPLAY_NMR_SPECTRUM = "Display NMR Spectrum";
	public static final String DISPLAY_FID_SIGNAL = "Display FID Signal";
	//
	private static final NodeLogger logger = NodeLogger.getLogger(ScanProcessorNodeModel.class);
	private String id;
	//
	private SettingsModelBoolean settingsDebuggingMode;
	private SettingsModelInteger settingsMaxNumberOfPoints;
	private SettingsModelString settingsDisplaySignalType;
	private DataArray dataTable;
	//

	static SettingsModelBoolean getSettingsDebuggingMode() {

		return new SettingsModelBoolean("activateDebugingMode", false);
	}

	static SettingsModelInteger getSettingsMaxNumberOfPoints() {

		return new SettingsModelIntegerBounded("maximalNumberOfPoins", 2500, 1, Integer.MAX_VALUE);
	}

	static SettingsModelString getSettingsDisplaySignalType() {

		return new SettingsModelString("signalType", DISPLAY_NMR_SPECTRUM);
	}

	ScanProcessorNodeModel(String id, SettingsObjectWrapper<IProcessorSettings> so) {

		super(new PortType[]{ScanNMRPortObject.TYPE}, new PortType[]{ScanNMRPortObject.TYPE}, so);
		this.id = id;
		this.settingsDebuggingMode = getSettingsDebuggingMode();
		this.settingsMaxNumberOfPoints = getSettingsMaxNumberOfPoints();
		this.settingsDisplaySignalType = getSettingsDisplaySignalType();
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return new PortObjectSpec[]{new ScanNMRPortObjectSpec()};
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		ScanNMRPortObject scanNMRPortObject = (ScanNMRPortObject)inObjects[0];
		IDataNMRSelection scanNMR = scanNMRPortObject.getScanNMR();
		SignalExtractor signalExtractor = new SignalExtractor(scanNMR);
		//
		double[] chemicalShift = null;
		long[] time = null;
		double[] signalFIDUnprocessed = null;
		double[] spectrumUnPreprocessed = null;
		if(settingsDebuggingMode.getBooleanValue()) {
			signalFIDUnprocessed = signalExtractor.extractIntensityProcessedFIDReal();
			spectrumUnPreprocessed = signalExtractor.extractSignalIntesity();
		}
		/*
		 * Apply the filter if a chromatogram selection is given at port 0.
		 */
		ScanProcessorNMR.process(scanNMR, getSettingsObject(), id, new NullProgressMonitor());
		//
		if(settingsDebuggingMode.getBooleanValue()) {
			time = signalExtractor.extractAcquisitionTimeFID();
			chemicalShift = signalExtractor.extractChemicalShift();
			double[] signalFID = signalExtractor.extractIntensityProcessedFIDReal();
			double[] spectrumNMR = signalExtractor.extractSignalIntesity();
			if(settingsDisplaySignalType.getStringValue().equals(DISPLAY_FID_SIGNAL)) {
				setBufferedDataTableFID(time, signalFIDUnprocessed, signalFID, exec);
			} else if(settingsDisplaySignalType.getStringValue().equals(DISPLAY_NMR_SPECTRUM)) {
				setBufferedDataTableNMR(chemicalShift, spectrumUnPreprocessed, spectrumNMR, exec);
			} else {
				setEmptyDataTable(exec);
			}
		} else {
			setEmptyDataTable(exec);
		}
		/*
		 * Store applied chromatogram filter and it's settings
		 */
		return new PortObject[]{scanNMRPortObject};
	}

	private void setBufferedDataTableNMR(double[] chemicalShift, double[] spectrumUnPreprocessed, double[] spectrumNMR, ExecutionContext exec) {

		int maximalNumeber = settingsMaxNumberOfPoints.getIntValue();
		int step = (int)Math.floor(chemicalShift.length / maximalNumeber);
		//
		int lengthChemicalShift = chemicalShift.length;
		boolean includeSpectrumUnPreprocessed = lengthChemicalShift == spectrumUnPreprocessed.length;
		boolean includeSpectrumPreprocessed = lengthChemicalShift == spectrumNMR.length;
		int numColumns = 0;
		if(includeSpectrumUnPreprocessed) {
			numColumns++;
		}
		if(includeSpectrumPreprocessed) {
			numColumns++;
		}
		DataColumnSpec[] dataColumnSpec = new DataColumnSpec[numColumns];
		int column = 0;
		if(includeSpectrumUnPreprocessed) {
			dataColumnSpec[column] = new DataColumnSpecCreator("Before processing", DoubleCell.TYPE).createSpec();
			column++;
		}
		if(includeSpectrumPreprocessed) {
			dataColumnSpec[column] = new DataColumnSpecCreator("After processing", DoubleCell.TYPE).createSpec();
			column++;
		}
		DataTableSpec dataTableSpec = new DataTableSpec("NMR Spectrum", dataColumnSpec);
		BufferedDataContainer bufferedDataContainer = exec.createDataContainer(dataTableSpec);
		//
		for(int i = 0; i < chemicalShift.length; i += step) {
			RowKey rowKey = new RowKey(Double.toString(chemicalShift[i]));
			DataCell[] cells = new DataCell[numColumns];
			int columnCell = 0;
			if(includeSpectrumUnPreprocessed) {
				cells[columnCell++] = new DoubleCell(spectrumUnPreprocessed[i]);
			}
			if(includeSpectrumPreprocessed) {
				cells[columnCell++] = new DoubleCell(spectrumNMR[i]);
			}
			DataRow dataRow = new DefaultRow(rowKey, cells);
			bufferedDataContainer.addRowToTable(dataRow);
		}
		bufferedDataContainer.close();
		BufferedDataTable bufferDataTable = bufferedDataContainer.getTable();
		dataTable = new DefaultDataArray(bufferDataTable, 1, (int)bufferDataTable.size());
	}

	private void setEmptyDataTable(ExecutionContext exec) {

		//
		DataColumnSpec[] dataColumnSpec = new DataColumnSpec[0];
		DataTableSpec dataTableSpec = new DataTableSpec(dataColumnSpec);
		BufferedDataContainer bufferedDataContainer = exec.createDataContainer(dataTableSpec);
		bufferedDataContainer.close();
		BufferedDataTable bufferDataTable = bufferedDataContainer.getTable();
		dataTable = new DefaultDataArray(bufferDataTable, 1, (int)bufferDataTable.size());
	}

	private void setBufferedDataTableFID(long[] time, double[] signalFIDUnprocessed, double[] signalFID, ExecutionContext exec) {

		int maximalNumeber = settingsMaxNumberOfPoints.getIntValue();
		int step = (int)Math.floor(time.length / maximalNumeber);
		//
		int lengthTime = time.length;
		boolean includeSignalFIDUnprocessed = lengthTime == signalFIDUnprocessed.length;
		boolean includeSignalFIDprocessed = lengthTime == signalFID.length;
		int numColumns = 0;
		if(includeSignalFIDUnprocessed) {
			numColumns++;
		}
		if(includeSignalFIDprocessed) {
			numColumns++;
		}
		DataColumnSpec[] dataColumnSpec = new DataColumnSpec[numColumns];
		int column = 0;
		if(includeSignalFIDUnprocessed) {
			dataColumnSpec[column] = new DataColumnSpecCreator("Before processing", DoubleCell.TYPE).createSpec();
			column++;
		}
		if(includeSignalFIDprocessed) {
			dataColumnSpec[column] = new DataColumnSpecCreator("After processing", DoubleCell.TYPE).createSpec();
			column++;
		}
		DataTableSpec dataTableSpec = new DataTableSpec("FID Signal", dataColumnSpec);
		BufferedDataContainer bufferedDataContainer = exec.createDataContainer(dataTableSpec);
		//
		int i = 0;
		for(i = 0; i < time.length; i += step) {
			RowKey rowKey = new RowKey(Long.toString(time[i]) + " ns");
			DataCell[] cells = new DataCell[numColumns];
			int columnCell = 0;
			if(includeSignalFIDUnprocessed) {
				cells[columnCell++] = new DoubleCell(signalFIDUnprocessed[i]);
			}
			if(includeSignalFIDprocessed) {
				cells[columnCell++] = new DoubleCell(signalFID[i]);
			}
			DataRow dataRow = new DefaultRow(rowKey, cells);
			bufferedDataContainer.addRowToTable(dataRow);
		}
		bufferedDataContainer.close();
		BufferedDataTable bufferDataTable = bufferedDataContainer.getTable();
		dataTable = new DefaultDataArray(bufferDataTable, 1, (int)bufferDataTable.size());
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {

		super.loadValidatedSettingsFrom(settings);
		settingsDebuggingMode.loadSettingsFrom(settings);
		settingsMaxNumberOfPoints.loadSettingsFrom(settings);
		settingsDisplaySignalType.loadSettingsFrom(settings);
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {

		super.saveSettingsTo(settings);
		settingsDebuggingMode.saveSettingsTo(settings);
		settingsMaxNumberOfPoints.saveSettingsTo(settings);
		settingsDisplaySignalType.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

		super.validateSettings(settings);
		settingsDebuggingMode.validateSettings(settings);
		settingsMaxNumberOfPoints.validateSettings(settings);
		settingsDisplaySignalType.validateSettings(settings);
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {

	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	public DataArray getDataArray(int index) {

		return dataTable;
	}
}
