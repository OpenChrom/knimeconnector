/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;

import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;

public class ChromatogramSelectionMSDProcessingPortObject extends ChromatogramSelectionMSDPortObject {

	private static final String CHROMATOGRAM_SELECTION_PROCESSING = "CHROMATOGRAM_SELECTION_PROCESSING";
	private List<IChromatogramSelectionProcessing<? super IChromatogramSelectionMSD>> processing = new ArrayList<>();

	public ChromatogramSelectionMSDProcessingPortObject() {
		super();
	}

	public ChromatogramSelectionMSDProcessingPortObject(IChromatogramSelectionMSD chromatogramSelectionMSD) {
		super(chromatogramSelectionMSD);
	}

	public void addProcessings(IChromatogramSelectionProcessing<? super IChromatogramSelectionMSD> chromatogramSelectionProcessing) {

		processing.add(chromatogramSelectionProcessing);
	}

	public List<IChromatogramSelectionProcessing<? super IChromatogramSelectionMSD>> getProcessings() {

		return Collections.unmodifiableList(processing);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void load(PortObjectZipInputStream in, PortObjectSpec spec, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

		super.load(in, spec, exec);
		ZipEntry zipEntry = in.getNextEntry();
		assert zipEntry.getName().equals(CHROMATOGRAM_SELECTION_PROCESSING);
		ObjectInputStream objectInputStream = new ObjectInputStream(in);
		int size = objectInputStream.readInt();
		for(int i = 0; i < size; i++) {
			try {
				Object object = objectInputStream.readObject();
				processing.add((IChromatogramSelectionProcessing<? super IChromatogramSelectionMSD>)object);
			} catch(ClassNotFoundException e) {
				throw new IOException(e);
			}
		}
	}

	@Override
	protected void save(PortObjectZipOutputStream out, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

		super.save(out, exec);
		ZipEntry zipEntry = new ZipEntry(CHROMATOGRAM_SELECTION_PROCESSING);
		out.putNextEntry(zipEntry);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
		objectOutputStream.writeInt(processing.size());
		for(int i = 0; i < processing.size(); i++) {
			objectOutputStream.writeObject(processing.get(i));
		}
	}
}
