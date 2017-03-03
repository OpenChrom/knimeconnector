package net.openchrom.xxd.process.supplier.knime.ui.sandbox.viz;

import org.knime.core.node.AbstractNodeView;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class XYSeriesViewNodeFactory extends NodeFactory<XYSeriesViewNodeModel> {

	@Override
	public XYSeriesViewNodeModel createNodeModel() {
		return new XYSeriesViewNodeModel();
	}

	@Override
	protected int getNrNodeViews() {
		return 1;
	}
	
	@Override
	public AbstractNodeView<XYSeriesViewNodeModel> createAbstractNodeView(int viewIndex, XYSeriesViewNodeModel nodeModel) {
		return new XYSeriesViewNodeView(nodeModel);
	}

	@Override
	public NodeView<XYSeriesViewNodeModel> createNodeView(int viewIndex, XYSeriesViewNodeModel nodeModel) {
		return null;
	}

	@Override
	protected boolean hasDialog() {
		return true;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return new XYSeriesViewNodeDialog();
	}

}
