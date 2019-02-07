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
package net.openchrom.nmr.process.supplier.knime.ui.brukerprojectexplorer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.chemclipse.converter.core.ISupplier;
import org.eclipse.chemclipse.nmr.converter.core.ScanConverterNMR;
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
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import net.openchrom.process.supplier.knime.support.TableTranslator;

/**
 * This is the model implementation of MeasurementReaderNMR.
 * This node is reads chromatographic raw data.
 *
 * @author OpenChrom
 */
public class BrukerProjectExplorerNodeModel extends NodeModel {

	/**
	 * the settings key which is used to retrieve and
	 * store the settings (from the dialog or from a settings file)
	 * (package visibility to be usable from the dialog).
	 */
	/**
	 * Constructor for the node model.
	 */
	private static final NodeLogger logger = NodeLogger.getLogger(BrukerProjectExplorerNodeModel.class);
	private static final String NMR_FILE_INPUT = "FileInput";
	private static final String LOAD_RAW_FILE = "LoadRawFile";
	private SettingsModelString settingsFolderInput;
	private SettingsModelBoolean settingsLoadRawFile;

	protected BrukerProjectExplorerNodeModel() {

		// TODO one incoming port and one outgoing port is assumed
		super(0, 1);
		settingsFolderInput = getSettingsFileInput();
		settingsLoadRawFile = getSettingsLoadRawFile();
	}

	@Override
	protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {

		return new DataTableSpec[]{TableTranslator.fileTableSpecific()};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(BufferedDataTable[] dataIn, ExecutionContext exec) throws Exception {

		logger.info("Read the scans nmr data.");
		File file = new File(settingsFolderInput.getStringValue());
		try {
			/*
			 * find all project
			 */
			List<File> outPutFiles = new ArrayList<>();
			Set<File> projectFiles = new HashSet<>();
			//
			List<File> filesFid = new ArrayList<>();
			ISupplier suplierFid = ScanConverterNMR.getScanConverterSupport().getSupplier("net.openchrom.nmr.converter.supplier.bruker.raw.fid");
			ISupplier suplier1r = ScanConverterNMR.getScanConverterSupport().getSupplier("net.openchrom.nmr.converter.supplier.bruker.raw.1r");
			findProject(file, filesFid, suplierFid, exec);
			ISupplier supplier;
			if(settingsLoadRawFile.getBooleanValue()) {
				supplier = suplierFid;
			} else {
				supplier = suplier1r;
			}
			for(File fileFid : filesFid) {
				File parantFileFid = fileFid.getParentFile();
				File projectFile = parantFileFid.getParentFile();
				if(projectFile != null) {
					projectFiles.add(projectFile);
				} else {
					List<File> fileData = selectFile(supplier, new File[]{parantFileFid}, exec);
					outPutFiles.addAll(fileData);
				}
			}
			for(File projectFile : projectFiles) {
				File[] childrenFiles = projectFile.listFiles();
				List<File> selectedDataFiles = selectFile(supplier, childrenFiles, exec);
				outPutFiles.addAll(selectedDataFiles);
			}
			/*
			 * find all project which
			 */
			return new BufferedDataTable[]{TableTranslator.filesToTable(outPutFiles, TableTranslator.fileTableSpecific(), exec)};
		} catch(Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}

	private List<File> selectFile(ISupplier supplier, File[] subProjectFiles, ExecutionContext exec) {

		File selectedFile = null;
		List<File> selectedData = new ArrayList<>();
		for(File subProjectFile : subProjectFiles) {
			List<File> dataFiles = new ArrayList<>();
			findProject(subProjectFile, dataFiles, supplier, exec);
			if(dataFiles.size() > 0) {
				if(selectedFile == null) {
					selectedFile = subProjectFile;
					selectedData = dataFiles;
				} else {
					try {
						int newFile = Integer.parseInt(subProjectFile.getName());
						int oldFile = Integer.parseInt(selectedFile.getName());
						if(newFile < oldFile) {
							selectedFile = subProjectFile;
							selectedData = dataFiles;
						}
					} catch(NumberFormatException e) {
						// TODO: handle exception
					}
				}
			}
		}
		return selectedData;
	}

	private void findProject(File parentFolder, List<File> files, ISupplier suplier, ExecutionContext exec) {

		File[] childrenFiles = parentFolder.listFiles();
		for(File file : childrenFiles) {
			if(suplier.isMatchMagicNumber(file)) {
				files.add(file);
			} else if(file.isDirectory()) {
				findProject(file, files, suplier, exec);
			}
		}
	}

	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {

		// TODO load internal data.
		// Everything handed to output ports is loaded automatically (data
		// returned by the execute method, models loaded in loadModelContent,
		// and user settings set through loadSettingsFrom - is all taken care
		// of). Load here only the other internals that need to be restored
		// (e.g. data used by the views).
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

		settingsFolderInput.loadSettingsFrom(settings);
		settingsLoadRawFile.loadSettingsFrom(settings);
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

		settingsFolderInput.saveSettingsTo(settings);
		settingsLoadRawFile.saveSettingsTo(settings);
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

		settingsFolderInput.validateSettings(settings);
		settingsLoadRawFile.validateSettings(settings);
	}

	static SettingsModelString getSettingsFileInput() {

		return new SettingsModelString(NMR_FILE_INPUT, "");
	}

	static SettingsModelBoolean getSettingsLoadRawFile() {

		return new SettingsModelBoolean(LOAD_RAW_FILE, false);
	}
}
