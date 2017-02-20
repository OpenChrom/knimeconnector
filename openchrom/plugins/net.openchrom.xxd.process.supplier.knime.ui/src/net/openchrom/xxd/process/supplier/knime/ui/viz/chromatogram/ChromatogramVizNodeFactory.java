package net.openchrom.xxd.process.supplier.knime.ui.viz.chromatogram;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ChromatogramViz" Node.
 * This view can be used to visualize a chromatogram.
 *
 * @author OpenChrom
 */
public class ChromatogramVizNodeFactory 
        extends NodeFactory<ChromatogramVizNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ChromatogramVizNodeModel createNodeModel() {
        return new ChromatogramVizNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<ChromatogramVizNodeModel> createNodeView(final int viewIndex,
            final ChromatogramVizNodeModel nodeModel) {
        return new ChromatogramVizNodeView(nodeModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new ChromatogramVizNodeDialog();
    }

}

