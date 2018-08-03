/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.model.chromatogram.msd;

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;;

public interface IChoromatogramMSDTableTranslator {

	BufferedDataTable getBufferedDataTable(IChromatogramSelectionMSD chromatogram, final ExecutionContext exec) throws CanceledExecutionException, NoExtractedIonSignalStoredException;

	IChromatogramMSD getChromatogram(BufferedDataTable bufferedDataTable, final ExecutionContext exec) throws Exception;

	boolean checkTable(BufferedDataTable bufferedDataTable, final ExecutionContext exec);
}
