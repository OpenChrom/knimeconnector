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

import org.eclipse.chemclipse.nmr.model.core.AcquisitionParameter;

/**
 * Default implementation for {@code KNIMEAcquisitionParameter}.
 * 
 * @author Alexander Kerner
 *
 */
public class KNIMEAcquisitionParameter implements AcquisitionParameter, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2728583862134828948L;

	private final BigDecimal spectralWidth;

	private final int numberOfPoints;

	private final BigDecimal spectrometerFrequency;

	private final BigDecimal carrierFrequency;

	public KNIMEAcquisitionParameter(BigDecimal spectralWidth, int numberOfPoints, BigDecimal spectrometerFrequency,
			BigDecimal carrierFrequency) {
		super();
		this.spectralWidth = spectralWidth;
		this.numberOfPoints = numberOfPoints;
		this.spectrometerFrequency = spectrometerFrequency;
		this.carrierFrequency = carrierFrequency;
	}

	@Override
	public BigDecimal getSpectralWidth() {
		return spectralWidth;
	}

	@Override
	public int getNumberOfPoints() {
		return numberOfPoints;
	}

	@Override
	public BigDecimal getSpectrometerFrequency() {
		return spectrometerFrequency;
	}

	@Override
	public BigDecimal getCarrierFrequency() {
		return carrierFrequency;
	}

}
