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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.eclipse.chemclipse.model.core.IMeasurementInfo;
import org.eclipse.chemclipse.support.util.IStringSerialization;

public class DataReportSerialization<Data extends IMeasurementInfo> implements IStringSerialization<IDataReport<Data>> {

	public DataReportSerialization() {

	}

	@Override
	public String serialize(List<IDataReport<Data>> data) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(data);
			oos.close();
			return Base64.getEncoder().encodeToString(baos.toByteArray());
		} catch(IOException e) {
		}
		return "";
	}

	@Override
	public List<IDataReport<Data>> deserialize(String data) {

		if(data != null && !data.isEmpty()) {
			byte[] byteData = Base64.getDecoder().decode(data);
			ObjectInputStream ois;
			try {
				ois = new ObjectInputStream(new ByteArrayInputStream(byteData));
				Object o = ois.readObject();
				ois.close();
				return (List<IDataReport<Data>>)o;
			} catch(IOException | ClassNotFoundException e) {
			}
		}
		return new ArrayList<>();
	}
}
