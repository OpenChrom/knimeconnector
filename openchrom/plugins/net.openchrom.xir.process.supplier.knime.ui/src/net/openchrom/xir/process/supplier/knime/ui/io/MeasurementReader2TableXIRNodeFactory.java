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
package net.openchrom.xir.process.supplier.knime.ui.io;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "MeasurementWriterNMR" Node.
 * This node writes chromatographic data.
 *
 * @author OpenChrom
 */
public class MeasurementReader2TableXIRNodeFactory extends NodeFactory<MeasurementReader2TableXIRNodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeDialogPane createNodeDialogPane() {

		return new MeasurementReader2TableXIRNodeDialog();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MeasurementReader2TableXIRNodeModel createNodeModel() {

		return new MeasurementReader2TableXIRNodeModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeView<MeasurementReader2TableXIRNodeModel> createNodeView(final int viewIndex, final MeasurementReader2TableXIRNodeModel nodeModel) {

		return new MeasurementReader2TableXIRNodeView(nodeModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNrNodeViews() {

		return 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasDialog() {

		return true;
	}
}