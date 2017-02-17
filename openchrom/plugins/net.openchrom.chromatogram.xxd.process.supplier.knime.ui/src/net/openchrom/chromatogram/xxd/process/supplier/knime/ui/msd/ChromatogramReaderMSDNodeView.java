/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 * 
 * This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.chromatogram.xxd.process.supplier.knime.ui.msd;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "ChromatogramReaderMSD" Node.
 * This node is reads chromatographic raw data.
 *
 * @author OpenChrom
 */
public class ChromatogramReaderMSDNodeView extends NodeView<ChromatogramReaderMSDNodeModel> {

	/**
	 * Creates a new view.
	 * 
	 * @param nodeModel
	 *            The model (class: {@link ChromatogramReaderMSDNodeModel})
	 */
	protected ChromatogramReaderMSDNodeView(final ChromatogramReaderMSDNodeModel nodeModel) {
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
		ChromatogramReaderMSDNodeModel nodeModel = (ChromatogramReaderMSDNodeModel)getNodeModel();
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
