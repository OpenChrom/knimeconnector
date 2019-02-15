/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.nmr.process.supplier.knime.ui.alignment;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * Node factory for the "Apply Filters"-node.
 *
 * @author Martin Horn, University of Konstanz
 *
 */
public class AlignmentNodeFactory extends NodeFactory<AlignmentNodeModel> {

	@Override
	protected NodeDialogPane createNodeDialogPane() {

		return new AligmentNodeDialog();
	}

	@Override
	public AlignmentNodeModel createNodeModel() {

		return new AlignmentNodeModel();
	}

	@Override
	public NodeView<AlignmentNodeModel> createNodeView(int viewIndex, AlignmentNodeModel nodeModel) {

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
