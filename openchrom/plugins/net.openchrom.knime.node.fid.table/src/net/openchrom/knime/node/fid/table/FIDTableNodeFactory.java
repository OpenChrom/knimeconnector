package net.openchrom.knime.node.fid.table;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class FIDTableNodeFactory extends NodeFactory<FIDTableNodeModel> {

	@Override
	public FIDTableNodeModel createNodeModel() {
		return new FIDTableNodeModel();
	}

	@Override
	protected int getNrNodeViews() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public NodeView<FIDTableNodeModel> createNodeView(int viewIndex, FIDTableNodeModel nodeModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean hasDialog() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		// TODO Auto-generated method stub
		return null;
	}

}
