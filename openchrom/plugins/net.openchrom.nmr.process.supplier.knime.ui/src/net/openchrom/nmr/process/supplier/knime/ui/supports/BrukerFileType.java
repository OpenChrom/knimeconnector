/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.nmr.process.supplier.knime.ui.supports;

public enum BrukerFileType {
	FILE_1R("1r"), FILE_FID("fid");

	private String name;

	private BrukerFileType(String name) {

		this.name = name;
	}

	@Override
	public String toString() {

		return name;
	}
}
