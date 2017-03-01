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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.chemclipse.msd.converter.chromatogram.ChromatogramConverterMSD;
import org.eclipse.core.runtime.NullProgressMonitor;
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
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import net.openchrom.xxd.process.supplier.knime.ui.model.msd.IVendorChromatogramMSD;
import net.openchrom.xxd.process.supplier.knime.ui.model.msd.IVendorScanMSD;
import net.openchrom.xxd.process.supplier.knime.ui.model.msd.VendorChromatogramMSD;
import net.openchrom.xxd.process.supplier.knime.ui.model.msd.VendorIon;
import net.openchrom.xxd.process.supplier.knime.ui.model.msd.VendorScanMSD;

/**
 * This is the model implementation of ChromatogramWriterMSD.
 * This node writes chromatographic data.
 */
public class ChromatogramWriterMSDNodeModel extends NodeModel {

	private static final NodeLogger logger = NodeLogger.getLogger(ChromatogramWriterMSDNodeModel.class);
	//
	private static final String CHROMATOGRAM_FILE_OUTPUT = "ChromatgramFileOutput";
	protected static final SettingsModelString SETTING_CHROMATOGRAM_FILE_OUTPUT = new SettingsModelString(CHROMATOGRAM_FILE_OUTPUT, "");
	/*
	 * Export the data in *.ocb format.
	 */
	protected static final String EXPORT_FILE_EXTENSION = ".ocb";
	private static final String EXPORT_CONVERTER_ID = "org.eclipse.chemclipse.xxd.converter.supplier.chemclipse";
	private static final double DEFAULT_MZ = 18.0d;

	/**
	 * Constructor for the node model.
	 */
	protected ChromatogramWriterMSDNodeModel() {
		super(1, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec) throws Exception {

		logger.info("Read the chromatographic raw data.");
		/*
		 * Write the chromatogram.
		 */
		if(inData.length > 0) {
			/*
			 * Export *.ocb file.
			 */
			File file = new File(SETTING_CHROMATOGRAM_FILE_OUTPUT.getStringValue());
			IVendorChromatogramMSD chromatogramMSD = new VendorChromatogramMSD();
			//
			BufferedDataTable bufferedDataTable = inData[0];
			DataTableSpec dataTableSpec = bufferedDataTable.getSpec();
			Map<Integer, Double> mzTable = new HashMap<Integer, Double>();
			//
			CloseableRowIterator iterator = bufferedDataTable.iterator();
			int scan = 1;
			int scanCount = getNumberOfRows(bufferedDataTable);
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
				exec.setProgress(scan / scanCount, "Exporting Scan: " + scan);
				scan++;
			}
			/*
			 * Write the chromatogram.
			 */
			ChromatogramConverterMSD.convert(file, chromatogramMSD, EXPORT_CONVERTER_ID, new NullProgressMonitor());
		}
		return null;
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
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {

		return null;
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
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

		SETTING_CHROMATOGRAM_FILE_OUTPUT.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		SETTING_CHROMATOGRAM_FILE_OUTPUT.validateSettings(settings);
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
