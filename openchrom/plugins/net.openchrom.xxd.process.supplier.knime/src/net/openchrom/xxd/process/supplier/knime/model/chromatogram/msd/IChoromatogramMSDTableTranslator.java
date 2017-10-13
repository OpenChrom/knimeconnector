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
package net.openchrom.xxd.process.supplier.knime.model.chromatogram.msd;

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;

import net.openchrom.xxd.process.supplier.knime.model.chromatogram.IChoromatogramTableTranslator;;

public interface IChoromatogramMSDTableTranslator extends IChoromatogramTableTranslator {

	String TRANSLATION_TYPE_TIC = "TYPE_TIC";
	String TRANSLATION_TYPE_XIC = "TYPE_XIC";

	static IChoromatogramMSDTableTranslator create(String translationType) {

		return new ChoromatogramMSDTableTranslator(translationType);
	}

	IChromatogramMSD getChromatogramMSD(BufferedDataTable bufferedDataTable, final ExecutionContext exec) throws Exception;

	String getTranslationType();

	void setTranslationType(String translationType);
}
