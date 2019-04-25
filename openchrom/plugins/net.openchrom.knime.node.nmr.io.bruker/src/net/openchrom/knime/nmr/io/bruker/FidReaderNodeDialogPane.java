package net.openchrom.knime.nmr.io.bruker;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class FidReaderNodeDialogPane extends DefaultNodeSettingsPane {

	public FidReaderNodeDialogPane() {

		final SettingsModelString selectionIn = FidReaderNodeModel.createSettingsModelValueIn();
		addDialogComponent(new DialogComponentFileChooser(selectionIn, FidReaderNodeDialogPane.class.getName(),
				JFileChooser.OPEN_DIALOG, true));
	}

}
