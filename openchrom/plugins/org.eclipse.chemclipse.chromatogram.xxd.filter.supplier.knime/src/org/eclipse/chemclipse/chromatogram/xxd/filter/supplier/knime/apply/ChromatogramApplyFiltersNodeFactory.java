/*******************************************************************************
 * Copyright (c) 2017 Martin Horn.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Martin Horn - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.apply;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * Node factory for the "Apply Filters"-node.
 * 
 * @author Martin Horn, University of Konstanz
 *
 */
public class ChromatogramApplyFiltersNodeFactory extends NodeFactory<ChromatogramApplyFiltersNodeModel> {

	@Override
	public ChromatogramApplyFiltersNodeModel createNodeModel() {

		return new ChromatogramApplyFiltersNodeModel();
	}

	@Override
	protected int getNrNodeViews() {

		return 0;
	}

	@Override
	public NodeView<ChromatogramApplyFiltersNodeModel> createNodeView(int viewIndex, ChromatogramApplyFiltersNodeModel nodeModel) {

		return null;
	}

	@Override
	protected boolean hasDialog() {

		return false;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {

		return null;
	}
}
