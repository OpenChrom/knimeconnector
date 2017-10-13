/*******************************************************************************
 * Copyright (c) 2017 Jan Holy.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.model.chromatogram;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

public interface IChoromatogramTableTranslator {

	BufferedDataTable getBufferedDataTable(IChromatogramSelection chromatogramSelection, final ExecutionContext exec) throws CanceledExecutionException, NoExtractedIonSignalStoredException;

	IChromatogram getChromatogram(BufferedDataTable bufferedDataTable, final ExecutionContext exec) throws Exception;
}
