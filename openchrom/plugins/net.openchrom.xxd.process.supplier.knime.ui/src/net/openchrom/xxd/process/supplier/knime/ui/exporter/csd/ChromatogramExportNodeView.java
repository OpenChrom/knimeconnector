/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
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
package net.openchrom.xxd.process.supplier.knime.ui.exporter.csd;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "ChromatogramWriterCSD" Node.
 * This node writes chromatographic data.
 */
public class ChromatogramExportNodeView extends NodeView<ChromatogramExportNodeModel> {

	/**
	 * Creates a new view.
	 *
	 * @param nodeModel
	 *            The model (class: {@link ChromatogramExportNodeModel})
	 */
	protected ChromatogramExportNodeView(final ChromatogramExportNodeModel nodeModel) {
		super(nodeModel);
		/*
		 * TODO add chromatogram TIC view
		 */
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void modelChanged() {

		ChromatogramExportNodeModel nodeModel = getNodeModel();
		assert nodeModel != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onClose() {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onOpen() {

	}
}
