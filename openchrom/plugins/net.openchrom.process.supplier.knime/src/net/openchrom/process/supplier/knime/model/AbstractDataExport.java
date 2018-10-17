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

import org.eclipse.chemclipse.model.core.IMeasurementInfo;

public abstract class AbstractDataExport<Data extends IMeasurementInfo> extends AbstractDataOutput<Data> implements IDataExport<Data> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2581302980172298044L;

	public AbstractDataExport(String id, File directory) {

		super(id, directory);
	}

	public AbstractDataExport() {

		super();
	}
}
