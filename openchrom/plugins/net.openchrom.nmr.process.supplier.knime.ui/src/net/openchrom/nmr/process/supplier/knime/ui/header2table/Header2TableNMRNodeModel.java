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
package net.openchrom.nmr.process.supplier.knime.ui.header2table;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.nmr.model.core.IScanNMR;
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
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

import net.openchrom.nmr.process.supplier.knime.portobject.PortObjectSupport;
import net.openchrom.nmr.process.supplier.knime.portobject.ScanNMRPortObject;
import net.openchrom.process.supplier.knime.support.TableTranslator;

/**
 * This is the model implementation of MeasurementWriterNMR.
 * This node writes chromatographic data.
 *
 * @author OpenChrom
 */
public class Header2TableNMRNodeModel extends NodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger.getLogger(Header2TableNMRNodeModel.class);
	// example value: the models count variable filled from the dialog
	// and used in the models execution method. The default components of the
	// dialog work with "SettingsModels".
	private DataTableSpec dataTableSpecHeader = TableTranslator.headerTranslatorTableSpec();

	/**
	 * file
	 * Constructor for the node model.
	 */
	protected Header2TableNMRNodeModel() {

		// TODO one incoming port and one outgoing port is assumed
		super(new PortType[]{PortTypeRegistry.getInstance().getPortType(ScanNMRPortObject.class)}, new PortType[]{PortTypeRegistry.getInstance().getPortType(BufferedDataTable.class)});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return new PortObjectSpec[]{dataTableSpecHeader};
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		ScanNMRPortObject scanNMRPortObject = PortObjectSupport.getScanNMRPortObject(inObjects);
		/*
		 * Convert the selection to table.
		 */
		logger.info("Convert NMR scan to table.");
		IScanNMR scan = scanNMRPortObject.getScanNMR();
		BufferedDataTable bufferedDataTable = TableTranslator.headerTranslator(scan, dataTableSpecHeader, exec);
		//
		return new PortObject[]{bufferedDataTable};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

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

	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}
}
