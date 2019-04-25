package net.openchrom.knime.nmr.io.bruker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.model.core.IComplexSignalMeasurement;
import org.eclipse.chemclipse.nmr.model.core.FIDMeasurement;
import org.eclipse.chemclipse.nmr.model.core.FIDSignal;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.SubMonitor;
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
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.util.FileUtil;

import net.openchrom.knime.node.base.progress.KnimeProgressMonitor;
import net.openchrom.knime.node.fid.base.portobject.FIDMeasurementPortObject;
import net.openchrom.knime.node.fid.base.portobject.FIDMeasurementPortObjectSpec;
import net.openchrom.nmr.converter.supplier.bruker.core.ScanImportConverterFid;

public class FidReaderNodeModel extends NodeModel {

	private static final NodeLogger logger = NodeLogger.getLogger(FidReaderNodeModel.class);

	public static String SETTINGS_CONFIG_KEY_VALUE_IN = "ValueIn";

	public static String SETTINGS_CONFIG_DEFAULT_VALUE_IN = null;

	public static SettingsModelString createSettingsModelValueIn() {
		return new SettingsModelString(SETTINGS_CONFIG_KEY_VALUE_IN, SETTINGS_CONFIG_DEFAULT_VALUE_IN);
	}

	private final SettingsModelString valueIn;

	private Path inputDir;

	public FidReaderNodeModel() {
		// zero input ports, one FID-port-object and one table as output
		super(new PortType[] {}, new PortType[] { FIDMeasurementPortObject.TYPE, BufferedDataTable.TYPE });
		valueIn = createSettingsModelValueIn();
	}

	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		if (valueIn.getStringValue() != null) {
			try {
				inputDir = FileUtil.getFileFromURL(FileUtil.toURL(valueIn.getStringValue())).toPath();
			} catch (final Exception e) {
				throw new InvalidSettingsException("Cannot read directory " + inputDir, e);
			}
			if (inputDir != null && Files.isDirectory(inputDir) && Files.isReadable(inputDir)) {
				// ok
			}
			logger.info(this.getClass().getSimpleName() + ": Input specs: " + Arrays.asList(inSpecs) + ", input dir: "
					+ inputDir);
			final FIDMeasurementPortObjectSpec portOne = new FIDMeasurementPortObjectSpec();
			final DataTableSpec portTwo = getFIDTableSpec();
			return new PortObjectSpec[] { portOne, portTwo };
		}
		throw new InvalidSettingsException("Cannot read directory " + inputDir);
	}

	@Deprecated
	static DataTableSpec getFIDTableSpec() {
		return new DataTableSpec(
				new DataColumnSpec[] { new DataColumnSpecCreator("Measurement Cnt", LongCell.TYPE).createSpec(),
						new DataColumnSpecCreator("Cnt", LongCell.TYPE).createSpec(),
						new DataColumnSpecCreator("Time", DoubleCell.TYPE).createSpec(),
						new DataColumnSpecCreator("Y", DoubleCell.TYPE).createSpec(),
						new DataColumnSpecCreator("Imaginary Y", DoubleCell.TYPE).createSpec() });
	}

	@Override
	protected PortObject[] execute(final PortObject[] inObjects, final ExecutionContext exec) throws Exception {
		logger.info(this.getClass().getSimpleName() + ": InObjects: " + Arrays.asList(inObjects));

		SubMonitor subMonitor = SubMonitor.convert(new KnimeProgressMonitor(exec), "Reading FID", 100);
		final ScanImportConverterFid importer = new ScanImportConverterFid();
		final IProcessingInfo<Collection<IComplexSignalMeasurement<?>>> processingInfo = importer
				.convert(inputDir.toFile(), subMonitor);

		if (processingInfo == null || processingInfo.getProcessingResult() == null
				|| processingInfo.getProcessingResult().isEmpty())
			throw new Exception("Failed to read any NMR data");

		final Collection<IComplexSignalMeasurement<?>> measurements = processingInfo.getProcessingResult();
		final List<FIDMeasurement> fidMeasurements = measurements.stream().filter(e -> e instanceof FIDMeasurement)
				.map(e -> (FIDMeasurement) e).collect(Collectors.toList());

		logger.info(this.getClass().getSimpleName() + ": Read " + fidMeasurements.size() + " FID measurements");

		final DataTableSpec portTwo = getFIDTableSpec();
		final BufferedDataContainer container = exec.createDataContainer(portTwo);
		long globalRowCnt = 0;
		long measurementCnt = 0;

		// TODO: deaktivierter Output Port, boolean flag um Tabelle zu schreiben

		SubMonitor subMonitor2 = SubMonitor.convert(subMonitor, "Reading Measurements", fidMeasurements.size());
		for (final FIDMeasurement fidMeasurement : fidMeasurements) {
			long signalCnt = 0;
			SubMonitor subMonitor3 = SubMonitor.convert(subMonitor2, "Reading Signals", fidMeasurement.getSignals().size());
			for (final FIDSignal fidSignal : fidMeasurement.getSignals()) {
				exec.checkCanceled();
				container.addRowToTable(
						buildRow(RowKey.createRowKey(globalRowCnt), measurementCnt, signalCnt, fidSignal));
				signalCnt++;
				globalRowCnt++;
				subMonitor3.worked(1);
				if(signalCnt == 30000) {
					int wait = 0;
				}
			}
			subMonitor2.worked(1);
			measurementCnt++;
		}

		final FIDMeasurementPortObject portOneOut = new FIDMeasurementPortObject(measurements);
		container.close();
		final BufferedDataTable portTwoOut = container.getTable();
		return new PortObject[] { portOneOut, portTwoOut };
	}

	@Deprecated
	static DataRow buildRow(final RowKey rowKey, final long measurementCnt, final long signalCnt,
			final FIDSignal fidSignal) {
		final List<DataCell> cells = new ArrayList<>();
		cells.add(new LongCell(measurementCnt));
		cells.add(new LongCell(signalCnt));
		cells.add(new DoubleCell(fidSignal.getSignalTime().doubleValue()));
		cells.add(new DoubleCell(fidSignal.getY()));
		cells.add(new DoubleCell(fidSignal.getImaginaryY()));
		// cells.add(new DoubleCell(fidSignal.getPhase()));
		return new DefaultRow(rowKey, cells);
	}

	@Override
	protected void loadInternals(final File nodeInternDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		logger.info(this.getClass().getSimpleName() + ": Load internals");

	}

	@Override
	protected void saveInternals(final File nodeInternDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		logger.info(this.getClass().getSimpleName() + ": Save internals");

	}

	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		logger.info(this.getClass().getSimpleName() + ": Saving settings");
		valueIn.saveSettingsTo(settings);

	}

	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		logger.info(this.getClass().getSimpleName() + ": Validate settings");
		valueIn.validateSettings(settings);

	}

	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		logger.info(this.getClass().getSimpleName() + ": Loading validated settings");
		valueIn.loadSettingsFrom(settings);

	}

	@Override
	protected void reset() {
		logger.info(this.getClass().getSimpleName() + ": OnReset");

	}

}
