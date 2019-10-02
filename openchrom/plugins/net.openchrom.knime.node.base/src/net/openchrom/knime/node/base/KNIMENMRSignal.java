/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *Alexander Kerner - initial API and implementation
 *******************************************************************************/
package net.openchrom.knime.node.base;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.nmr.model.core.SpectrumSignal;

public class KNIMENMRSignal implements SpectrumSignal, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6600133619077263931L;

	public static List<KNIMENMRSignal> build(Collection<? extends SpectrumSignal> templateSignals) {
		return templateSignals.stream().map(e -> build(e)).collect(Collectors.toList());
	}

	public static KNIMENMRSignal build(SpectrumSignal templateSignal) {
		return new KNIMENMRSignal(templateSignal.getFrequency(), templateSignal.getAbsorptiveIntensity(),
				templateSignal.getImaginaryY());
	}

	private BigDecimal frequency;
	private Number real;
	private Number imaginary;

	public KNIMENMRSignal(BigDecimal frequency, Number real, Number imaginary) {
		this.frequency = frequency;

		this.real = real;
		this.imaginary = imaginary;

	}

	@Override
	public BigDecimal getFrequency() {

		return frequency;
	}

	@Override
	public Number getAbsorptiveIntensity() {

		return real;
	}

	@Override
	public Number getDispersiveIntensity() {

		return imaginary;
	}

}
