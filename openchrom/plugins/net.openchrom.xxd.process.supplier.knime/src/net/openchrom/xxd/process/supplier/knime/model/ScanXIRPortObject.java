/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JComponent;

import org.eclipse.chemclipse.xir.model.core.IScanXIR;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.port.AbstractPortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;

public class ScanXIRPortObject extends AbstractPortObject {

	private ScanXIRPortObjectSpec portObjectSpec;
	private IScanXIR scanXIR;

	public ScanXIRPortObject(IScanXIR scanXIR) {
		portObjectSpec = new ScanXIRPortObjectSpec();
		this.scanXIR = scanXIR;
	}

	@Override
	public String getSummary() {

		return "Scan XIR";
	}

	@Override
	public PortObjectSpec getSpec() {

		return portObjectSpec;
	}

	@Override
	public JComponent[] getViews() {

		return null;
	}

	@Override
	protected void save(PortObjectZipOutputStream out, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

		ObjectOutputStream outputStream = new ObjectOutputStream(out);
		outputStream.writeObject(scanXIR);
		outputStream.close();
	}

	@Override
	protected void load(PortObjectZipInputStream in, PortObjectSpec spec, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

		ObjectInputStream inputStream = new ObjectInputStream(in);
		try {
			scanXIR = (IScanXIR)inputStream.readObject();
		} catch(ClassNotFoundException e) {
			throw new IOException(e);
		}
	}

	public IScanXIR getScanNMR() {

		return scanXIR;
	}
}
