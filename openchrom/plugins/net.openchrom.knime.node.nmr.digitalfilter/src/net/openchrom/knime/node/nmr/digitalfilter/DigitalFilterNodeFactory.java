package net.openchrom.knime.node.nmr.digitalfilter;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class DigitalFilterNodeFactory extends NodeFactory<DigitalFilterNodeModel> {

	@Override
	public DigitalFilterNodeModel createNodeModel() {
		return new DigitalFilterNodeModel();
	}

	@Override
	protected int getNrNodeViews() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public NodeView<DigitalFilterNodeModel> createNodeView(final int viewIndex,
			final DigitalFilterNodeModel nodeModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean hasDialog() {
		return false;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return null;
	}

}
