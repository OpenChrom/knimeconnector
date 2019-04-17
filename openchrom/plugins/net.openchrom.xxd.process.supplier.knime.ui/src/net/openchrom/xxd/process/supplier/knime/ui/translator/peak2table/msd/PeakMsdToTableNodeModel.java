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
package net.openchrom.xxd.process.supplier.knime.ui.translator.peak2table.msd;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
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

import net.openchrom.xxd.process.supplier.knime.model.table.msd.PeaksMSDTableTranslator;
import net.openchrom.xxd.process.supplier.knime.portobject.ChromatogramSelectionMSDPortObject;
import net.openchrom.xxd.process.supplier.knime.portobject.PortObjectSupport;

public class PeakMsdToTableNodeModel extends NodeModel {

	protected static final boolean DEF_USE_TIC = false;
	private static final NodeLogger logger = NodeLogger.getLogger(PeakMsdToTableNodeModel.class);
	//

	protected PeakMsdToTableNodeModel() {

		super(new PortType[]{PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionMSDPortObject.class)}, new PortType[]{PortTypeRegistry.getInstance().getPortType(BufferedDataTable.class)});
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return null;
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		ChromatogramSelectionMSDPortObject chromatogramSelectionMSDPortObject = PortObjectSupport.getChromatogramSelectionMSDPortObject(inObjects);
		if(chromatogramSelectionMSDPortObject != null) {
			/*
			 * Convert the selection to table.
			 */
			logger.info("Convert chromatogram selection to table.");
			List<IChromatogramPeakMSD> peaks = chromatogramSelectionMSDPortObject.getChromatogramSelectionMSD().getChromatogramMSD().getPeaks();
			BufferedDataTable bufferedDataTable = new PeaksMSDTableTranslator().getBufferedDataTable(peaks, exec);
			//
			return new PortObject[]{bufferedDataTable};
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
	protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {

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
	protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {

	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

	}
}
