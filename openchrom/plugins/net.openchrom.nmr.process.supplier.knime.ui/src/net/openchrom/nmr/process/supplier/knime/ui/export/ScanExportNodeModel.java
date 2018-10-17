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
package net.openchrom.nmr.process.supplier.knime.ui.export;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.chemclipse.nmr.model.core.IScanNMR;
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

import net.openchrom.nmr.process.supplier.knime.portobject.ScanNMRPortObject;
import net.openchrom.process.supplier.knime.model.DataExportSerialization;
import net.openchrom.process.supplier.knime.model.IDataExport;

/**
 * This is the model implementation of ChromatogramWriterCSD.
 * This node writes chromatographic data.
 */
public class ScanExportNodeModel extends NodeModel {

	//
	private static final String SCAN_NMR_EXPORT = "ScanExport";
	private static final NodeLogger logger = NodeLogger.getLogger(ScanExportNodeModel.class);
	private SettingsModelString scanExport;
	private DataExportSerialization<IScanNMR> dataExporterSerialization;

	/**
	 * Constructor for the node model.
	 */
	protected ScanExportNodeModel() {

		super(new PortType[]{ScanNMRPortObject.TYPE}, new PortType[]{});
		scanExport = getSettingsModelScanNMRExport();
		dataExporterSerialization = new DataExportSerialization<>();
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return null;
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		logger.info("Extract chromatogram");
		ScanNMRPortObject scanNMRPortObject = (ScanNMRPortObject)inObjects[0];
		IScanNMR scanNMR = scanNMRPortObject.getScanNMR();
		logger.info("Export scans");
		List<IDataExport<IScanNMR>> exporters = dataExporterSerialization.deserialize(scanExport.getStringValue());
		for(IDataExport<IScanNMR> exporter : exporters) {
			exporter.process(scanNMR, new NullProgressMonitor());
		}
		return new PortObject[]{};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

		scanExport.loadSettingsFrom(settings);
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
	protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		scanExport.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		scanExport.validateSettings(settings);
	}

	static SettingsModelString getSettingsModelScanNMRExport() {

		return new SettingsModelString(SCAN_NMR_EXPORT, "");
	}
}
