package net.openchrom.knime.node.nmr.ft;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class FTNodeFactory extends NodeFactory<FTNodeModel> {

	@Override
	public FTNodeModel createNodeModel() {
		return new FTNodeModel();
	}

	@Override
	protected int getNrNodeViews() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public NodeView<FTNodeModel> createNodeView(final int viewIndex, final FTNodeModel nodeModel) {
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
