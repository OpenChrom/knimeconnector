/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *Alexander Kerner - initial API and implementation
 *******************************************************************************/
package net.openchrom.knime.node.base;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;

import javax.swing.JComponent;

import org.eclipse.chemclipse.model.core.IMeasurement;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.port.AbstractPortObject;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;

/**
 * Base class for {@link PortObject}s that hold collections of
 * {@link IMeasurement}.
 * 
 * @author Alexander Kerner
 *
 * @param <T>
 *            type of {@link IMeasurement}
 */
public abstract class GenericPortObject<T extends IMeasurement> extends AbstractPortObject {

	private final static String summary = "OpenChrom Measurement";

	private final GenericPortObjectSpec portObjectSpec;

	private final List<T> measurements;

	public GenericPortObject(final Collection<? extends T> measurements, final GenericPortObjectSpec portObjectSpec) {
		this.measurements = new ArrayList<>(measurements);
		this.portObjectSpec = Objects.requireNonNull(portObjectSpec);
	}

	public GenericPortObject(final Collection<? extends T> measurements) {
		this(measurements, new GenericPortObjectSpec());
	}

	public GenericPortObject() {
		this(new ArrayList<>(0), new GenericPortObjectSpec());
	}

	public List<T> getMeasurements() {
		return measurements;
	}

	@Override
	public String getSummary() {
		return summary;
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
	protected void save(final PortObjectZipOutputStream out, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		final ZipEntry zipEntry = new ZipEntry(this.getClass().getSimpleName());
		out.putNextEntry(zipEntry);
		if (measurements != null) {
			out.write(measurements.size());
			final ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
			for (final T m : measurements) {
				objectOutputStream.writeObject(m);
		// objectOutputStream.writeObject(m.getHeaderDataMap());
			}
			objectOutputStream.flush();
		} else {
			out.write(0);
		}

		out.closeEntry();

	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	protected void load(final PortObjectZipInputStream in, final PortObjectSpec spec, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {

		// specs currently not needed
		final GenericPortObjectSpec fidSpec = (GenericPortObjectSpec) spec;

		Thread t = Thread.currentThread();
		ClassLoader ccl = t.getContextClassLoader();
		try {
			t.setContextClassLoader(getClass().getClassLoader());

			final ZipEntry zipEntry = in.getNextEntry();
			final int numMeasurements = in.read();
			final ObjectInputStream objectInputStream = new ObjectInputStream(in) {
				@Override
				protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
					// System.err.println("Resolving class " + desc);
					return super.resolveClass(desc);
				}
			};
			for (int i = 0; i < numMeasurements; i++) {
				try {
					Object o = objectInputStream.readObject();
					final T m = (T) o;
					measurements.add(m);
				} catch (final ClassNotFoundException e) {
					e.printStackTrace();
					throw new IOException(e);
				}
			}
		} finally {
			t.setContextClassLoader(ccl);
		}
	}

}
