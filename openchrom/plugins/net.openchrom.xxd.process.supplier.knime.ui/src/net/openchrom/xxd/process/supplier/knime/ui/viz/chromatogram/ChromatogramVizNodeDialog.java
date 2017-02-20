package net.openchrom.xxd.process.supplier.knime.ui.viz.chromatogram;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;

/**
 * <code>NodeDialog</code> for the "ChromatogramViz" Node.
 * This view can be used to visualize a chromatogram.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author OpenChrom
 */
public class ChromatogramVizNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring ChromatogramViz node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected ChromatogramVizNodeDialog() {
        super();
        
        addDialogComponent(new DialogComponentNumber(
                new SettingsModelIntegerBounded(
                    ChromatogramVizNodeModel.CFGKEY_COUNT,
                    ChromatogramVizNodeModel.DEFAULT_COUNT,
                    Integer.MIN_VALUE, Integer.MAX_VALUE),
                    "Counter:", /*step*/ 1, /*componentwidth*/ 5));
                    
    }
}

