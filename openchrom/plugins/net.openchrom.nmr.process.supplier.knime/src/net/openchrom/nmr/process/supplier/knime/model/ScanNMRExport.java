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
package net.openchrom.nmr.process.supplier.knime.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.nmr.converter.core.ScanConverterNMR;
import org.eclipse.chemclipse.nmr.model.core.IScanNMR;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import net.openchrom.process.supplier.knime.model.AbstractDataExport;

public class ScanNMRExport extends AbstractDataExport<IScanNMR> {

	private static final int INTERNAL_VERSION_ID = 1;

	public ScanNMRExport(String id, File directory) {

		super(id, directory);
	}

	@Override
	public IProcessingInfo process(IScanNMR scanNmr, IProgressMonitor monitor) throws Exception {

		String filePath = generateFilePath(scanNmr);
		return ScanConverterNMR.convert(new File(filePath), scanNmr, getId(), new NullProgressMonitor());
	}

	@Override
	public String getName() {

		try {
			return ScanConverterNMR.getScanConverterSupport().getSupplier(getId()).getFilterName();
		} catch(NoConverterAvailableException e) {
		}
		return null;
	}

	@Override
	public String getDescription() {

		try {
			return ScanConverterNMR.getScanConverterSupport().getSupplier(getId()).getDescription();
		} catch(NoConverterAvailableException e) {
		}
		return null;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

		super.readExternal(in);
		int version = in.read();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {

		super.writeExternal(out);
		out.writeInt(INTERNAL_VERSION_ID);
	}

	public static List<ScanNMRExport> readString(String data) throws IOException, ClassNotFoundException {

		List<ScanNMRExport> reports = new ArrayList<>();
		byte[] byteData = Base64.getDecoder().decode(data);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(byteData));
		int size = ois.readInt();
		for(int i = 0; i < size; i++) {
			reports.add((ScanNMRExport)ois.readObject());
		}
		ois.close();
		return reports;
	}

	public static String writeToString(List<ScanNMRExport> reports) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		oos = new ObjectOutputStream(baos);
		oos.writeInt(reports.size());
		for(Object report : reports) {
			oos.writeObject(report);
		}
		oos.close();
		return Base64.getEncoder().encodeToString(baos.toByteArray());
	}
}
