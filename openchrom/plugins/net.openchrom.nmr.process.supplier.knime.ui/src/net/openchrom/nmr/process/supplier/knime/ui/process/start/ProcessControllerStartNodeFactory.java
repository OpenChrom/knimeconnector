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
package net.openchrom.nmr.process.supplier.knime.ui.process.start;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class ProcessControllerStartNodeFactory extends NodeFactory<ProcessControllerStartNodeModel> {

	@Override
	protected NodeDialogPane createNodeDialogPane() {

		return null;
	}

	@Override
	public ProcessControllerStartNodeModel createNodeModel() {

		return new ProcessControllerStartNodeModel();
	}

	@Override
	public NodeView<ProcessControllerStartNodeModel> createNodeView(int viewIndex, ProcessControllerStartNodeModel nodeModel) {

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
