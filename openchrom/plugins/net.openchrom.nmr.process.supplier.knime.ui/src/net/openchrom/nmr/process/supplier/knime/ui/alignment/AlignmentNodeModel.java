/*******************************************************************************
 * Copyright (c) 2017 Martin Horn.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Martin Horn - initial API and implementation
 *******************************************************************************/
package net.openchrom.nmr.process.supplier.knime.ui.alignment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

import org.eclipse.chemclipse.nmr.model.core.IMeasurementNMR;
import org.eclipse.chemclipse.nmr.model.core.IScanNMR;
import org.eclipse.chemclipse.nmr.model.support.SignalExtractor;
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
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.workflow.LoopEndNode;
import org.knime.core.node.workflow.LoopStartNode;
import org.knime.core.node.workflow.LoopStartNodeTerminator;

import net.openchrom.nmr.process.supplier.knime.portobject.PortObjectSupport;
import net.openchrom.nmr.process.supplier.knime.portobject.ScanNMRPortObject;

/**
 * Node model for the "Apply Filters"-node.
 *
 * @author Martin Horn, University of Konstanz
 *
 */
public class AlignmentNodeModel extends NodeModel implements LoopEndNode {

	private List<IMeasurementNMR> measurements = new ArrayList<>();

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
			BufferedDataTable bufferDataTable = extractMultipleSpectra(measurements, exec);
			return new PortObject[]{bufferDataTable};
		}
	}

	public BufferedDataTable extractMultipleSpectra(List<IMeasurementNMR> measurements, ExecutionContext exec) {

		// List<Object> experimentalDatasetsList = new ArrayList<Object>();
		// experimentalDatasetsList = importMultipleDatasets(experimentalDatasets);
		//
		//
		int numberOfColumns = measurements.size();
		DataColumnSpec[] dataColumnSpec = new DataColumnSpec[numberOfColumns];
		List<double[]> fourierTransformations = new ArrayList<>(numberOfColumns);
		for(int i = 0; i < measurements.size(); i++) {
			String columnName = Integer.toString(i);
			fourierTransformations.add(new SignalExtractor(measurements.get(i)).extractSignalIntesity());
			dataColumnSpec[i] = new DataColumnSpecCreator(columnName, DoubleCell.TYPE).createSpec();
		}
		DataTableSpec dataTableSpec = new DataTableSpec(dataColumnSpec);
		BufferedDataContainer bufferConteiner = exec.createDataContainer(dataTableSpec);
		OptionalInt maxRow = measurements.stream().mapToInt(m -> m.getScanMNR().getNumberOfFourierPoints()).max();
		if(maxRow.isPresent()) {
			for(int i = 0; i < maxRow.getAsInt(); i++) {
				RowKey rowKey = new RowKey(Integer.toString(i));
				DataCell[] cells = new DataCell[numberOfColumns];
				for(int j = 0; j < numberOfColumns; j++) {
					IScanNMR scans = measurements.get(j).getScanMNR();
					int scanSize = scans.getNumberOfFourierPoints();
					if(i < scanSize) {
						double cellNumber = fourierTransformations.get(j)[i];
						cells[j] = new DoubleCell(cellNumber);
					} else {
						cells[j] = new DoubleCell(0.0);
					}
				}
				DataRow dataRow = new DefaultRow(rowKey, cells);
				bufferConteiner.addRowToTable(dataRow);
			}
		}
		bufferConteiner.close();
		return bufferConteiner.getTable();
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {

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

	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

	}
}
