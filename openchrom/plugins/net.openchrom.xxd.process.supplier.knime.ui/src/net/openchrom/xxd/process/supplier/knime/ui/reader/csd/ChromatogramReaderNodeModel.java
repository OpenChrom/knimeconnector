/*******************************************************************************
 * Copyright (c) 2017, 2019 Lablicate GmbH.
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
 * Alexander Kerner - Generics
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.reader.csd;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.csd.converter.chromatogram.ChromatogramConverterCSD;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.csd.model.core.selection.ChromatogramSelectionCSD;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
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

import net.openchrom.xxd.process.supplier.knime.portobject.ChromatogramSelectionCSDPortObject;

public class ChromatogramReaderNodeModel extends NodeModel {

	//
	private static final String CHROMATOGRAM_FILE_INPUT = "ChromatgramFileInput";
	private static final NodeLogger logger = NodeLogger.getLogger(ChromatogramReaderNodeModel.class);
	private SettingsModelString settingChromatogramFileInput;

	protected ChromatogramReaderNodeModel() {

		super(new PortType[]{}, new PortType[]{PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionCSDPortObject.class)});
		settingChromatogramFileInput = getSettingsChromatogamFileInput();
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return null;
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		logger.info("Read the chromatographic raw data.");
		//
		File file = new File(settingChromatogramFileInput.getStringValue());
		IChromatogramCSD chromatogramCSD = loadChromatogram(file);
		IChromatogramSelectionCSD chromatogramSelectionCSD = new ChromatogramSelectionCSD(chromatogramCSD);
		ChromatogramSelectionCSDPortObject portObjectSelectionCSD = new ChromatogramSelectionCSDPortObject(chromatogramSelectionCSD);
		return new PortObject[]{portObjectSelectionCSD};
	}

	private IChromatogramCSD loadChromatogram(File file) {

		IProcessingInfo<IChromatogramCSD> processingInfo = ChromatogramConverterCSD.getInstance().convert(file, new NullProgressMonitor());
		IChromatogramCSD chromatogramCSD = processingInfo.getProcessingResult();
		chromatogramCSD.setFile(file);
		return chromatogramCSD;
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

		settingChromatogramFileInput.loadSettingsFrom(settings);
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

		settingChromatogramFileInput.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		settingChromatogramFileInput.validateSettings(settings);
	}

	static SettingsModelString getSettingsChromatogamFileInput() {

		return new SettingsModelString(CHROMATOGRAM_FILE_INPUT, "");
	}
}
