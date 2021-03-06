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
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.nodeset;

import org.knime.core.node.NodeView;

public class ChromatogramFilterNodeView extends NodeView<ChromatogramFilterNodeModel> {

	/**
	 * Creates a new view.
	 * 
	 * @param nodeModel
	 *            The model (class: {@link ChromatogramFilterNodeModel})
	 */
	protected ChromatogramFilterNodeView(final ChromatogramFilterNodeModel nodeModel) {
		super(nodeModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void modelChanged() {

		ChromatogramFilterNodeModel nodeModel = (ChromatogramFilterNodeModel)getNodeModel();
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
