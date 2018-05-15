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
package net.openchrom.xxd.process.supplier.knime.ui.io.xir;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.xir.converter.core.ScanConverterXIR;
import org.eclipse.chemclipse.xir.model.core.IScanXIR;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.knime.core.data.DataTableSpec;
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

import net.openchrom.xxd.process.supplier.knime.model.scan.xir.ScanXirTableTranslator;
import net.openchrom.xxd.process.supplier.knime.model.scan.xir.ScanXirTableTranslatorBackgroundSignals;
import net.openchrom.xxd.process.supplier.knime.model.scan.xir.ScanXirTableTranslatorRawData;
import net.openchrom.xxd.process.supplier.knime.model.utils.TableTranslator;

/**
 * This is the model implementation of MeasurementWriterNMR.
 * This node writes chromatographic data.
 *
 * @author OpenChrom
 */
public class MeasurementReader2TableXIRNodeModel extends NodeModel {

	private static final String XIR_FILE_INPUT = "FileInput";
	protected static final String RAW_DATA = "Raw Data";
	protected static final String ROTATION_ANGLE = "Processed Data";
	protected static final String BACKGROUNG_SIGNAL = "Background Signal";
	protected static final SettingsModelString SETTING_XIR_FILE_INPUT = new SettingsModelString(XIR_FILE_INPUT, "");
	protected static final SettingsModelString SETTING_XIR_TABLE_OUTPUT = new SettingsModelString(ROTATION_ANGLE, "");
	// the logger instance
	private static final NodeLogger logger = NodeLogger.getLogger(MeasurementReader2TableXIRNodeModel.class);
	// example value: the models count variable filled from the dialog
	// and used in the models execution method. The default components of the
	// dialog work with "SettingsModels".

	/**
	 * file
	 * Constructor for the node model.
	 */
	protected MeasurementReader2TableXIRNodeModel() {
		// TODO one incoming port and one outgoing port is assumed
		super(0, 2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {

		// TODO: check if user settings are available, fit to the incoming
		// table structure, and the incoming types are feasible for the node
		// to execute. If the node can execute in its current state return
		// the spec of its output data table(s) (if you can, otherwise an array
		// with null elements), or throw an exception with a useful user message
		return new DataTableSpec[]{null, null};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec) throws Exception {

		logger.info("Read the scans xir data.");
		File file = new File(SETTING_XIR_FILE_INPUT.getStringValue());
		try {
			IProcessingInfo processingInfo = ScanConverterXIR.convert(file, new NullProgressMonitor());
			IScanXIR scanXIR = (IScanXIR)processingInfo.getProcessingResult();
			BufferedDataTable bufferedDataTable;
			if(SETTING_XIR_TABLE_OUTPUT.getStringValue().equals(ROTATION_ANGLE)) {
				bufferedDataTable = new ScanXirTableTranslator().getBufferedDataTable(scanXIR, exec);
			} else if(SETTING_XIR_TABLE_OUTPUT.getStringValue().equals(RAW_DATA)) {
				bufferedDataTable = new ScanXirTableTranslatorRawData().getBufferedDataTable(scanXIR, exec);
			} else {
				bufferedDataTable = new ScanXirTableTranslatorBackgroundSignals().getBufferedDataTable(scanXIR, exec);
			}
			BufferedDataTable bufferedDataTableHeaders = TableTranslator.headerTranslator(scanXIR, exec);
			return new BufferedDataTable[]{bufferedDataTable, bufferedDataTableHeaders};
		} catch(Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

		SETTING_XIR_FILE_INPUT.loadSettingsFrom(settings);
		SETTING_XIR_TABLE_OUTPUT.loadSettingsFrom(settings);
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

		SETTING_XIR_FILE_INPUT.saveSettingsTo(settings);
		SETTING_XIR_TABLE_OUTPUT.saveSettingsTo(settings);
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

		SETTING_XIR_FILE_INPUT.validateSettings(settings);
		SETTING_XIR_TABLE_OUTPUT.validateSettings(settings);
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}
}
