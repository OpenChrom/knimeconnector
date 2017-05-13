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

import org.eclipse.chemclipse.model.selection.ChromatogramSelection;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.converter.chromatogram.ChromatogramConverterMSD;
import org.eclipse.chemclipse.msd.converter.processing.chromatogram.IChromatogramMSDImportConverterProcessingInfo;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
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
import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionPortObject;

public class ChromatogramReaderMSDNodeModel extends NodeModel {

	private static final NodeLogger logger = NodeLogger.getLogger(ChromatogramReaderMSDNodeModel.class);
	//
	private static final String CHROMATOGRAM_FILE_INPUT = "ChromatgramFileInput";
	protected static final SettingsModelString SETTING_CHROMATOGRAM_FILE_INPUT = new SettingsModelString(CHROMATOGRAM_FILE_INPUT, "");

	protected ChromatogramReaderMSDNodeModel() {
		super(new PortType[]{}, new PortType[]{PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionPortObject.class), PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionMSDPortObject.class)});
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		logger.info("Read the chromatographic raw data.");
		//
		IChromatogramMSD chromatogramMSD = loadChromatogram(SETTING_CHROMATOGRAM_FILE_INPUT.getStringValue());
		IChromatogramSelection chromatogramSelection = new ChromatogramSelection(chromatogramMSD);
		IChromatogramSelectionMSD chromatogramSelectionMSD = new ChromatogramSelectionMSD(chromatogramMSD);
		PortObject portObjectSelection = new ChromatogramSelectionPortObject(chromatogramSelection);
		PortObject portObjectSelectionMSD = new ChromatogramSelectionMSDPortObject(chromatogramSelectionMSD);
		return new PortObject[]{portObjectSelection, portObjectSelectionMSD};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {

	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		SETTING_CHROMATOGRAM_FILE_INPUT.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

		SETTING_CHROMATOGRAM_FILE_INPUT.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		SETTING_CHROMATOGRAM_FILE_INPUT.validateSettings(settings);
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
	protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	private IChromatogramMSD loadChromatogram(String pathChromatogram) {

		File file = new File(pathChromatogram);
		IChromatogramMSDImportConverterProcessingInfo processingInfo = ChromatogramConverterMSD.convert(file, new NullProgressMonitor());
		return processingInfo.getChromatogram();
	}
}
