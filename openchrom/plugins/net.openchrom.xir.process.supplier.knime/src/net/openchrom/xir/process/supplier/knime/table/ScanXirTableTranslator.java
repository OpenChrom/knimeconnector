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
package net.openchrom.xir.process.supplier.knime.table;

import org.eclipse.chemclipse.xir.model.core.IScanXIR;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import net.openchrom.process.supplier.knime.support.TableTranslator;

public class ScanXirTableTranslator implements IScanXirTableTranslator {

	private final String scanColumnX = "Wavelength";
	private final String scanColumnY = "Intensity";

	public ScanXirTableTranslator() {

	}

	@Override
	public BufferedDataTable getBufferedDataTable(IScanXIR scanXIR, ExecutionContext exec) throws CanceledExecutionException {

		return TableTranslator.scanToTable(scanXIR.getProcessedSignals(), scanColumnX, scanColumnY, exec);
	}
}