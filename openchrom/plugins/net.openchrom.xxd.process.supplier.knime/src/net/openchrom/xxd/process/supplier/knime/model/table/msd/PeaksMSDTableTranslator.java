/*******************************************************************************
 * Copyright (c) 2018 jan.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * jan - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.model.table.msd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.chemclipse.model.comparator.TargetExtendedComparator;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.targets.IPeakTarget;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakModelMSD;
import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.eclipse.chemclipse.support.comparator.SortOrder;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import net.openchrom.xxd.process.supplier.knime.model.table.TableValidation;

public class PeaksMSDTableTranslator implements IPeaksMSDTableTranslator {

	private TargetExtendedComparator targetExtendedComparator;

	public PeaksMSDTableTranslator() {

		targetExtendedComparator = new TargetExtendedComparator(SortOrder.DESC);
		targetExtendedComparator = new TargetExtendedComparator(SortOrder.DESC);
	}

	@Override
	public BufferedDataTable getBufferedDataTable(Collection<IChromatogramPeakMSD> peaks, ExecutionContext exec) throws CanceledExecutionException, NoExtractedIonSignalStoredException {

		int numberOfColumns = 14;
		int columnSpec = 0;
		DataColumnSpec[] dataColumnSpec = new DataColumnSpec[numberOfColumns];
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.PEAK_IS_ACTIVE_FOR_ANALYSIS, StringCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.RETENTION_TIME, DoubleCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.RETENTION_INDEX, DoubleCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.PEAK_AREA, DoubleCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.PEAK_START_RETENTION_TIME, DoubleCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.PEAK_STOP_RETENTION_TIME, DoubleCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.PEAK_WIDTH, DoubleCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.PEAK_SCAN_NUMBER_AT_PEAK_MAXIMUM, DoubleCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.PEAK_SIGNAL_TO_NOISE_RETION, DoubleCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.PEAK_LEADING, DoubleCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.PEAK_TAILING, DoubleCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.PEAK_MODEL_DESCRIPTION, StringCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.PEAK_SUGGERSTION_COMPONENTS, DoubleCell.TYPE).createSpec();
		dataColumnSpec[columnSpec++] = new DataColumnSpecCreator(TableValidation.PEAK_NAME, StringCell.TYPE).createSpec();
		DataTableSpec dataTableSpec = new DataTableSpec(dataColumnSpec);
		Iterator<IChromatogramPeakMSD> it = peaks.iterator();
		int numPeak = 0;
		BufferedDataContainer bufferedDataContainer = exec.createDataContainer(dataTableSpec);
		while(it.hasNext()) {
			/*
			 * Set the cell data.
			 */
			IChromatogramPeakMSD peak = it.next();
			IPeakModelMSD peakModel = peak.getPeakModel();
			RowKey rowKey = new RowKey(Integer.toString(numPeak));
			ILibraryInformation libraryInformation = getLibraryInformation(new ArrayList<>(peak.getTargets()));
			int columnCell = 0;
			DataCell[] cells = new DataCell[numberOfColumns];
			cells[columnCell++] = new StringCell(Boolean.toString(peak.isActiveForAnalysis()));
			cells[columnCell++] = new DoubleCell(peakModel.getRetentionTimeAtPeakMaximum());
			cells[columnCell++] = new DoubleCell(peakModel.getPeakMassSpectrum().getRetentionIndex());
			cells[columnCell++] = new DoubleCell(peak.getIntegratedArea());
			cells[columnCell++] = new DoubleCell(peakModel.getStartRetentionTime());
			cells[columnCell++] = new DoubleCell(peakModel.getStopRetentionTime());
			cells[columnCell++] = new DoubleCell(peakModel.getWidthByInflectionPoints());
			cells[columnCell++] = new DoubleCell(peak.getScanMax());
			cells[columnCell++] = new DoubleCell(peak.getSignalToNoiseRatio());
			cells[columnCell++] = new DoubleCell(peakModel.getLeading());
			cells[columnCell++] = new DoubleCell(peakModel.getTailing());
			cells[columnCell++] = new StringCell(peak.getModelDescription());
			cells[columnCell++] = new DoubleCell(peak.getSuggestedNumberOfComponents());
			if(libraryInformation != null) {
				cells[columnCell++] = new StringCell(libraryInformation.getName());
			} else {
				cells[columnCell++] = new StringCell("");
			}
			DataRow dataRow = new DefaultRow(rowKey, cells);
			bufferedDataContainer.addRowToTable(dataRow);
			exec.checkCanceled();
			exec.setProgress(numPeak / peaks.size(), "Adding Peak: " + numPeak);
			numPeak++;
		}
		//
		bufferedDataContainer.close();
		return bufferedDataContainer.getTable();
	}

	private ILibraryInformation getLibraryInformation(List<IPeakTarget> targets) {

		ILibraryInformation libraryInformation = null;
		targets = new ArrayList<>(targets);
		Collections.sort(targets, targetExtendedComparator);
		if(targets.size() >= 1) {
			libraryInformation = targets.get(0).getLibraryInformation();
		}
		return libraryInformation;
	}
	//
}
