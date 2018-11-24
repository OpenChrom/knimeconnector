/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * jan - initial API and implementation
 *******************************************************************************/
package net.openchrom.nmr.process.supplier.knime.ui.listfilesfolder;

import org.eclipse.chemclipse.converter.core.IConverterSupport;
import org.eclipse.chemclipse.nmr.converter.core.ScanConverterNMR;

import net.openchrom.process.supplier.knime.ui.node.listfilesfolder.ListFilesFolderFactory;

public class ListFilesFolderReaderNMRNodeFactory extends ListFilesFolderFactory {

	public ListFilesFolderReaderNMRNodeFactory() {

		super();
	}

	@Override
	protected IConverterSupport getConverterSupport() {

		return ScanConverterNMR.getScanConverterSupport();
	}
}
