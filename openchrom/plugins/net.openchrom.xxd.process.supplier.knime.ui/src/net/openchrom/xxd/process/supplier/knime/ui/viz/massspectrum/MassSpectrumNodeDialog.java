package net.openchrom.xxd.process.supplier.knime.ui.viz.massspectrum;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;

/**
 * <code>NodeDialog</code> for the "MassSpectrum" Node.
 * This view can be used to visualize a mass spectrum.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author OpenChrom
 */
public class MassSpectrumNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring MassSpectrum node dialog.
	 * This is just a suggestion to demonstrate possible default dialog
	 * components.
	 */
	protected MassSpectrumNodeDialog() {
		super();
		addDialogComponent(new DialogComponentNumber(new SettingsModelIntegerBounded(MassSpectrumNodeModel.CFGKEY_COUNT, MassSpectrumNodeModel.DEFAULT_COUNT, Integer.MIN_VALUE, Integer.MAX_VALUE), "Counter:", /* step */ 1, /* componentwidth */ 5));
	}
}
