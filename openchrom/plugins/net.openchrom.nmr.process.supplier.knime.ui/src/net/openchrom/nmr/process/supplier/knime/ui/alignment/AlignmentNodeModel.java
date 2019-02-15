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
package net.openchrom.nmr.process.supplier.knime.ui.alignment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.nmr.model.core.IMeasurementNMR;
import org.eclipse.chemclipse.nmr.model.support.SignalExtractor;
import org.ejml.simple.SimpleMatrix;
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
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleRange;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.workflow.LoopEndNode;
import org.knime.core.node.workflow.LoopStartNode;
import org.knime.core.node.workflow.LoopStartNodeTerminator;

import net.openchrom.nmr.process.supplier.knime.portobject.PortObjectSupport;
import net.openchrom.nmr.process.supplier.knime.portobject.ScanNMRPortObject;
import net.openchrom.nmr.processing.supplier.base.core.IcoShiftAlignment;
import net.openchrom.nmr.processing.supplier.base.core.IcoShiftAlignment.AlignmentType;
import net.openchrom.nmr.processing.supplier.base.core.IcoShiftAlignment.GapFillingType;
import net.openchrom.nmr.processing.supplier.base.core.IcoShiftAlignment.ShiftCorrectionType;
import net.openchrom.nmr.processing.supplier.base.core.IcoShiftAlignment.TargetCalculationSelection;
import net.openchrom.nmr.processing.supplier.base.core.IcoShiftAlignmentSettings;

/**
 * Node model for the "Apply Filters"-node.
 *
 * @author Martin Horn, University of Konstanz
 *
 */
public class AlignmentNodeModel extends NodeModel implements LoopEndNode {

	//
	private static final String TARGET_CALCULATION_SELECTION = "TARGET_CALCULATION_SELECTION";
	private static final String GAP_FILLING_TYPE = "GAP_FILLING_TYPE";
	private static final String ALIGNMENT_TYPE = "ALIGNMENT_TYPE";
	private static final String SINGLE_PEAK_BORDER = "SINGLE_PEAK_LOWER_BORDER";
	private static final String NUMBER_OF_INTERVALS = "NUMBER_OF_INTERVALS";
	private static final String SHIFT_CORRECTION_TYPE = "SHIFT_CORRECTION_TYPE";
	private static final String SHIFT_CORRECTION_TYPE_VALUE = "SHIFT_CORRECTION_TYPE_VALUE";
	private static final String INTERVAL_LENGHT = "INTERVAL_LENGHT";
	//
	private List<IMeasurementNMR> measurements = new ArrayList<>();
	private SettingsModelString settingsTargetCalcutaltionSelection;
	private SettingsModelString settingsGapFillingType;
	private SettingsModelString settingsAligmentType;
	private SettingsModelDouble settingsSinglePeakLowerBorder;
	private SettingsModelDouble settingsSingleHigherBorder;
	private SettingsModelInteger settingsNumberOfIntervals;
	private SettingsModelString settingsShiftCorrectionType;
	private SettingsModelInteger settingsShiftCorrectionTypeValue;
	private SettingsModelDouble settingsIntervalLenght;

	static SettingsModelString getSettingsTargetCalcutaltionSelection() {

		return new SettingsModelString(TARGET_CALCULATION_SELECTION, TargetCalculationSelection.MEAN.name());
	}

	static SettingsModelString getSettingsGapFillingType() {

		return new SettingsModelString(GAP_FILLING_TYPE, GapFillingType.MARGIN.name());
	}

	static SettingsModelString getSettingsAligmentType() {

		return new SettingsModelString(ALIGNMENT_TYPE, AlignmentType.WHOLE_SPECTRUM.name());
	}

	static SettingsModelDoubleRange getSettingsSinglePeakBorder() {

		return new SettingsModelDoubleRange(SINGLE_PEAK_BORDER, 0, 10);
	}

	static SettingsModelInteger getSettingsNumberOfIntervals() {

		return new SettingsModelInteger(NUMBER_OF_INTERVALS, 20);
	}

	static SettingsModelString getSettingsShiftCorrectionType() {

		return new SettingsModelString(SHIFT_CORRECTION_TYPE, ShiftCorrectionType.FAST.name());
	}

	static SettingsModelDouble getIntervalLength() {

		return new SettingsModelDouble(INTERVAL_LENGHT, 20);
	}

	static SettingsModelInteger getSettingsShiftCorrectionTypeValue() {

		return new SettingsModelInteger(SHIFT_CORRECTION_TYPE_VALUE, 10);
	}

