package net.openchrom.knime.node.base;

import java.awt.FlowLayout;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.eclipse.chemclipse.model.core.IMeasurement;
import org.eclipse.chemclipse.nmr.model.core.FIDMeasurement;
import org.eclipse.chemclipse.nmr.model.core.SpectrumMeasurement;
import org.knime.core.node.NodeView;

public class GenericPortObjectView extends JComponent {

    /**
     * 
     */
    private static final long serialVersionUID = -6523892936829554060L;

    /**
     * Updates are synchronized on this object. Declaring the methods as
     * synchronized (i.e. using "this" as mutex) does not work as swing also
     * acquires locks on this graphical object.
     */
    private final Object updateLock = new Object();

    static String getDataSummary(GenericPortObject<?> data) {
	int numberOfMeasurements = data.getMeasurements().size();

	long countFIDSignals = 0;
	long countNMRSignals = 0;

	for (IMeasurement e : data.getMeasurements()) {
	    if (e instanceof FIDMeasurement) {
		countFIDSignals += ((FIDMeasurement) e).getSignals().size();
	    } else if (e instanceof SpectrumMeasurement) {
		countNMRSignals += ((SpectrumMeasurement) e).getSignals().size();
	    }
	}

	return numberOfMeasurements + " measurement" + pluralSuffix(numberOfMeasurements) + " containing a total of "
		+ countFIDSignals + " fid signals and " + countNMRSignals + " nmr signals.";
    }

    private static Set<?> getMeasurementTypes(GenericPortObject<?> data) {
	Set<Object> measurementTypes = new LinkedHashSet<>();
	data.getMeasurements().stream().filter(e -> e instanceof FIDMeasurement)
		.forEach(e -> measurementTypes.add(FIDMeasurement.class.getSimpleName()));
	data.getMeasurements().stream().filter(e -> e instanceof SpectrumMeasurement)
		.forEach(e -> measurementTypes.add(SpectrumMeasurement.class.getSimpleName()));
	return measurementTypes;
    }

    private static String pluralSuffix(int numberOfMeasurements) {
	return (numberOfMeasurements > 1) ? "s" : "";
    }

    public GenericPortObjectView(GenericPortObject<?> genericPortObject) {
	setLayout(new FlowLayout());
	setBackground(NodeView.COLOR_BACKGROUND);
	updateView(genericPortObject);
    }

    private void updateView(GenericPortObject<?> data) {
	synchronized (updateLock) {
	    add(new JLabel("<html>" + getDataSummary(data) + "</html>"));
	    revalidate();
	}
    }

}
