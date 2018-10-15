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
package net.openchrom.xxd.process.supplier.knime.portobject;

import java.util.List;

import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.core.runtime.IProgressMonitor;

import net.openchrom.xxd.process.supplier.knime.model.IChromatogramSelectionProcessing;

public class ChromatogramProcessingPortObject {

	private static Logger log = Logger.getLogger(ChromatogramProcessingPortObject.class);

	public static void updateChromatogramSelection(ChromatogramSelectionMSDPortObject chromatogramSelectionMSDPortObject, IProgressMonitor monitor, boolean clear) throws Exception {

		List<IChromatogramSelectionProcessing<? super IChromatogramSelectionMSD>> processing = chromatogramSelectionMSDPortObject.processing;
		IChromatogramSelectionMSD chromatogramSelectionMSD = chromatogramSelectionMSDPortObject.extractChromatogramSelectionMSD();
		for(int i = 0; i < processing.size(); i++) {
			IChromatogramSelectionProcessing<? super IChromatogramSelectionMSD> p = processing.get(i);
			try {
				p.process(chromatogramSelectionMSD, monitor);
			} catch(Exception e) {
				log.fatal("Process with id " + p.getId() + " faild", e);
			}
		}
		if(clear) {
			processing.clear();
		}
		chromatogramSelectionMSDPortObject.chromatogramSelectionUpdate();
	}

	public static void updateChromatogramSelection(ChromatogramSelectionCSDPortObject chromatogramSelectionCSDPortObject, IProgressMonitor monitor, boolean clear) throws Exception {

		List<IChromatogramSelectionProcessing<? super IChromatogramSelectionCSD>> processing = chromatogramSelectionCSDPortObject.processing;
		IChromatogramSelectionCSD chromatogramSelectionCSD = chromatogramSelectionCSDPortObject.extractChromatogramSelectionCSD();
		for(int i = 0; i < processing.size(); i++) {
			IChromatogramSelectionProcessing<? super IChromatogramSelectionCSD> p = processing.get(i);
			try {
				p.process(chromatogramSelectionCSD, monitor);
			} catch(Exception e) {
				log.fatal("Process with id " + p.getId() + " faild", e);
			}
		}
		if(clear) {
			processing.clear();
		}
		chromatogramSelectionCSDPortObject.chromatogramSelectionUpdate();
	}
}
