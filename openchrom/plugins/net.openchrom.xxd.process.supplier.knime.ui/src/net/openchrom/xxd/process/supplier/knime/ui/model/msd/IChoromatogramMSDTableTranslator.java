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
package net.openchrom.xxd.process.supplier.knime.ui.model.msd;

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import net.openchrom.xxd.process.supplier.knime.ui.model.IChoromatogramTableTranslator;

public interface IChoromatogramMSDTableTranslator extends IChoromatogramTableTranslator {
	
	static IChoromatogramMSDTableTranslator create(){
		return new ChoromatogramMSDTableTranslator();
	}

	BufferedDataTable getBufferedDataTableTIC(IChromatogramSelectionMSD chromatogramSelection, final ExecutionContext exec) throws CanceledExecutionException, NoExtractedIonSignalStoredException;

	BufferedDataTable getBufferedDataTableXIC(IChromatogramSelectionMSD chromatogramSelection, final ExecutionContext exec) throws CanceledExecutionException, NoExtractedIonSignalStoredException;

	IChromatogramMSD getChromatogramMSD(BufferedDataTable bufferedDataTable, final ExecutionContext exec) throws Exception;
}
