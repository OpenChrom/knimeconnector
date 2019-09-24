package net.openchrom.knime.node.nmr.ft.portobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.chemclipse.model.core.AbstractMeasurement;
import org.eclipse.chemclipse.nmr.model.core.AcquisitionParameter;
import org.eclipse.chemclipse.nmr.model.core.FIDMeasurement;
import org.eclipse.chemclipse.nmr.model.core.SpectrumMeasurement;

import net.openchrom.knime.node.fid.base.portobject.KNIMEFIDMeasurement;

public class KNIMENMRMeasurement extends AbstractMeasurement implements SpectrumMeasurement {

	public static List<KNIMENMRMeasurement> build(Collection<? extends SpectrumMeasurement> measurements) {
		List<KNIMENMRMeasurement> result = new ArrayList<>();
		for (SpectrumMeasurement knimefidMeasurement : measurements) {
			result.add(new KNIMENMRMeasurement(KNIMEFIDMeasurement.build(knimefidMeasurement.getParent()),
					KNIMENMRSignal.build(knimefidMeasurement.getSignals()), System.currentTimeMillis()));
		}
		return result;
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
	public FIDMeasurement getParent() {
		return parent;
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
