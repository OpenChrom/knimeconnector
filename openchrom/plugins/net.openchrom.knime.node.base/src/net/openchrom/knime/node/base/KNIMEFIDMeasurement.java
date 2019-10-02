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
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.model.core.AbstractMeasurement;
import org.eclipse.chemclipse.nmr.model.core.AcquisitionParameter;
import org.eclipse.chemclipse.nmr.model.core.DataDimension;
import org.eclipse.chemclipse.nmr.model.core.FIDMeasurement;
import org.eclipse.chemclipse.nmr.model.core.FIDSignal;

public class KNIMEFIDMeasurement extends AbstractMeasurement
		implements FIDMeasurement, AcquisitionParameter, KNIMEMeasurement, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8772697804527656178L;

	public static List<KNIMEFIDMeasurement> build(Collection<? extends FIDMeasurement> templateMeasurements) {
		return Objects.requireNonNull(templateMeasurements).stream().map(e -> build(e)).collect(Collectors.toList());
	}

	public static KNIMEFIDMeasurement build(FIDMeasurement templateMeasurement) {
		return new KNIMEFIDMeasurement(templateMeasurement.getDataDimension(),
				KNIMEFIDSignal.build(templateMeasurement.getSignals()), templateMeasurement.getHeaderDataMap());
	}

	private final DataDimension dimension;

	private final List<KNIMEFIDSignal> signals;

	public KNIMEFIDMeasurement(DataDimension dimension, Collection<? extends FIDSignal> signals,
			Map<String, String> headerData) {
		super(headerData);
		this.dimension = dimension;
		this.signals = KNIMEFIDSignal.build(signals);
	}

	@Override
	public AcquisitionParameter getAcquisitionParameter() {
		return this;
	}

	public String getRequiredHeaderData(String key, String name) {

		String data = getHeaderData(key);
		if (data == null) {
			throw new IllegalStateException("can't determine " + name + " header key " + key + " is missing!");
		}
		return data;
	}

	@Override
	public List<? extends FIDSignal> getSignals() {
		return signals;
	}

	@Override
	public DataDimension getDataDimension() {
		return dimension;
	}

	@Override
	public BigDecimal getSpectrometerFrequency() {
		return new BigDecimal(getRequiredHeaderData("acqu_BF1", "Spectrometer Frequency"));
	}

	@Override
	public BigDecimal getCarrierFrequency() {
		return new BigDecimal(getRequiredHeaderData("acqu_SFO1", "Carrier Frequency"));
	}

	public BigDecimal getSpectralWidthPPM() {
		return new BigDecimal(getRequiredHeaderData("acqu_SW", "Spectral Width"));
	}

	@Override
	public int getNumberOfPoints() {
		return signals.size();
	}

	@Override
	public BigDecimal getSpectralWidth() {
		String headerData = getHeaderData("acqu_SW_h");
		if (headerData != null) {
			return new BigDecimal(headerData);
		}
		return toHz(getSpectralWidthPPM());
	}

	@Override
	public String toString() {
		return "KNIMEFIDMeasurement [dimension=" + dimension + ", signals=" + signals.size() + ", getHeaderDataMap()="
				+ getHeaderDataMap() + "]";
	}

	@Override
	public void setHeaderDataMap(Map<String, String> map) {
		super.setHeaderDataMap(map);
	}

}
