/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.model.chromatogram.msd;

import org.eclipse.chemclipse.model.exceptions.AbundanceLimitExceededException;
import org.eclipse.chemclipse.msd.model.core.AbstractScanIon;
import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.eclipse.chemclipse.msd.model.exceptions.IonLimitExceededException;
import org.eclipse.chemclipse.msd.model.exceptions.IonTransitionIsNullException;

public class VendorIon extends AbstractScanIon implements IVendorIon {

	public static final float MAX_ABUNDANCE = Float.MAX_VALUE;
	public static final double MAX_ION = 65535.0d;
	// A max value for abundance
	public static final float MIN_ABUNDANCE = Float.MIN_VALUE;
	// A max value for m/z
	public static final double MIN_ION = 1.0d;
	/**
	 * Renew the serialVersionUID any time you have changed some fields or
	 * methods.
	 */
	private static final long serialVersionUID = 3924319153961956114L;

	public VendorIon(double ion, float abundance) throws AbundanceLimitExceededException, IonLimitExceededException {
		super(ion, abundance);
	}

	public VendorIon(double ion, float abundance, IIonTransition ionTransition) throws AbundanceLimitExceededException, IonLimitExceededException, IonTransitionIsNullException {
		super(ion, abundance, ionTransition);
	}

	@Override
	public float getMaxPossibleAbundanceValue() {

		return MAX_ABUNDANCE;
	}

	@Override
	public double getMaxPossibleIonValue() {

		return MAX_ION;
	}

	@Override
	public float getMinPossibleAbundanceValue() {

		return MIN_ABUNDANCE;
	}

	@Override
	public double getMinPossibleIonValue() {

		return MIN_ION;
	}
}
