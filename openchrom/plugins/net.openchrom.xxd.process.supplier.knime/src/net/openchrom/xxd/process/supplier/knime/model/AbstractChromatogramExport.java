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

import java.io.File;

import org.eclipse.chemclipse.model.selection.IChromatogramSelection;

public abstract class AbstractChromatogramExport<ChromatogramSelection extends IChromatogramSelection> extends AbstractChromatogramOutput<ChromatogramSelection> implements IChromatogramExport<ChromatogramSelection> {

	public AbstractChromatogramExport(String id, File directory) {
		super(id, directory);
	}

	public AbstractChromatogramExport() {
		super();
	}
}
