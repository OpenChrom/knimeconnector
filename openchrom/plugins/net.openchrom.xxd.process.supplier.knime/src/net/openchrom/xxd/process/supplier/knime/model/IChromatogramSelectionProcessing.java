/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
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

import java.io.Serializable;
import java.util.List;

import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IChromatogramSelectionProcessing<ChromatogramSelection extends IChromatogramSelection> extends Serializable {

	static void updateChromatogramSelection(ChromatogramSelectionMSDPortObject chromatogramSelectionMSDPortObject, IProgressMonitor monitor, boolean clear) throws Exception {

		List<IChromatogramSelectionProcessing<? super IChromatogramSelectionMSD>> processing = chromatogramSelectionMSDPortObject.processing;
		IChromatogramSelectionMSD chromatogramSelectionMSD = chromatogramSelectionMSDPortObject.extractChromatogramSelectionMSD();
		for(int i = 0; i < processing.size(); i++) {
			processing.get(i).process(chromatogramSelectionMSD, monitor);
		}
		if(clear) {
			processing.clear();
		}
		chromatogramSelectionMSDPortObject.chromatogramSelectionUpdate();
	}

	IProcessingInfo process(ChromatogramSelection chromatogramSelection, IProgressMonitor monitor) throws Exception;
}
