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
import java.util.zip.ZipEntry;

import javax.swing.JComponent;

import org.eclipse.chemclipse.nmr.model.core.ScanNMR;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.port.AbstractPortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

public class ScanNMRPortObject extends AbstractPortObject {

	private static final String SCAN_NMR_DATA = "SCAN_NMR_DATA";
	private static final ScanNMR EMPTY_SCAN_NMR = new ScanNMR();

	public static final class Serializer extends AbstractPortObjectSerializer<ScanNMRPortObject> {
	}

	public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(ScanNMRPortObject.class);
	private ScanNMRPortObjectSpec portObjectSpec;
	private ScanNMR scanNMR;

	public ScanNMRPortObject() {
		this(EMPTY_SCAN_NMR);
	}

	public ScanNMRPortObject(ScanNMR scanNMR) {
		this.scanNMR = scanNMR;
		portObjectSpec = new ScanNMRPortObjectSpec();
	}

	@Override
	public String getSummary() {

		return "Scan NMR";
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

		ZipEntry zipEntry = new ZipEntry(SCAN_NMR_DATA);
		out.putNextEntry(zipEntry);
		ObjectOutputStream outputStream = new ObjectOutputStream(out);
		outputStream.writeObject(scanNMR);
		outputStream.close();
	}

	@Override
	protected void load(PortObjectZipInputStream in, PortObjectSpec spec, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

		ZipEntry zipEntry = in.getNextEntry();
		assert zipEntry.getName().equals(SCAN_NMR_DATA);
		ObjectInputStream inputStream = new ObjectInputStream(in);
		try {
			scanNMR = (ScanNMR)inputStream.readObject();
		} catch(ClassNotFoundException e) {
			throw new IOException(e);
		}
	}

	public ScanNMR getScanNMR() {

		return scanNMR;
	}
}
