package net.openchrom.xxd.process.supplier.knime.ui.viz.massspectrum;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "MassSpectrum" Node.
 * This view can be used to visualize a mass spectrum.
 *
 * @author OpenChrom
 */
public class MassSpectrumNodeFactory extends NodeFactory<MassSpectrumNodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MassSpectrumNodeModel createNodeModel() {

		return new MassSpectrumNodeModel();
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
	public NodeView<MassSpectrumNodeModel> createNodeView(final int viewIndex, final MassSpectrumNodeModel nodeModel) {

		return new MassSpectrumNodeView(nodeModel);
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

		return new MassSpectrumNodeDialog();
	}
}
