package net.openchrom.knime.node.fid.base.portobject;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.model.core.AbstractMeasurement;
import org.eclipse.chemclipse.nmr.model.core.AcquisitionParameter;
import org.eclipse.chemclipse.nmr.model.core.DataDimension;
import org.eclipse.chemclipse.nmr.model.core.FIDMeasurement;
import org.eclipse.chemclipse.nmr.model.core.FIDSignal;

public class KNIMEFIDMeasurement extends AbstractMeasurement implements FIDMeasurement {

	public static List<KNIMEFIDMeasurement> buld(Collection<? extends FIDMeasurement> templateMeasurements) {
		return templateMeasurements.stream()
				.map(e -> new KNIMEFIDMeasurement(e.getDataDimension(), KNIMEFIDSignal.build(e.getSignals())))
				.collect(Collectors.toList());
	}

	private final DataDimension dimension;

	private final List<KNIMEFIDSignal> signals;

	public KNIMEFIDMeasurement(DataDimension dimension, Collection<? extends FIDSignal> signals) {
		this.dimension = dimension;
		this.signals = KNIMEFIDSignal.build(signals);
	}

	@Override
	public AcquisitionParameter getAcquisitionParameter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends FIDSignal> getSignals() {
		return signals;
	}

	@Override
	public DataDimension getDataDimension() {
		return dimension;
	}

}
