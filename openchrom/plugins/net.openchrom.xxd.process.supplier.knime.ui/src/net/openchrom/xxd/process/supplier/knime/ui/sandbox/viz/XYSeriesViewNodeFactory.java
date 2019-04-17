/*******************************************************************************
 * Copyright (c) 2017 Universität Konstanz.
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
 * Dr. Martin Horn - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.sandbox.viz;

import org.knime.core.node.AbstractNodeView;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class XYSeriesViewNodeFactory extends NodeFactory<XYSeriesViewNodeModel> {

	@Override
	public AbstractNodeView<XYSeriesViewNodeModel> createAbstractNodeView(int viewIndex, XYSeriesViewNodeModel nodeModel) {

		return new XYSeriesViewNodeView(nodeModel);
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {

		return new XYSeriesViewNodeDialog();
	}

	@Override
	public XYSeriesViewNodeModel createNodeModel() {

		return new XYSeriesViewNodeModel();
	}

	@Override
	public NodeView<XYSeriesViewNodeModel> createNodeView(int viewIndex, XYSeriesViewNodeModel nodeModel) {

		return null;
	}

	@Override
	protected int getNrNodeViews() {

		return 1;
	}

	@Override
	protected boolean hasDialog() {

		return true;
	}
}
