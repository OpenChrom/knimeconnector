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
package net.openchrom.xxd.process.supplier.knime.ui.io.csd;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ChromatogramWriterCSD" Node.
 * This node writes chromatographic data.
 *
 * @author OpenChrom
 */
public class ChromatogramExportNodeFactory extends NodeFactory<ChromatogramExportNodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeDialogPane createNodeDialogPane() {

		return new ChromatogramExportNodeDialog();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChromatogramExportNodeModel createNodeModel() {

		return new ChromatogramExportNodeModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeView<ChromatogramExportNodeModel> createNodeView(final int viewIndex, final ChromatogramExportNodeModel nodeModel) {

		return new ChromatogramExportNodeView(nodeModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNrNodeViews() {

		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasDialog() {

		return true;
	}
}
