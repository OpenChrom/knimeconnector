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
package net.openchrom.process.supplier.knime.model;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.eclipse.chemclipse.model.core.IMeasurementInfo;

public abstract class AbstractDataExport<Data extends IMeasurementInfo> extends AbstractDataOutput<Data> implements IDataExport<Data> {

	private static final int INTERNAL_VERSION_ID = 1;

	public AbstractDataExport(String id, File directory) {

		super(id, directory);
	}

	public AbstractDataExport() {

		super();
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
}
