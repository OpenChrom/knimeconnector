package net.openchrom.knime.node.nmr.apodization;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class ApodizationNodeFactory extends NodeFactory<ApodizationNodeModel> {

	@Override
	public ApodizationNodeModel createNodeModel() {
		return new ApodizationNodeModel();
	}

	@Override
	protected int getNrNodeViews() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public NodeView<ApodizationNodeModel> createNodeView(final int viewIndex, final ApodizationNodeModel nodeModel) {
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
