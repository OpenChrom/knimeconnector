package net.openchrom.knime.node.fid.base.portobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.nmr.model.core.FIDSignal;

public class KNIMEFIDSignal implements FIDSignal, Serializable {

	public static List<KNIMEFIDSignal> build(Collection<? extends FIDSignal> templateSignals) {
		return templateSignals.stream().map(e -> build(e)).collect(Collectors.toList());
	}

	public static KNIMEFIDSignal build(FIDSignal templateSignal) {
		return new KNIMEFIDSignal(templateSignal.getSignalTime(), templateSignal.getRealComponent(),
				templateSignal.getImaginaryComponent());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6467804926152642928L;
	private BigDecimal time;
	private Number real;
	private Number imag;

	public KNIMEFIDSignal(BigDecimal time, Number real, Number imag) {
		this.time = time;
		this.real = real;
		this.imag = imag;
	}

	@Override
	public BigDecimal getSignalTime() {

		return time;
	}

	@Override
	public Number getRealComponent() {

		return real;
	}

	@Override
	public Number getImaginaryComponent() {

		return imag;
	}

}
