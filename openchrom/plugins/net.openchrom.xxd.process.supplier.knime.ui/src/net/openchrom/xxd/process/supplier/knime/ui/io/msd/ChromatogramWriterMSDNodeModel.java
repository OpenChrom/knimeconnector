/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
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
package net.openchrom.xxd.process.supplier.knime.ui.io.msd;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.msd.converter.chromatogram.ChromatogramConverterMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
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

import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionMSDPortObject;
import net.openchrom.xxd.process.supplier.knime.model.PortObjectSupport;

/**
 * This is the model implementation of ChromatogramWriterMSD.
 * This node writes chromatographic data.
 */
public class ChromatogramWriterMSDNodeModel extends NodeModel {

	private static final String EXPORT_CONVERTER_ID = "org.eclipse.chemclipse.xxd.converter.supplier.chemclipse";
	/*
	 * Export the data in *.ocb format.
	 */
	protected static final String EXPORT_FILE_EXTENSION = ".ocb";
	//
	private static final String CHROMATOGRAM_FILE_OUTPUT = "ChromatgramFileOutput";
	private static final NodeLogger logger = NodeLogger.getLogger(ChromatogramWriterMSDNodeModel.class);
	protected static final SettingsModelString SETTING_CHROMATOGRAM_FILE_OUTPUT = new SettingsModelString(CHROMATOGRAM_FILE_OUTPUT, "");

	/**
	 * Constructor for the node model.
	 */
	protected ChromatogramWriterMSDNodeModel() {
		super(new PortType[]{PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionMSDPortObject.class)}, new PortType[]{});
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return null;
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		logger.info("Read the chromatographic raw data.");
		//
		ChromatogramSelectionMSDPortObject chromatogramSelectionMSDPortObject = PortObjectSupport.getChromatogramSelectionMSDPortObject(inObjects);
		if(chromatogramSelectionMSDPortObject != null) {
			File file = new File(SETTING_CHROMATOGRAM_FILE_OUTPUT.getStringValue());
			IChromatogramMSD chromatogramMSD = chromatogramSelectionMSDPortObject.getChromatogramSelectionMSD().getChromatogramMSD();
			ChromatogramConverterMSD.convert(file, chromatogramMSD, EXPORT_CONVERTER_ID, new NullProgressMonitor());
		}
		//
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

		SETTING_CHROMATOGRAM_FILE_OUTPUT.loadSettingsFrom(settings);
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

		SETTING_CHROMATOGRAM_FILE_OUTPUT.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		SETTING_CHROMATOGRAM_FILE_OUTPUT.validateSettings(settings);
	}
}
