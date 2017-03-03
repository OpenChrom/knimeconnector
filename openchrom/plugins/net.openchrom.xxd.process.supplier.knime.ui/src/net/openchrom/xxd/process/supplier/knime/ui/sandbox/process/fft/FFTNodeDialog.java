package net.openchrom.xxd.process.supplier.knime.ui.sandbox.process.fft;

import org.knime.core.data.vector.doublevector.DoubleVectorValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;

public class FFTNodeDialog extends DefaultNodeSettingsPane {

	public FFTNodeDialog() {
		DialogComponentColumnNameSelection dc = new DialogComponentColumnNameSelection(
				FFTNodeModel.createVectorColumnModel(), "Vector", 0, DoubleVectorValue.class);
		addDialogComponent(dc);
	}

}
