package net.openchrom.xxd.process.supplier.knime.ui.viz.chromatogram;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Chromatogram" Node.
 * This view can be used to visualize a chromatogram.
 *
 * @author OpenChrom
 */
public class ChromatogramNodeFactory extends NodeFactory<ChromatogramNodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChromatogramNodeModel createNodeModel() {

		return new ChromatogramNodeModel();
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
	public NodeView<ChromatogramNodeModel> createNodeView(final int viewIndex, final ChromatogramNodeModel nodeModel) {

		return new ChromatogramNodeView(nodeModel);
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

		return new ChromatogramNodeDialog();
	}
}
