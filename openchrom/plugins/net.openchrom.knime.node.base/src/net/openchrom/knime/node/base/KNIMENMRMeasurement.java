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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.model.core.AbstractMeasurement;
import org.eclipse.chemclipse.nmr.model.core.AcquisitionParameter;
import org.eclipse.chemclipse.nmr.model.core.SpectrumMeasurement;

/**
 * Default implementation for {@link SpectrumMeasurement}
 * 
 * @author Alexander Kerner
 *
 */
public class KNIMENMRMeasurement extends AbstractMeasurement implements SpectrumMeasurement, KNIMEMeasurement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6737316171841818539L;

	public static List<KNIMENMRMeasurement> build(Collection<? extends SpectrumMeasurement> measurements) {
		List<KNIMENMRMeasurement> result = new ArrayList<>();
		for (SpectrumMeasurement m : measurements) {
			result.add(build(m));
		}
		return result;
	}

	public static KNIMENMRMeasurement build(SpectrumMeasurement measurement) {
		return new KNIMENMRMeasurement(KNIMENMRSignal.build(measurement.getSignals()),
				measurement.getAcquisitionParameter());
	}

	private final List<KNIMENMRSignal> signals;

	private final AcquisitionParameter aqAcquisitionParameter;

	public KNIMENMRMeasurement(List<KNIMENMRSignal> signals, AcquisitionParameter aqAcquisitionParameter) {
		this.signals = signals;
		this.aqAcquisitionParameter = new KNIMEAcquisitionParameter(aqAcquisitionParameter.getSpectralWidth(),
				aqAcquisitionParameter.getNumberOfPoints(), aqAcquisitionParameter.getSpectrometerFrequency(),
				aqAcquisitionParameter.getCarrierFrequency());
	}

	@Override
	public AcquisitionParameter getAcquisitionParameter() {
		return aqAcquisitionParameter;
	}

	@Override
	public List<KNIMENMRSignal> getSignals() {
		return signals;
	}

	@Override
	public void setHeaderDataMap(Map<String, String> map) {
		super.setHeaderDataMap(map);
	}

}
