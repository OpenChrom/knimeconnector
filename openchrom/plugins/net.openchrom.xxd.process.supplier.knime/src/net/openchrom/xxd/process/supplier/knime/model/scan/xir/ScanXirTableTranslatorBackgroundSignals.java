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
package net.openchrom.xxd.process.supplier.knime.model.scan.xir;

import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.eclipse.chemclipse.xir.model.core.IScanXIR;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import net.openchrom.xxd.process.supplier.knime.model.utils.TableTranslator;

public class ScanXirTableTranslatorBackgroundSignals implements IScanXirTableTranslator {

	private final String columnName = "Background Signal";

	public ScanXirTableTranslatorBackgroundSignals() {
	}

	@Override
	public BufferedDataTable getBufferedDataTable(IScanXIR scanXIR, ExecutionContext exec) throws CanceledExecutionException, NoExtractedIonSignalStoredException {

		return TableTranslator.doubleArrayToTable(scanXIR.getBackgroundSignals(), columnName, exec);
	}
}
