package net.openchrom.xxd.process.supplier.knime.ui.sandbox.io.fid;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;

public class FIDReaderNodeDialog extends DefaultNodeSettingsPane {

	public FIDReaderNodeDialog() {
		DialogComponentColumnNameSelection dc = new DialogComponentColumnNameSelection(
				FIDReaderNodeModel.createURLColumnModel(), "URL column", 0, StringValue.class);
		addDialogComponent(dc);
	}

}
