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
package net.openchrom.nmr.process.supplier.knime.table;

import org.eclipse.chemclipse.nmr.model.core.IScanNMR;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import net.openchrom.process.supplier.knime.support.TableTranslator;

public class ScanNmrTableTranslator implements IScanNmrTableTranslator {

	private final String scanColumnX = "Chemical Shift";
	private final String scanColumnY = "Intensity";

	public ScanNmrTableTranslator() {

	}

	@Override
	public BufferedDataTable getBufferedDataTable(IScanNMR scanNMR, ExecutionContext exec) throws CanceledExecutionException {

		return TableTranslator.scanToTable(scanNMR.getProcessedSignals(), scanColumnX, scanColumnY, exec);
	}
}
