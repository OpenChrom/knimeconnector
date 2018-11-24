/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.nmr.process.supplier.knime.ui.process.start;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.chemclipse.nmr.converter.core.ScanConverterNMR;
import org.eclipse.chemclipse.nmr.model.core.IScanNMR;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.workflow.LoopStartNodeTerminator;

import net.openchrom.nmr.process.supplier.knime.portobject.ScanNMRPortObject;
import net.openchrom.nmr.process.supplier.knime.portobject.ScanNMRPortObjectSpec;
import net.openchrom.process.supplier.knime.filesportobject.FilePortObject;

public class ProcessControllerStartNodeModel extends NodeModel implements LoopStartNodeTerminator {

	private int interation = 0;
	private List<File> files;

	protected ProcessControllerStartNodeModel() {

		super(new PortType[]{FilePortObject.TYPE}, new PortType[]{ScanNMRPortObject.TYPE});
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		ScanNMRPortObjectSpec scanNMRPortObjectSpec = new ScanNMRPortObjectSpec();
		return new PortObjectSpec[]{scanNMRPortObjectSpec};
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		FilePortObject filePortObject = (FilePortObject)inObjects[0];
		files = filePortObject.getFiles();
		File file = files.get(interation);
		interation++;
		IProcessingInfo progressInfo = ScanConverterNMR.convert(file, new NullProgressMonitor());
		IScanNMR scanNMR = progressInfo.getProcessingResult(IScanNMR.class);
		return new PortObject[]{new ScanNMRPortObject(scanNMR)};
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {

	}

	@Override
	protected void reset() {

		interation = 0;
	}

	@Override
	public boolean terminateLoop() {

		if(files == null) {
			return true;
		} else {
			return interation >= files.size();
		}
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {

	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

	}
}
