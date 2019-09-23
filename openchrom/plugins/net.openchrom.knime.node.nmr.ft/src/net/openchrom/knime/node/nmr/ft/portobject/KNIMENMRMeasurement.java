package net.openchrom.knime.node.nmr.ft.portobject;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.model.core.AbstractMeasurement;
import org.eclipse.chemclipse.nmr.model.core.AcquisitionParameter;
import org.eclipse.chemclipse.nmr.model.core.SpectrumMeasurement;

import net.openchrom.knime.node.fid.base.portobject.KNIMEFIDMeasurement;

public class KNIMENMRMeasurement extends AbstractMeasurement implements SpectrumMeasurement {

	public static List<KNIMENMRMeasurement> build(Collection<? extends SpectrumMeasurement> measurements) {
		return measurements.stream()
				.map(e -> new KNIMENMRMeasurement(null, KNIMENMRSignal.build(e.getSignals()), new Date().getTime()))
				.collect(Collectors.toList());
	}

	private final KNIMEFIDMeasurement parent;
	private final long lastModified;
	private final List<KNIMENMRSignal> signals;

	public KNIMENMRMeasurement(KNIMEFIDMeasurement parent, List<KNIMENMRSignal> signals, long lastModified) {
		this.parent = parent;
		this.signals = signals;
		this.lastModified = lastModified;
	}

	@Override
	public AcquisitionParameter getAcquisitionParameter() {
		return parent.getAcquisitionParameter();
	}

	@Override
	public List<KNIMENMRSignal> getSignals() {
		return signals;
	}

}
