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
package net.openchrom.xxd.process.supplier.knime.ui.io.csd;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
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

import net.openchrom.xxd.process.supplier.knime.model.ChromatogramCSDExportSerialization;
import net.openchrom.xxd.process.supplier.knime.model.ChromatogramReportSerialization;
import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionCSDPortObject;
import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionCSDPortObjectSpec;
import net.openchrom.xxd.process.supplier.knime.model.IChromatogramCSDExport;
import net.openchrom.xxd.process.supplier.knime.model.IChromatogramReport;

/**
 * This is the model implementation of ChromatogramWriterCSD.
 * This node writes chromatographic data.
 */
public class ChromatogramExportNodeModel extends NodeModel {

	//
	private static final String CHROMATOGRAM_CSD_EXPORT = "ChromatgramCSDExport";
	private static final String CHROMATOGRAM_REPORT = "ChromatgramCSDReport";
	private static final NodeLogger logger = NodeLogger.getLogger(ChromatogramExportNodeModel.class);
	private SettingsModelString chromatogramCSDExport;
	private SettingsModelString chromatogramReport;
	private ChromatogramCSDExportSerialization chromatogramCSDExportSerialization;
	private ChromatogramReportSerialization chromatogramReprotSerialization;

	/**
	 * Constructor for the node model.
	 */
	protected ChromatogramExportNodeModel() {
		super(new PortType[]{PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionCSDPortObject.class)}, new PortType[]{});
		chromatogramCSDExport = getSettingsModelChromatogramCSDExport();
		chromatogramReport = getCreateSettingsModelReport();
		chromatogramCSDExportSerialization = new ChromatogramCSDExportSerialization();
		chromatogramReprotSerialization = new ChromatogramReportSerialization();
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return null;
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		logger.info("Extract chromatogram");
		ChromatogramSelectionCSDPortObject chromatogramSelectionPortObject = (ChromatogramSelectionCSDPortObject)inObjects[0];
		ChromatogramSelectionCSDPortObjectSpec chromatogramSelectionCSDPortObjectSpec = chromatogramSelectionPortObject.getSpec();
		IChromatogramSelectionCSD chromatogramSelection = chromatogramSelectionPortObject.getChromatogramSelectionCSD();
		logger.info("Export chromatogram");
		List<IChromatogramCSDExport> exporters = chromatogramCSDExportSerialization.deserialize(chromatogramCSDExport.getStringValue());
		for(IChromatogramCSDExport exporter : exporters) {
			exporter.process(chromatogramSelection, new NullProgressMonitor());
		}
		logger.info("Create reports");
		List<IChromatogramReport> retorters = chromatogramReprotSerialization.deserialize(chromatogramReport.getStringValue());
		for(IChromatogramReport reporter : retorters) {
			reporter.process(chromatogramSelection, new NullProgressMonitor());
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

		chromatogramCSDExport.loadSettingsFrom(settings);
		chromatogramReport.loadSettingsFrom(settings);
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

		chromatogramCSDExport.saveSettingsTo(settings);
		chromatogramReport.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		chromatogramCSDExport.validateSettings(settings);
		chromatogramReport.validateSettings(settings);
	}

	static SettingsModelString getSettingsModelChromatogramCSDExport() {

		return new SettingsModelString(CHROMATOGRAM_CSD_EXPORT, "");
	}

	static SettingsModelString getCreateSettingsModelReport() {

		return new SettingsModelString(CHROMATOGRAM_REPORT, "");
	}
}
