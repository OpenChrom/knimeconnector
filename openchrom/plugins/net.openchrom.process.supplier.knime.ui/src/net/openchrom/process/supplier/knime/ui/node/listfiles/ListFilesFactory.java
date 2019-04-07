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
package net.openchrom.process.supplier.knime.ui.node.listfiles;

import org.eclipse.chemclipse.converter.core.IConverterSupport;
import org.eclipse.chemclipse.converter.core.ISupplier;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public abstract class ListFilesFactory extends NodeFactory<ListFilesNodeModel> {

	private String fileType;
	private String[] suffixes;

	public ListFilesFactory(String fileType, IConverterSupport converterSupport) {

		this.fileType = fileType;
		this.suffixes = converterSupport.getSupplier().stream().map(ISupplier::getFileExtension).toArray(String[]::new);
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

		return new ListFilesNodeDialog(fileType, suffixes);
	}

	@Override
	public NodeView<ListFilesNodeModel> createNodeView(int viewIndex, ListFilesNodeModel nodeModel) {

		return null;
	}

	@Override
	public ListFilesNodeModel createNodeModel() {

		return new ListFilesNodeModel();
	}
}
