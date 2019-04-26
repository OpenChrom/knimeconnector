package net.openchrom.knime.node.nmr.table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.chemclipse.model.core.IComplexSignalMeasurement;
import org.eclipse.chemclipse.nmr.model.core.SpectrumMeasurement;
import org.eclipse.chemclipse.nmr.model.core.SpectrumSignal;
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
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import net.openchrom.knime.node.nmr.ft.portobject.NMRMeasurementPortObject;

public class NMRTableNodeModel extends NodeModel {

	static DataTableSpec getFIDTableSpec() {
		return new DataTableSpec(
				new DataColumnSpec[] { new DataColumnSpecCreator("Measurement Cnt", LongCell.TYPE).createSpec(),
						new DataColumnSpecCreator("Cnt", LongCell.TYPE).createSpec(),
						new DataColumnSpecCreator("Chemical Shift", DoubleCell.TYPE).createSpec(),
						new DataColumnSpecCreator("Y", DoubleCell.TYPE).createSpec(),
						new DataColumnSpecCreator("Imaginary Y", DoubleCell.TYPE).createSpec() });
	}

	static DataRow buildRow(final RowKey rowKey, final long measurementCnt, final long signalCnt,
			final SpectrumSignal signal) {
		final List<DataCell> cells = new ArrayList<>();
		cells.add(new LongCell(measurementCnt));
		cells.add(new LongCell(signalCnt));
		cells.add(new DoubleCell(signal.getX()));
		cells.add(new DoubleCell(signal.getY()));
		cells.add(new DoubleCell(signal.getImaginaryY()));
		// cells.add(new DoubleCell(fidSignal.getPhase()));
		return new DefaultRow(rowKey, cells);
	}

	public NMRTableNodeModel() {
		super(new PortType[] { NMRMeasurementPortObject.TYPE }, new PortType[] { BufferedDataTable.TYPE });
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

		NMRMeasurementPortObject fidObject = (NMRMeasurementPortObject) inObjects[0];
		Collection<IComplexSignalMeasurement<?>> measurements = fidObject.getMeasurements();
		exec.getProgressMonitor().setProgress(0);
		for (final IComplexSignalMeasurement<?> measurement : measurements) {
			exec.checkCanceled();
			if (measurement instanceof SpectrumMeasurement) {
				long signalCnt = 0;
				for (final SpectrumSignal signal : ((SpectrumMeasurement) measurement).getSignals()) {
					exec.checkCanceled();
					// skip a few signals
					if (signalCnt % 5 == 0) {
						container.addRowToTable(
								buildRow(RowKey.createRowKey(globalRowCnt), measurementCnt, signalCnt, signal));
					}
					signalCnt++;
					globalRowCnt++;
				}
				exec.getProgressMonitor().setProgress(measurementCnt / measurements.size());
			}
			measurementCnt++;
		}
		container.close();
		final BufferedDataTable portOut = container.getTable();
		return new PortObject[] { portOut };
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void reset() {
		// TODO Auto-generated method stub

	}

}
