package net.openchrom.xxd.process.supplier.knime.ui.sandbox.io.fid;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class FIDReaderNodeFactory extends NodeFactory<FIDReaderNodeModel> {

	@Override
	public FIDReaderNodeModel createNodeModel() {
		return new FIDReaderNodeModel();
	}

	@Override
	protected int getNrNodeViews() {
		return 0;
	}

	@Override
	public NodeView<FIDReaderNodeModel> createNodeView(int viewIndex, FIDReaderNodeModel nodeModel) {
		return null;
	}

	@Override
	protected boolean hasDialog() {
		return true;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return new FIDReaderNodeDialog();
	}

}
