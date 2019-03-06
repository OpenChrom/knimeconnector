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
 * Jan Holy - implentation
 *******************************************************************************/
package net.openchrom.nmr.process.supplier.knime.ui.brukerprojectexplorer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.chemclipse.converter.core.ISupplier;
import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
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
import org.knime.core.util.FileUtil;

import net.openchrom.nmr.process.supplier.knime.ui.supports.BrukerFileType;
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
	private static final String FILE_TYPE_IMPORT = "FileTypeImport";
	private static final String SELECT_MEASUREMENT_LOWEST_NUMBER = "SelectMeasurementLowestNumber";
	private SettingsModelString settingsFolderInput;
	private SettingsModelString settingsFileTypeImport;
	private SettingsModelBoolean settingsSelectOnlyLowestMeasumentNumber;

	protected BrukerProjectExplorerNodeModel() {

		// TODO one incoming port and one outgoing port is assumed
		super(0, 1);
		settingsFolderInput = getSettingsFileInput();
		settingsFileTypeImport = getSettingsFileTypeImport();
		settingsSelectOnlyLowestMeasumentNumber = getSettingsMeasurementsLowestNumber();
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

		Path path = FileUtil.resolveToPath(new URL(settingsFolderInput.getStringValue()));
		File file = path.toFile();
		try {
			List<File> fileToImport;
			if(settingsSelectOnlyLowestMeasumentNumber.getBooleanValue()) {
				/*
				 * find all measurement
				 */
				fileToImport = filteringFiles(file, exec);
			} else {
				/*
				 * find measurement with lowest number
				 */
				fileToImport = allFiles(file, exec);
			}
			return new BufferedDataTable[]{TableTranslator.filesToTable(fileToImport, TableTranslator.fileTableSpecific(), exec)};
		} catch(Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}

	private List<File> allFiles(File file, ExecutionContext exec) throws NoConverterAvailableException {

		List<File> outPutFiles = new ArrayList<>();
		ISupplier supplier = getSupplier();
		findProject(file, outPutFiles, supplier, exec);
		return outPutFiles;
	}

	private List<File> filteringFiles(File file, ExecutionContext exec) throws NoConverterAvailableException {

		List<File> outPutFiles = new ArrayList<>();
		Set<File> projectFiles = new HashSet<>();
		//
		List<File> filesFid = new ArrayList<>();
		ISupplier suplierFid = ScanConverterNMR.getScanConverterSupport().getSupplier("net.openchrom.nmr.converter.supplier.bruker.raw.fid");
		ISupplier suplier1r = ScanConverterNMR.getScanConverterSupport().getSupplier("net.openchrom.nmr.converter.supplier.bruker.raw.1r");
		findProject(file, filesFid, suplierFid, exec);
		ISupplier supplier;
		supplier = getSupplier();
		if(filesFid.isEmpty() && supplier.getId().equals(suplier1r.getId())) {
			List<File> files1r = new ArrayList<>();
			findProject(file, files1r, supplier, exec);
			outPutFiles.addAll(files1r);
		} else {
			for(File fileFid : filesFid) {
				File parantFileFid = fileFid.getParentFile();
				File projectFile = parantFileFid.getParentFile();
				if(projectFile == null || file.equals(parantFileFid)) {
					List<File> dataFiles = new ArrayList<>();
					findProject(parantFileFid, dataFiles, supplier, exec);
					outPutFiles.addAll(dataFiles);
				} else {
					projectFiles.add(projectFile);
				}
			}
			for(File projectFile : projectFiles) {
				File[] childrenFiles = projectFile.listFiles();
				List<File> selectedDataFiles = selectFile(supplier, childrenFiles, exec);
				outPutFiles.addAll(selectedDataFiles);
			}
		}
		return outPutFiles;
	}

	private ISupplier getSupplier() throws NoConverterAvailableException {

		ISupplier supplier;
		BrukerFileType fileType = BrukerFileType.valueOf(settingsFileTypeImport.getStringValue());
		switch(fileType) {
			case FILE_1R:
				return ScanConverterNMR.getScanConverterSupport().getSupplier("net.openchrom.nmr.converter.supplier.bruker.raw.1r");
			case FILE_FID:
				return ScanConverterNMR.getScanConverterSupport().getSupplier("net.openchrom.nmr.converter.supplier.bruker.raw.fid");
			default:
				throw new IllegalArgumentException(fileType.toString() + " is not supported");
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
		if(childrenFiles == null) {
			// skip files
			return;
		}
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
		settingsFileTypeImport.loadSettingsFrom(settings);
		settingsSelectOnlyLowestMeasumentNumber.loadSettingsFrom(settings);
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
		settingsFileTypeImport.saveSettingsTo(settings);
		settingsSelectOnlyLowestMeasumentNumber.saveSettingsTo(settings);
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

		settingsFolderInput.validateSettings(settings);
		settingsFileTypeImport.validateSettings(settings);
		settingsSelectOnlyLowestMeasumentNumber.validateSettings(settings);
	}

	static SettingsModelString getSettingsFileInput() {

		return new SettingsModelString(NMR_FILE_INPUT, "");
	}

	static SettingsModelString getSettingsFileTypeImport() {

		return new SettingsModelString(FILE_TYPE_IMPORT, BrukerFileType.FILE_1R.name());
	}

	static SettingsModelBoolean getSettingsMeasurementsLowestNumber() {

		return new SettingsModelBoolean(SELECT_MEASUREMENT_LOWEST_NUMBER, true);
	}
}
