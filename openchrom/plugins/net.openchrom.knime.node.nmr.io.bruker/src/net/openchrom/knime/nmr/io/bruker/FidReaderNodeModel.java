package net.openchrom.knime.nmr.io.bruker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.model.core.IComplexSignalMeasurement;
import org.eclipse.chemclipse.nmr.model.core.FIDMeasurement;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.SubMonitor;
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
		super(new PortType[] {}, new PortType[] { FIDMeasurementPortObject.TYPE });
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
			return new PortObjectSpec[] { portOne };
		}
		throw new InvalidSettingsException("Cannot read directory " + inputDir);
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

		final FIDMeasurementPortObject portOneOut = new FIDMeasurementPortObject(measurements);
		return new PortObject[] { portOneOut };
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
