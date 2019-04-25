package net.openchrom.knime.nmr.io.bruker;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class FidReaderNodeFactory extends NodeFactory<FidReaderNodeModel> {

	@Override
	public FidReaderNodeModel createNodeModel() {
		return new FidReaderNodeModel();
	}

	@Override
	protected int getNrNodeViews() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public NodeView<FidReaderNodeModel> createNodeView(final int viewIndex, final FidReaderNodeModel nodeModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean hasDialog() {
		return true;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return new FidReaderNodeDialogPane();
	}

}
