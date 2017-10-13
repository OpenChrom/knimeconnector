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
package net.openchrom.xxd.process.supplier.knime.ui.sandbox.io.fid;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class FIDReaderNodeFactory extends NodeFactory<FIDReaderNodeModel> {

	@Override
	protected NodeDialogPane createNodeDialogPane() {

		return new FIDReaderNodeDialog();
	}

	@Override
	public FIDReaderNodeModel createNodeModel() {

		return new FIDReaderNodeModel();
	}

	@Override
	public NodeView<FIDReaderNodeModel> createNodeView(int viewIndex, FIDReaderNodeModel nodeModel) {

		return null;
	}

	@Override
	protected int getNrNodeViews() {

		return 0;
	}

	@Override
	protected boolean hasDialog() {

		return true;
	}
}
