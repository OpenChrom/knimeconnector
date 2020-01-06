/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Contributors:
 * Alexander Kerner - initial API and implementation
 *******************************************************************************/
package net.openchrom.knime.node.nmr.io.bruker.nmr;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.model.core.IComplexSignalMeasurement;
import org.eclipse.chemclipse.nmr.model.core.SpectrumMeasurement;
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

import net.openchrom.knime.node.base.GenericPortObjectSpec;
import net.openchrom.knime.node.base.NMRPortObject;
import net.openchrom.knime.node.base.progress.KnimeProgressMonitor;
import net.openchrom.nmr.converter.supplier.bruker.core.ScanImportConverterFid;

/**
 * {@link NodeModel} for the Bruker 1r/1i reader node.
 * 
 * @author Alexander Kerner
 *
 */
public class NMRReaderNodeModel extends NodeModel {

	private static final NodeLogger logger = NodeLogger.getLogger(NMRReaderNodeModel.class);
	public static String SETTINGS_CONFIG_KEY_VALUE_IN = "ValueIn";
	public static String SETTINGS_CONFIG_DEFAULT_VALUE_IN = null;

	public static SettingsModelString createSettingsModelValueIn() {

		return new SettingsModelString(SETTINGS_CONFIG_KEY_VALUE_IN, SETTINGS_CONFIG_DEFAULT_VALUE_IN);
	}

	private final SettingsModelString valueIn;
	private Path inputDir;

	public NMRReaderNodeModel() {

		// zero input ports, one FID-port-object and one NMR-port-object as
		// output
		super(new PortType[]{}, new PortType[]{NMRPortObject.TYPE});
		valueIn = createSettingsModelValueIn();
	}

	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		if(valueIn.getStringValue() != null) {
			try {
				inputDir = FileUtil.getFileFromURL(FileUtil.toURL(valueIn.getStringValue())).toPath();
			} catch(final Exception e) {
				throw new InvalidSettingsException("Cannot read directory " + inputDir, e);
			}
			if(inputDir != null && Files.isDirectory(inputDir) && Files.isReadable(inputDir)) {
				logger.info(this.getClass().getSimpleName() + ": Input specs: " + Arrays.asList(inSpecs) + ", input dir: " + inputDir);
				final GenericPortObjectSpec portOne = new GenericPortObjectSpec();
				return new PortObjectSpec[]{portOne};
			}
		}
		throw new InvalidSettingsException("Cannot read directory " + inputDir);
	}

	@Override
	protected PortObject[] execute(final PortObject[] inObjects, final ExecutionContext exec) throws Exception {

		logger.info(this.getClass().getSimpleName() + ": InObjects: " + Arrays.asList(inObjects));
		SubMonitor subMonitor = SubMonitor.convert(new KnimeProgressMonitor(exec), "Reading NMR", 100);
		final ScanImportConverterFid importer = new ScanImportConverterFid();
		final IProcessingInfo<Collection<IComplexSignalMeasurement<?>>> processingInfo = importer.convert(inputDir.toFile(), subMonitor);
		if(processingInfo == null || processingInfo.getProcessingResult() == null || processingInfo.getProcessingResult().isEmpty()) {
			throw new Exception("Failed to read data");
		}
		final List<SpectrumMeasurement> nmrMeasurements = processingInfo.getProcessingResult().stream().filter(e -> e instanceof SpectrumMeasurement).map(e -> (SpectrumMeasurement)e).collect(Collectors.toList());
		logger.debug(this.getClass().getSimpleName() + ": Read " + nmrMeasurements.size() + " NMR measurements");
		final NMRPortObject portTwoOut = new NMRPortObject(nmrMeasurements);
		if(nmrMeasurements.isEmpty()) {
			logger.warn(this.getClass().getSimpleName() + ": No NMR data read!");
		}
		return new PortObject[]{portTwoOut};
	}

	@Override
	protected void loadInternals(final File nodeInternDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {

		logger.debug(this.getClass().getSimpleName() + ": Load internals");
	}

	@Override
	protected void saveInternals(final File nodeInternDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {

		logger.debug(this.getClass().getSimpleName() + ": Save internals");
	}

	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		logger.debug(this.getClass().getSimpleName() + ": Saving settings");
		valueIn.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		logger.debug(this.getClass().getSimpleName() + ": Validate settings");
		valueIn.validateSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

		logger.debug(this.getClass().getSimpleName() + ": Loading validated settings");
		valueIn.loadSettingsFrom(settings);
	}

	@Override
	protected void reset() {

		logger.debug(this.getClass().getSimpleName() + ": OnReset");
	}
}
