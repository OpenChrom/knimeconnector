package net.openchrom.xxd.process.supplier.knime.ui.sandbox.process.fft;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class FFTNodeFactory extends NodeFactory<FFTNodeModel> {

	@Override
	public FFTNodeModel createNodeModel() {
		return new FFTNodeModel();
	}

	@Override
	protected int getNrNodeViews() {
		return 0;
	}

	@Override
	public NodeView<FFTNodeModel> createNodeView(int viewIndex, FFTNodeModel nodeModel) {
		return null;
	}

	@Override
	protected boolean hasDialog() {
		return true;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return new FFTNodeDialog();
	}

}
