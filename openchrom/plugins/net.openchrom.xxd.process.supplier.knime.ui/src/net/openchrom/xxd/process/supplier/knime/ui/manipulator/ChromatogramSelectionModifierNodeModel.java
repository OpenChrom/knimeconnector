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
package net.openchrom.xxd.process.supplier.knime.ui.manipulator;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.model.core.AbstractChromatogram;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionMSDPortObject;
import net.openchrom.xxd.process.supplier.knime.model.PortObjectSupport;

public class ChromatogramSelectionModifierNodeModel extends NodeModel {

	private static final NodeLogger logger = NodeLogger.getLogger(ChromatogramSelectionModifierNodeModel.class);
	//
	protected static final String START_RETENTION_TIME = "Start Retention Time (Minutes)";
	protected static final double DEF_START_RETENTION_TIME = 0.0d;
	protected static final String STOP_RETENTION_TIME = "Stop Retention Time (Minutes)";
	protected static final double DEF_STOP_RETENTION_TIME = 10.0d;

	protected static SettingsModelDouble createSettingsModelStartRetentionTime() {

		return new SettingsModelDouble(START_RETENTION_TIME, DEF_START_RETENTION_TIME);
	}

	protected static SettingsModelDouble createSettingsModelStopRetentionTime() {

		return new SettingsModelDouble(STOP_RETENTION_TIME, DEF_STOP_RETENTION_TIME);
	}

	private final SettingsModelDouble settingsModelStartRetentionTime = createSettingsModelStartRetentionTime();
	private final SettingsModelDouble settingsModelStopRetentionTime = createSettingsModelStopRetentionTime();

	protected ChromatogramSelectionModifierNodeModel() {
		super(new PortType[]{PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionMSDPortObject.class)}, new PortType[]{PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionMSDPortObject.class)});
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		ChromatogramSelectionMSDPortObject chromatogramSelectionMSDPortObject = PortObjectSupport.getChromatogramSelectionMSDPortObject(inObjects);
		if(chromatogramSelectionMSDPortObject != null) {
			/*
			 * Apply the filter.
			 */
			IChromatogramSelectionMSD chromatogramSelection = chromatogramSelectionMSDPortObject.getChromatogramSelectionMSD();
			int startRetentionTime = (int)(settingsModelStartRetentionTime.getDoubleValue() * AbstractChromatogram.MINUTE_CORRELATION_FACTOR);
			int stopRetentionTime = (int)(settingsModelStopRetentionTime.getDoubleValue() * AbstractChromatogram.MINUTE_CORRELATION_FACTOR);
			logger.info("Apply the retention time range");
			logger.info("Modifier set range: " + (startRetentionTime / AbstractChromatogram.MINUTE_CORRELATION_FACTOR) + "\t->\t" + (stopRetentionTime / AbstractChromatogram.MINUTE_CORRELATION_FACTOR));
			chromatogramSelection.setStartRetentionTime(startRetentionTime);
			chromatogramSelection.setStopRetentionTime(stopRetentionTime);
			//
			return new PortObject[]{chromatogramSelectionMSDPortObject};
		} else {
			/*
			 * If things have gone wrong.
			 */
			return new PortObject[]{};
		}
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

		settingsModelStartRetentionTime.saveSettingsTo(settings);
		settingsModelStopRetentionTime.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

		settingsModelStartRetentionTime.loadSettingsFrom(settings);
		settingsModelStopRetentionTime.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		settingsModelStartRetentionTime.validateSettings(settings);
		settingsModelStopRetentionTime.validateSettings(settings);
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
}
