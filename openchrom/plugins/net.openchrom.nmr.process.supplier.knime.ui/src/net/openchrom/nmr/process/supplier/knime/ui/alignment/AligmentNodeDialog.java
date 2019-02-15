/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentDoubleRange;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;

import net.openchrom.nmr.processing.supplier.base.core.IcoShiftAlignment.AlignmentType;
import net.openchrom.nmr.processing.supplier.base.core.IcoShiftAlignment.ShiftCorrectionType;
import net.openchrom.nmr.processing.supplier.base.core.IcoShiftAlignment.TargetCalculationSelection;
import net.openchrom.process.supplier.knime.ui.dialog.DialogComponentStringIdSelection;

public class AligmentNodeDialog extends DefaultNodeSettingsPane {

	AligmentNodeDialog() {

		List<String> targetsCalculation = Arrays.stream(TargetCalculationSelection.values()).map(t -> t.toString()).collect(Collectors.toList());
		Map<String, String> idTargetCalculation = Arrays.stream(TargetCalculationSelection.values()).collect(Collectors.toMap(TargetCalculationSelection::toString, TargetCalculationSelection::name));
		addDialogComponent(new DialogComponentStringIdSelection(AlignmentNodeModel.getSettingsTargetCalcutaltionSelection(), "Target calculation", targetsCalculation, idTargetCalculation, false));
		//
		List<String> shiftCorrection = Arrays.stream(ShiftCorrectionType.values()).map(ShiftCorrectionType::toString).collect(Collectors.toList());
		Map<String, String> idShiftCorrection = Arrays.stream(ShiftCorrectionType.values()).collect(Collectors.toMap(ShiftCorrectionType::toString, ShiftCorrectionType::name));
		addDialogComponent(new DialogComponentStringIdSelection(AlignmentNodeModel.getSettingsShiftCorrectionType(), "Shift Correction", shiftCorrection, idShiftCorrection, false));
		//
		addDialogComponent(new DialogComponentNumber(AlignmentNodeModel.getSettingsShiftCorrectionTypeValue(), "Shift Correction Value (Only User Defined)", 10));
		//
		AlignmentType[] aligmentsType = {AlignmentType.SINGLE_PEAK, AlignmentType.NUMBER_OF_INTERVALS, AlignmentType.INTERVAL_LENGTH, AlignmentType.WHOLE_SPECTRUM};
		List<String> alignments = Arrays.stream(aligmentsType).map(t -> t.toString()).collect(Collectors.toList());
		Map<String, String> idAlignments = Arrays.stream(aligmentsType).collect(Collectors.toMap(AlignmentType::toString, AlignmentType::name));
		addDialogComponent(new DialogComponentStringIdSelection(AlignmentNodeModel.getSettingsTargetCalcutaltionSelection(), "Alignments", alignments, idAlignments, false));
		//
		addDialogComponent(new DialogComponentDoubleRange(AlignmentNodeModel.getSettingsSinglePeakBorder(), 0.0, Double.MAX_VALUE, 10, "Peak Borders (ppm)"));
		//
		addDialogComponent(new DialogComponentNumber(AlignmentNodeModel.getSettingsNumberOfIntervals(), "Number of Intervals", 10));
		//
		addDialogComponent(new DialogComponentNumber(AlignmentNodeModel.getSettingsNumberOfIntervals(), "Interval Lenght (ppm)", 20));
	}
}
