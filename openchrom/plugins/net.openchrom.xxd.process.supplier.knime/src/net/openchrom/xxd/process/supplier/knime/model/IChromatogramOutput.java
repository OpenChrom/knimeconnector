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
package net.openchrom.xxd.process.supplier.knime.model;

import java.io.File;
import java.io.Serializable;
import java.util.function.BiFunction;

import org.eclipse.chemclipse.model.selection.IChromatogramSelection;

public interface IChromatogramOutput<ChromatogramSelection extends IChromatogramSelection> extends Serializable, IChromatogramSelectionProcessing<ChromatogramSelection> {

	BiFunction<IChromatogramOutput, IChromatogramSelection, String> USE_CHROMATOGAM_NAME = (BiFunction<IChromatogramOutput, IChromatogramSelection, String> & Serializable)(chromatogramExport, chromatogram) -> {
		StringBuilder sb = new StringBuilder();
		sb.append(chromatogramExport.getPrefix());
		sb.append(chromatogram.getChromatogram().getName());
		sb.append(chromatogramExport.getPostfix());
		return sb.toString();
	};
	BiFunction<IChromatogramOutput, IChromatogramSelection, String> USE_NAME = (chromatogramExport, chromatogram) -> {
		StringBuilder sb = new StringBuilder();
		sb.append(chromatogramExport.getPrefix());
		sb.append(chromatogramExport.getFileName());
		sb.append(chromatogramExport.getPostfix());
		return sb.toString();
	};

	void setPrefix(String prefix);

	String getPrefix();

	void setPostfix(String postfix);

	String getPostfix();

	void setDirectory(File directory);

	void setDirectory(String directory);

	File getDirectory();

	void setFileName(String fileName);

	String getFileName();

	void setFunctionCreateFileName(BiFunction<IChromatogramOutput, IChromatogramSelection, String> functionCreateFileName);

	BiFunction<IChromatogramOutput, IChromatogramSelection, String> getFunctionCreateFileName();
}
