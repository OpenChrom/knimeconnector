package net.openchrom.xxd.process.supplier.knime.ui.viz.chromatogram;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "Chromatogram" Node.
 * This view can be used to visualize a chromatogram.
 *
 * @author OpenChrom
 */
public class ChromatogramNodeView extends NodeView<ChromatogramNodeModel> {

	/**
	 * Creates a new view.
	 * 
	 * @param nodeModel
	 *            The model (class: {@link ChromatogramNodeModel})
	 */
	protected ChromatogramNodeView(final ChromatogramNodeModel nodeModel) {
		super(nodeModel);
		// TODO instantiate the components of the view here.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void modelChanged() {

		// TODO retrieve the new model from your nodemodel and
		// update the view.
		ChromatogramNodeModel nodeModel = (ChromatogramNodeModel)getNodeModel();
		assert nodeModel != null;
		// be aware of a possibly not executed nodeModel! The data you retrieve
		// from your nodemodel could be null, emtpy, or invalid in any kind.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onClose() {

		// TODO things to do when closing the view
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onOpen() {

		// TODO things to do when opening the view
	}
}
