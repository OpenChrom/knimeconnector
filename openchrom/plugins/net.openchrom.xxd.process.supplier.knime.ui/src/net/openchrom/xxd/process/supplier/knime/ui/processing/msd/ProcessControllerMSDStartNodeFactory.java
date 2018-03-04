/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.processing.msd;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class ProcessControllerMSDStartNodeFactory extends NodeFactory<ProcessControllerMSDStartNodeModel> {

	@Override
	protected NodeDialogPane createNodeDialogPane() {

		return new ProcessControllerMSDStartNodeDialog();
	}

	@Override
	public ProcessControllerMSDStartNodeModel createNodeModel() {

		return new ProcessControllerMSDStartNodeModel();
	}

	@Override
	public NodeView<ProcessControllerMSDStartNodeModel> createNodeView(int viewIndex, ProcessControllerMSDStartNodeModel nodeModel) {

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
