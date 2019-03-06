/*******************************************************************************
 * Copyright (c) 2018, 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.process.supplier.knime.ui.node.listfilesfolder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.chemclipse.converter.core.ISupplier;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.util.FileUtil;

import net.openchrom.process.supplier.knime.support.TableTranslator;

public class ListFilesFolderNodeModel extends NodeModel {

	static SettingsModelBoolean getSettingRecursive() {

		return new SettingsModelBoolean(RECURSIVE, false);
	}

	static SettingsModelString getSettingSupplierID() {

		return new SettingsModelString(SUPPLIER_ID, "");
	}

	static SettingsModelString getSettingInputFolder() {

		return new SettingsModelString(INPUT_FOLDER, "");
	}

	private static final String INPUT_FOLDER = "INPUT_FOLDER";
	private static final String SUPPLIER_ID = "SUPPLIER_ID";
	private static final String RECURSIVE = "RECURSIVE";
	private SettingsModelBoolean recursive;
	private SettingsModelString supplierID;
	private SettingsModelString folder;
	private List<ISupplier> suppliers;

	public ListFilesFolderNodeModel(List<ISupplier> suppliers) {

		super(0, 1);
		this.suppliers = suppliers;
		this.folder = getSettingInputFolder();
		this.supplierID = getSettingSupplierID();
		this.recursive = getSettingRecursive();
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {

		return new DataTableSpec[]{TableTranslator.fileTableSpecific()};
	}

	@Override
	protected BufferedDataTable[] execute(BufferedDataTable[] inData, ExecutionContext exec) throws Exception {

		Optional<ISupplier> supplier = suppliers.stream().filter(s -> s.getId().equals(supplierID.getStringValue())).findAny();
		List<File> files = new ArrayList<>();
		File parentFile = FileUtil.resolveToPath(new URL(folder.getStringValue())).toFile();
		if(supplier.isPresent() && parentFile.isDirectory() && recursive.getBooleanValue()) {
			findFiles(parentFile, files, supplier.get(), exec);
		}
		BufferedDataTable table = TableTranslator.filesToTable(files, TableTranslator.fileTableSpecific(), exec);
		return new BufferedDataTable[]{table};
	}

	private void findFiles(File parentFolder, List<File> files, ISupplier suplier, ExecutionContext exec) throws CanceledExecutionException {

		File[] childrenFiles = parentFolder.listFiles();
		for(File file : childrenFiles) {
			exec.checkCanceled();
			if(suplier.isMatchMagicNumber(file)) {
				files.add(file);
			} else if(file.isDirectory()) {
				findFiles(file, files, suplier, exec);
			}
		}
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {

		recursive.saveSettingsTo(settings);
		supplierID.saveSettingsTo(settings);
		folder.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

		recursive.validateSettings(settings);
		supplierID.validateSettings(settings);
		folder.validateSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {

		recursive.loadSettingsFrom(settings);
		supplierID.loadSettingsFrom(settings);
		folder.loadSettingsFrom(settings);
	}

	@Override
	protected void reset() {

	}
}
