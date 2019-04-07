/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.process.supplier.knime.ui.node.listfilesfolder;

import org.eclipse.chemclipse.converter.core.IConverterSupport;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class ListFilesFolderFactory extends NodeFactory<ListFilesFolderNodeModel> {

	private IConverterSupport converterSupport;

	public ListFilesFolderFactory(IConverterSupport converterSupport) {

		this.converterSupport = converterSupport;
	}

	@Override
	protected int getNrNodeViews() {

		return 0;
	}

	@Override
	protected boolean hasDialog() {

		return true;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {

		return new ListFilesFolderNodeDialog(converterSupport.getSupplier());
	}

	@Override
	public NodeView<ListFilesFolderNodeModel> createNodeView(int viewIndex, ListFilesFolderNodeModel nodeModel) {

		return null;
	}

	@Override
	public ListFilesFolderNodeModel createNodeModel() {

		return new ListFilesFolderNodeModel(converterSupport.getSupplier());
	}
}
