/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *Alexander Kerner - initial API and implementation
 *******************************************************************************/
package net.openchrom.knime.node.nmr.apodization;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * {@link NodeFactory} for the Apodization node.
 * 
 * @author Alexander Kerner
 *
 */
public class ApodizationNodeFactory extends NodeFactory<ApodizationNodeModel> {

    @Override
    public ApodizationNodeModel createNodeModel() {
	return new ApodizationNodeModel();
    }

    @Override
    protected int getNrNodeViews() {
	return 0;
    }

    @Override
    public NodeView<ApodizationNodeModel> createNodeView(final int viewIndex, final ApodizationNodeModel nodeModel) {
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
