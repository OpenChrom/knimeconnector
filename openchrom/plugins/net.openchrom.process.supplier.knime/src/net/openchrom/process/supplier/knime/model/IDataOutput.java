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

public interface IDataOutput<Data extends IMeasurementInfo> extends IDataProcessing<Data> {

	void setPrefix(String prefix);

	String getPrefix();

	void setPostfix(String postfix);

	String getPostfix();

	void setDirectory(File directory);

	void setDirectory(String directory);

	File getDirectory();

	void setFileName(String fileName);

	String getFileName();
}
