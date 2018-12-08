/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
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
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.nmr.process.supplier.knime.ui.reader;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.nmr.converter.core.ScanConverterNMR;
import org.eclipse.chemclipse.nmr.model.selection.IDataNMRSelection;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
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
import org.knime.core.node.port.PortTypeRegistry;

import net.openchrom.nmr.process.supplier.knime.portobject.ScanNMRPortObject;
import net.openchrom.nmr.process.supplier.knime.portobject.ScanNMRPortObjectSpec;

/**
 * This is the model implementation of MeasurementReaderNMR.
 * This node is reads chromatographic raw data.
 *
 * @author OpenChrom
 */
public class ReaderNMRNodeModel extends NodeModel {

	/**
	 * the settings key which is used to retrieve and
	 * store the settings (from the dialog or from a settings file)
	 * (package visibility to be usable from the dialog).
	 */
	/**
	 * Constructor for the node model.
	 */
	private static final NodeLogger logger = NodeLogger.getLogger(ReaderNMRNodeModel.class);
	private static final String NMR_FILE_INPUT = "FileInput";
	private SettingsModelString settingsFileInput;

	protected ReaderNMRNodeModel() {

		// TODO one incoming port and one outgoing port is assumed
		super(new PortType[]{}, new PortType[]{PortTypeRegistry.getInstance().getPortType(ScanNMRPortObject.class)});
		settingsFileInput = getSettingsFileInput();
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return new PortObjectSpec[]{new ScanNMRPortObjectSpec()};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		logger.info("Read the scans nmr data.");
		File file = new File(settingsFileInput.getStringValue());
		try {
			IProcessingInfo processingInfo = ScanConverterNMR.convert(file, new NullProgressMonitor());
			IDataNMRSelection scanNMR = (IDataNMRSelection)processingInfo.getProcessingResult();
			return new PortObject[]{new ScanNMRPortObject(scanNMR)};
		} catch(Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}

	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {

		// TODO load internal data.
		// Everything handed to output ports is loaded automatically (data
		// returned by the execute method, models loaded in loadModelContent,
		// and user settings set through loadSettingsFrom - is all taken care
		// of). Load here only the other internals that need to be restored
		// (e.g. data used by the views).
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

		settingsFileInput.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		settingsFileInput.saveSettingsTo(settings);
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

		settingsFileInput.validateSettings(settings);
	}

	static SettingsModelString getSettingsFileInput() {

		return new SettingsModelString(NMR_FILE_INPUT, "");
	}
}
