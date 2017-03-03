package net.openchrom.xxd.process.supplier.knime.ui.sandbox.viz;

import org.knime.core.data.vector.doublevector.DoubleVectorValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;

public class XYSeriesViewNodeDialog extends DefaultNodeSettingsPane {

	public XYSeriesViewNodeDialog() {
		DialogComponentColumnNameSelection xVectorDC = new DialogComponentColumnNameSelection(
				XYSeriesViewNodeModel.createXVectorColumnModel(), "X Vector", 0, DoubleVectorValue.class);
		DialogComponentColumnNameSelection yVectorDC = new DialogComponentColumnNameSelection(
				XYSeriesViewNodeModel.createXVectorColumnModel(), "Y Vector", 0, DoubleVectorValue.class);
		addDialogComponent(xVectorDC);
		addDialogComponent(yVectorDC);
	}

}
