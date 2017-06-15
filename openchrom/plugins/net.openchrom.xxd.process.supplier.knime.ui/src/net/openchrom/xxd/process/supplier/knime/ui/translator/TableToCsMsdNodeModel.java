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
package net.openchrom.xxd.process.supplier.knime.ui.translator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.container.CloseableRowIterator;
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

import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionMSDPortObject;
import net.openchrom.xxd.process.supplier.knime.model.PortObjectSupport;
import net.openchrom.xxd.process.supplier.knime.ui.model.msd.IVendorChromatogramMSD;
import net.openchrom.xxd.process.supplier.knime.ui.model.msd.IVendorScanMSD;
import net.openchrom.xxd.process.supplier.knime.ui.model.msd.VendorChromatogramMSD;
import net.openchrom.xxd.process.supplier.knime.ui.model.msd.VendorIon;
import net.openchrom.xxd.process.supplier.knime.ui.model.msd.VendorScanMSD;

public class TableToCsMsdNodeModel extends NodeModel {

	private static final NodeLogger logger = NodeLogger.getLogger(TableToCsMsdNodeModel.class);
	private static final double DEFAULT_MZ = 18.0d;

	protected TableToCsMsdNodeModel() {
		super(new PortType[]{PortTypeRegistry.getInstance().getPortType(BufferedDataTable.class)}, new PortType[]{PortTypeRegistry.getInstance().getPortType(ChromatogramSelectionMSDPortObject.class)});
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		BufferedDataTable bufferedDataTable = PortObjectSupport.getBufferedDataTable(inObjects);
		if(bufferedDataTable != null) {
			/*
			 * Convert the table to chromatogram selection.
			 */
			logger.info("Convert the buffered data table to chromatogram selection");
			IChromatogramMSD chromatogramMSD = getChromatogramMSD(bufferedDataTable, exec);
			IChromatogramSelectionMSD chromatogramSelectionMSD = new ChromatogramSelectionMSD(chromatogramMSD);
			ChromatogramSelectionMSDPortObject chromatogramSelectionMSDPortObject = new ChromatogramSelectionMSDPortObject(chromatogramSelectionMSD);
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
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

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

	private IChromatogramMSD getChromatogramMSD(BufferedDataTable bufferedDataTable, final ExecutionContext exec) throws Exception {

		DataTableSpec dataTableSpec = bufferedDataTable.getSpec();
		Map<Integer, Double> mzTable = new HashMap<Integer, Double>();
		//
		CloseableRowIterator iterator = bufferedDataTable.iterator();
		int scan = 1;
		int scanCount = getNumberOfRows(bufferedDataTable);
		//
		IVendorChromatogramMSD chromatogramMSD = new VendorChromatogramMSD();
		//
		while(iterator.hasNext()) {
			DataRow dataRow = iterator.next();
			//
			IVendorScanMSD vendorScanMSD = new VendorScanMSD();
			vendorScanMSD.setRetentionTime(Integer.parseInt(dataRow.getCell(0).toString()));
			vendorScanMSD.setRetentionIndex(Float.parseFloat(dataRow.getCell(1).toString()));
			//
			int numberOfCells = dataRow.getNumCells();
			if(numberOfCells == 3) {
				/*
				 * TIC
				 */
				vendorScanMSD.addIon(new VendorIon(DEFAULT_MZ, Float.parseFloat(dataRow.getCell(2).toString())));
			} else {
				/*
				 * XIC
				 */
				for(int i = 2; i < numberOfCells; i++) {
					float abundance = Float.parseFloat(dataRow.getCell(i).toString());
					if(abundance > 0) {
						double mz;
						if(mzTable.containsKey(i)) {
							mz = mzTable.get(i);
						} else {
							mz = Double.parseDouble(dataTableSpec.getColumnSpec(i).getName().toString());
							mzTable.put(i, mz);
						}
						/*
						 * Add the ion.
						 */
						if(mz > 0) {
							vendorScanMSD.addIon(new VendorIon(mz, abundance));
						}
					}
				}
			}
			chromatogramMSD.addScan(vendorScanMSD);
			//
			exec.checkCanceled();
			exec.setProgress(scan / scanCount, "Get scan from table: " + scan);
			scan++;
		}
		//
		return chromatogramMSD;
	}

	private int getNumberOfRows(BufferedDataTable bufferedDataTable) {

		int counter = 0;
		CloseableRowIterator iterator = bufferedDataTable.iterator();
		while(iterator.hasNext()) {
			iterator.next();
			counter++;
		}
		return counter;
	}
}
