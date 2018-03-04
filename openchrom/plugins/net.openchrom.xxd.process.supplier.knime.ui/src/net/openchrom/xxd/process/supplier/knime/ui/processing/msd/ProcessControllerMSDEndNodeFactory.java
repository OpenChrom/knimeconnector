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
 * Jan Holy
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.processing.msd;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * Node factory for the "Apply Filters"-node.
 *
 * @author Martin Horn, University of Konstanz
 *
 */
public class ProcessControllerMSDEndNodeFactory extends NodeFactory<ProcessControllerMSDEndNodeModel> {

	@Override
	protected NodeDialogPane createNodeDialogPane() {

		return null;
	}

	@Override
	public ProcessControllerMSDEndNodeModel createNodeModel() {

		return new ProcessControllerMSDEndNodeModel();
	}

	@Override
	public NodeView<ProcessControllerMSDEndNodeModel> createNodeView(int viewIndex, ProcessControllerMSDEndNodeModel nodeModel) {

		return null;
	}

	@Override
	protected int getNrNodeViews() {

		return 0;
	}

	@Override
	protected boolean hasDialog() {

		return false;
	}
}
