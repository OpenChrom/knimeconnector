/*******************************************************************************
 * Copyright (c) 2017, 2019 Martin Horn, Lablicate GmbH
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Martin Horn - initial API and implementation
 * Jan Holy - implementation
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
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
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
import net.openchrom.nmr.processing.supplier.base.settings.IcoShiftAlignmentSettings;

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
	private static final String PRELIMITER_COSHIFITINGS = "PRELIMITER_COSHIFITINGS";
	//
	private final List<IMeasurementNMR> measurements = new ArrayList<>();
	private final SettingsModelString settingsTargetCalcutaltionSelection;
	private final SettingsModelString settingsGapFillingType;
	private final SettingsModelString settingsAligmentType;
	private final SettingsModelDoubleRange settingsSinglePeakBorder;
	private final SettingsModelInteger settingsNumberOfIntervals;
	private final SettingsModelString settingsShiftCorrectionType;
	private final SettingsModelInteger settingsShiftCorrectionTypeValue;
	private final SettingsModelDouble settingsIntervalLenght;
	private final SettingsModelBoolean settingsPrelimiterCoShifting;

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

	static SettingsModelDouble getSettingsIntervalLength() {

		return new SettingsModelDouble(INTERVAL_LENGHT, 20);
	}

	static SettingsModelInteger getSettingsShiftCorrectionTypeValue() {

		return new SettingsModelInteger(SHIFT_CORRECTION_TYPE_VALUE, 10);
	}

	static SettingsModelBoolean getSettingsPrelimiterCoShifting() {

		return new SettingsModelBoolean(PRELIMITER_COSHIFITINGS, false);
	}

	protected AlignmentNodeModel() {

		super(new PortType[] { ScanNMRPortObject.TYPE }, new PortType[] { BufferedDataTable.TYPE });
		settingsTargetCalcutaltionSelection = getSettingsTargetCalcutaltionSelection();
		settingsGapFillingType = getSettingsGapFillingType();
		settingsAligmentType = getSettingsAligmentType();
		settingsSinglePeakBorder = getSettingsSinglePeakBorder();
		settingsNumberOfIntervals = getSettingsNumberOfIntervals();
		settingsShiftCorrectionType = getSettingsShiftCorrectionType();
		settingsShiftCorrectionTypeValue = getSettingsShiftCorrectionTypeValue();
		settingsIntervalLenght = getSettingsIntervalLength();
		settingsPrelimiterCoShifting = getSettingsPrelimiterCoShifting();
	}

	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return null;
	}

	@Override
	protected PortObject[] execute(final PortObject[] inObjects, final ExecutionContext exec) throws Exception {

		final ScanNMRPortObject scanNMRPortObject = PortObjectSupport.getScanNMRPortObject(inObjects);
		final IMeasurementNMR measurement = scanNMRPortObject.getScanNMR().getMeasurmentNMR();
		measurements.add(measurement);
		final LoopStartNode loopStart = getLoopStartNode();
		if (!(getLoopStartNode() instanceof LoopStartNodeTerminator))
			throw new IllegalStateException("End node without correct start slice loop node!");
		final LoopStartNodeTerminator loopStartNodeTerminator = (LoopStartNodeTerminator) loopStart;
		final boolean terminate = loopStartNodeTerminator.terminateLoop();
		if (!terminate) {
			super.continueLoop();
			return null;
		} else {
			final SimpleMatrix data = dataAlignment(measurements);
			final BufferedDataTable bufferDataTable = extractMultipleSpectra(data, exec);
			return new PortObject[] { bufferDataTable };
		}
	}

	private SimpleMatrix dataAlignment(final List<IMeasurementNMR> measurements) {

		final IcoShiftAlignment icoShiftAlignment = new IcoShiftAlignment();
		final IcoShiftAlignmentSettings settings = new IcoShiftAlignmentSettings();
		settings.setAligmentType(AlignmentType.valueOf(settingsAligmentType.getStringValue()));
		settings.setGapFillingType(GapFillingType.valueOf(settingsGapFillingType.getStringValue()));
		settings.setIntervalLength(settingsIntervalLenght.getDoubleValue());
		settings.setNumberOfIntervals(settingsNumberOfIntervals.getIntValue());
		settings.setShiftCorrectionType(ShiftCorrectionType.valueOf(settingsShiftCorrectionType.getStringValue()));
		settings.setShiftCorrectionTypeValue(settingsShiftCorrectionTypeValue.getIntValue());
		settings.setTargetCalculationSelection(
				TargetCalculationSelection.valueOf(settingsTargetCalcutaltionSelection.getStringValue()));
		settings.setSinglePeakLowerBorder(settingsSinglePeakBorder.getMinRange());
		settings.setSinglePeakHigherBorder(settingsSinglePeakBorder.getMaxRange());
		return icoShiftAlignment.process(measurements, settings);
	}

	private BufferedDataTable extractMultipleSpectra(final SimpleMatrix data, final ExecutionContext exec) {

		// List<Object> experimentalDatasetsList = new ArrayList<Object>();
		// experimentalDatasetsList = importMultipleDatasets(experimentalDatasets);
		//
		//
		final int numberOfColumns = data.numRows();
		final DataColumnSpec[] dataColumnSpec = new DataColumnSpec[numberOfColumns];
		final List<double[]> fourierTransformations = new ArrayList<>(numberOfColumns);
		for (int i = 0; i < numberOfColumns; i++) {
			final String columnName = Integer.toString(i);
			fourierTransformations.add(new SignalExtractor(measurements.get(i)).extractSignalIntesity());
			dataColumnSpec[i] = new DataColumnSpecCreator(columnName, DoubleCell.TYPE).createSpec();
		}
		final DataTableSpec dataTableSpec = new DataTableSpec(dataColumnSpec);
		final BufferedDataContainer bufferConteiner = exec.createDataContainer(dataTableSpec);
		final int numberOfRow = data.numCols();
		for (int i = 0; i < numberOfRow; i++) {
			final RowKey rowKey = new RowKey(Integer.toString(i));
			final DataCell[] cells = new DataCell[numberOfColumns];
			for (int j = 0; j < numberOfColumns; j++) {
				cells[j] = new DoubleCell(data.get(j, i));
			}
			final DataRow dataRow = new DefaultRow(rowKey, cells);
			bufferConteiner.addRowToTable(dataRow);
		}
		bufferConteiner.close();
		return bufferConteiner.getTable();
	}

	@Override
	protected void loadInternals(final File nodeInternDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {

	}

	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

		settingsTargetCalcutaltionSelection.loadSettingsFrom(settings);
		settingsGapFillingType.loadSettingsFrom(settings);
		settingsAligmentType.loadSettingsFrom(settings);
		settingsSinglePeakBorder.loadSettingsFrom(settings);
		settingsNumberOfIntervals.loadSettingsFrom(settings);
		settingsShiftCorrectionType.loadSettingsFrom(settings);
		settingsShiftCorrectionTypeValue.loadSettingsFrom(settings);
		settingsIntervalLenght.loadSettingsFrom(settings);
		settingsPrelimiterCoShifting.loadSettingsFrom(settings);
	}

	@Override
	protected void reset() {

		measurements.clear();
	}

	@Override
	protected void saveInternals(final File nodeInternDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {

	}

	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		settingsTargetCalcutaltionSelection.saveSettingsTo(settings);
		settingsGapFillingType.saveSettingsTo(settings);
		settingsAligmentType.saveSettingsTo(settings);
		settingsSinglePeakBorder.saveSettingsTo(settings);
		settingsNumberOfIntervals.saveSettingsTo(settings);
		settingsShiftCorrectionType.saveSettingsTo(settings);
		settingsShiftCorrectionTypeValue.saveSettingsTo(settings);
		settingsIntervalLenght.saveSettingsTo(settings);
		settingsPrelimiterCoShifting.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		settingsTargetCalcutaltionSelection.validateSettings(settings);
		settingsGapFillingType.validateSettings(settings);
		settingsAligmentType.validateSettings(settings);
		settingsSinglePeakBorder.validateSettings(settings);
		settingsNumberOfIntervals.validateSettings(settings);
		settingsShiftCorrectionType.validateSettings(settings);
		settingsShiftCorrectionTypeValue.validateSettings(settings);
		settingsIntervalLenght.validateSettings(settings);
		settingsPrelimiterCoShifting.validateSettings(settings);
	}
}
