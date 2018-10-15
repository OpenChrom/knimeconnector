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
 * Jan Holy - implementation
 *******************************************************************************/
package net.openchrom.xir.process.supplier.knime.ui.translator;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.xir.model.core.IScanXIR;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

import net.openchrom.xir.process.supplier.knime.portobject.PortObjectSupport;
import net.openchrom.xir.process.supplier.knime.portobject.ScanXIRPortObject;

public class CsXirToTableNodeModel extends NodeModel {

	protected static final boolean DEF_USE_RAW_DATA = false;
	private static final NodeLogger logger = NodeLogger.getLogger(CsXirToTableNodeModel.class);
	//
	protected static final String USE_RAW_DATA = "Raw Data";
	//
	private final SettingsModelBoolean settingsModelUseTic = new SettingsModelBoolean(USE_RAW_DATA, DEF_USE_RAW_DATA);

	protected CsXirToTableNodeModel() {

		super(new PortType[]{PortTypeRegistry.getInstance().getPortType(ScanXIRPortObject.class)}, new PortType[]{PortTypeRegistry.getInstance().getPortType(BufferedDataTable.class)});
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return null;
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		ScanXIRPortObject scanNMRPortObject = PortObjectSupport.getScanXIRPortObject(inObjects);
		if(scanNMRPortObject != null) {
			/*
			 * Convert the selection to table.
			 */
			logger.info("Convert XIR scan to table.");
			IScanXIR scan = scanNMRPortObject.getScanXIR();
			BufferedDataTable bufferedDataTable = null;
			//
			return new PortObject[]{bufferedDataTable};
		} else {
			/*
			 * If things have gone wrong.
			 */
			throw new Exception("no input scan");
		}
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

		settingsModelUseTic.loadSettingsFrom(settings);
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

		settingsModelUseTic.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		settingsModelUseTic.validateSettings(settings);
	}
}