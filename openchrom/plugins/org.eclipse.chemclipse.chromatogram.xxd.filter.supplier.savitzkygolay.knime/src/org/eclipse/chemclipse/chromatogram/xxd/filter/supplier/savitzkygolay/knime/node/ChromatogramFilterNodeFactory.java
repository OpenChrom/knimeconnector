/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.knime.node;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class ChromatogramFilterNodeFactory extends NodeFactory<ChromatogramFilterNodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChromatogramFilterNodeModel createNodeModel() {

		return new ChromatogramFilterNodeModel();
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
	public NodeView<ChromatogramFilterNodeModel> createNodeView(final int viewIndex, final ChromatogramFilterNodeModel nodeModel) {

		return new ChromatogramFilterNodeView(nodeModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasDialog() {

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeDialogPane createNodeDialogPane() {

		return new ChromatogramFilterNodeDialog();
	}
}