	protected AlignmentNodeModel() {

		super(new PortType[]{ScanNMRPortObject.TYPE}, new PortType[]{BufferedDataTable.TYPE});
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return null;
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		ScanNMRPortObject scanNMRPortObject = PortObjectSupport.getScanNMRPortObject(inObjects);
		IMeasurementNMR measurement = scanNMRPortObject.getScanNMR().getMeasurmentNMR();
		measurements.add(measurement);
		LoopStartNode loopStart = getLoopStartNode();
		if(!(getLoopStartNode() instanceof LoopStartNodeTerminator)) {
			throw new IllegalStateException("End node without correct start slice loop node!");
		}
		LoopStartNodeTerminator loopStartNodeTerminator = (LoopStartNodeTerminator)loopStart;
		boolean terminate = loopStartNodeTerminator.terminateLoop();
		if(!terminate) {
			super.continueLoop();
			return null;
		} else {
			SimpleMatrix data = dataAlignment(measurements);
			BufferedDataTable bufferDataTable = extractMultipleSpectra(data, exec);
			return new PortObject[]{bufferDataTable};
		}
	}

	private SimpleMatrix dataAlignment(List<IMeasurementNMR> measurements) {

		IcoShiftAlignment icoShiftAlignment = new IcoShiftAlignment();
		IcoShiftAlignmentSettings settings = new IcoShiftAlignmentSettings();
		return icoShiftAlignment.process(measurements, settings);
	}

	private BufferedDataTable extractMultipleSpectra(SimpleMatrix data, ExecutionContext exec) {

		// List<Object> experimentalDatasetsList = new ArrayList<Object>();
		// experimentalDatasetsList = importMultipleDatasets(experimentalDatasets);
		//
		//
		int numberOfColumns = data.numRows();
		DataColumnSpec[] dataColumnSpec = new DataColumnSpec[numberOfColumns];
		List<double[]> fourierTransformations = new ArrayList<>(numberOfColumns);
		for(int i = 0; i < numberOfColumns; i++) {
			String columnName = Integer.toString(i);
			fourierTransformations.add(new SignalExtractor(measurements.get(i)).extractSignalIntesity());
			dataColumnSpec[i] = new DataColumnSpecCreator(columnName, DoubleCell.TYPE).createSpec();
		}
		DataTableSpec dataTableSpec = new DataTableSpec(dataColumnSpec);
		BufferedDataContainer bufferConteiner = exec.createDataContainer(dataTableSpec);
		int numberOfRow = data.numCols();
		for(int i = 0; i < numberOfRow; i++) {
			RowKey rowKey = new RowKey(Integer.toString(i));
			DataCell[] cells = new DataCell[numberOfColumns];
			for(int j = 0; j < numberOfColumns; j++) {
				cells[j] = new DoubleCell(data.get(j, i));
			}
			DataRow dataRow = new DefaultRow(rowKey, cells);
			bufferConteiner.addRowToTable(dataRow);
		}
		bufferConteiner.close();
		return bufferConteiner.getTable();
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {

		settingsTargetCalcutaltionSelection.loadSettingsFrom(settings);
		settingsGapFillingType.loadSettingsFrom(settings);
		settingsAligmentType.loadSettingsFrom(settings);
		settingsSinglePeakLowerBorder.loadSettingsFrom(settings);
		settingsSingleHigherBorder.loadSettingsFrom(settings);
		settingsNumberOfIntervals.loadSettingsFrom(settings);
		settingsShiftCorrectionType.loadSettingsFrom(settings);
		settingsShiftCorrectionTypeValue.loadSettingsFrom(settings);
		settingsIntervalLenght.loadSettingsFrom(settings);
	}

	@Override
	protected void reset() {

		measurements.clear();
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {

		settingsTargetCalcutaltionSelection.saveSettingsTo(settings);
		settingsGapFillingType.saveSettingsTo(settings);
		settingsAligmentType.saveSettingsTo(settings);
		settingsSinglePeakLowerBorder.saveSettingsTo(settings);
		settingsSingleHigherBorder.saveSettingsTo(settings);
		settingsNumberOfIntervals.saveSettingsTo(settings);
		settingsShiftCorrectionType.saveSettingsTo(settings);
		settingsShiftCorrectionTypeValue.saveSettingsTo(settings);
		settingsIntervalLenght.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

		settingsTargetCalcutaltionSelection.validateSettings(settings);
		settingsGapFillingType.validateSettings(settings);
		settingsAligmentType.validateSettings(settings);
		settingsSinglePeakLowerBorder.validateSettings(settings);
		settingsSingleHigherBorder.validateSettings(settings);
		settingsNumberOfIntervals.validateSettings(settings);
		settingsShiftCorrectionType.validateSettings(settings);
		settingsShiftCorrectionTypeValue.validateSettings(settings);
		settingsIntervalLenght.validateSettings(settings);
	}
}
