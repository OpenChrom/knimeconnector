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
package net.openchrom.knime.node.nmr.phasecorrection;

 import org.knime.core.node.NodeDialogPane;
 import org.knime.core.node.NodeFactory;
 import org.knime.core.node.NodeView;

 public class PhaseCorrectionNodeFactory extends NodeFactory<PhaseCorrectionNodeModel> {

	 @Override
	 public PhaseCorrectionNodeModel createNodeModel() {
		 return new PhaseCorrectionNodeModel();
	 }

	 @Override
	 protected int getNrNodeViews() {
		 // TODO Auto-generated method stub
		 return 0;
	 }

	 @Override
	 public NodeView<PhaseCorrectionNodeModel> createNodeView(int viewIndex, PhaseCorrectionNodeModel nodeModel) {
		 // TODO Auto-generated method stub
		 return null;
	 }

	 @Override
	 protected boolean hasDialog() {
		 // TODO Auto-generated method stub
		 return false;
	 }

	 @Override
	 protected NodeDialogPane createNodeDialogPane() {
		 // TODO Auto-generated method stub
		 return null;
	 }

 }