/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *Alexander Kerner - initial API and implementation
 *******************************************************************************/
package net.openchrom.knime.node.fid.table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.chemclipse.nmr.model.core.FIDMeasurement;
import org.eclipse.chemclipse.nmr.model.core.FIDSignal;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.LongCell;
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
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import net.openchrom.knime.node.base.FIDPortObject;

/**
 * {@link NodeModel} for the FID to Table node.
 * 
 * @author Alexander Kerner
 *
 */
public class FIDTableNodeModel extends NodeModel {

    private static final NodeLogger logger = NodeLogger.getLogger(FIDTableNodeModel.class);

    private static DataTableSpec getFIDTableSpec() {
	return new DataTableSpec(
		new DataColumnSpec[] { new DataColumnSpecCreator("Measurement Cnt", LongCell.TYPE).createSpec(),
			new DataColumnSpecCreator("Cnt", LongCell.TYPE).createSpec(),
			new DataColumnSpecCreator("Time", DoubleCell.TYPE).createSpec(),
			new DataColumnSpecCreator("Real", DoubleCell.TYPE).createSpec(),
			new DataColumnSpecCreator("Imaginary", DoubleCell.TYPE).createSpec() });
    }

    private static DataRow buildRow(final RowKey rowKey, final long measurementCnt, final long signalCnt,
	    final FIDSignal fidSignal) {
	final List<DataCell> cells = new ArrayList<>();
	cells.add(new LongCell(measurementCnt));
	cells.add(new LongCell(signalCnt));
	cells.add(new DoubleCell(fidSignal.getSignalTime().doubleValue()));
	cells.add(new DoubleCell(fidSignal.getRealComponent().doubleValue()));
	cells.add(new DoubleCell(fidSignal.getImaginaryComponent().doubleValue()));
	// cells.add(new DoubleCell(fidSignal.getPhase()));
	return new DefaultRow(rowKey, cells);
    }

    public FIDTableNodeModel() {
	super(new PortType[] { FIDPortObject.TYPE }, new PortType[] { BufferedDataTable.TYPE });
    }

    @Override
    protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
	final DataTableSpec portOne = getFIDTableSpec();
	return new PortObjectSpec[] { portOne };
    }

    @Override
    protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
	final DataTableSpec portTwo = getFIDTableSpec();
	final BufferedDataContainer container = exec.createDataContainer(portTwo);
	long globalRowCnt = 0;
	long measurementCnt = 0;

	FIDPortObject fidObject = (FIDPortObject) inObjects[0];
	Collection<FIDMeasurement> measurements = fidObject.getMeasurements();
	exec.getProgressMonitor().setProgress(0);
	for (final FIDMeasurement measurement : measurements) {
	    exec.checkCanceled();
	    long signalCnt = 0;
	    for (final FIDSignal fidSignal : measurement.getSignals()) {
		exec.checkCanceled();
		container.addRowToTable(
			buildRow(RowKey.createRowKey(globalRowCnt), measurementCnt, signalCnt, fidSignal));
		signalCnt++;
		globalRowCnt++;
	    }
	    exec.getProgressMonitor().setProgress(measurementCnt / measurements.size());
	    measurementCnt++;
	}
	container.close();
	final BufferedDataTable portOut = container.getTable();
	return new PortObject[] { portOut };
    }

    @Override
    protected void loadInternals(final File nodeInternDir, final ExecutionMonitor exec)
	    throws IOException, CanceledExecutionException {
	logger.debug(this.getClass().getSimpleName() + ": Load internals");

    }

    @Override
    protected void saveInternals(final File nodeInternDir, final ExecutionMonitor exec)
	    throws IOException, CanceledExecutionException {
	logger.debug(this.getClass().getSimpleName() + ": Save internals");

    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
	logger.debug(this.getClass().getSimpleName() + ": Saving settings");

    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
	logger.debug(this.getClass().getSimpleName() + ": Validate settings");

    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
	logger.debug(this.getClass().getSimpleName() + ": Loading validated settings");

    }

    @Override
    protected void reset() {
	logger.debug(this.getClass().getSimpleName() + ": OnReset");

    }

}
