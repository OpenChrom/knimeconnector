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

import org.knime.core.node.NodeDescription;
import org.knime.core.node.NodeDescription27Proxy;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.node2012.FullDescriptionDocument.FullDescription;
import org.knime.node2012.InPortDocument.InPort;
import org.knime.node2012.KnimeNodeDocument;
import org.knime.node2012.KnimeNodeDocument.KnimeNode;
import org.knime.node2012.OutPortDocument.OutPort;
import org.knime.node2012.PortsDocument.Ports;

/**
 * Node factory for the "Apply Filters"-node.
 *
 * @author Martin Horn, University of Konstanz
 *
 */
public class ProcessControllerMSDEndNodeFactory extends NodeFactory<ProcessControllerMSDEndNodeModel> {

	@Override
	protected NodeDescription createNodeDescription() {

		KnimeNodeDocument doc = KnimeNodeDocument.Factory.newInstance();
		KnimeNode node = doc.addNewKnimeNode();
		node.setType(KnimeNode.Type.LOOP_END);
		node.setName("End Processing");
		FullDescription description = node.addNewFullDescription();
		description.addNewIntro().newCursor().setTextValue("This node applies the selected processes on chromataogram.");
		//
		Ports ports = node.addNewPorts();
		InPort inPort1 = ports.addNewInPort();
		inPort1.setIndex(0);
		inPort1.setName("Chromatogram Selection");
		inPort1.newCursor().setTextValue("");
		OutPort outPort1 = ports.addNewOutPort();
		outPort1.setIndex(0);
		outPort1.setName("Chromatogram Selection");
		outPort1.newCursor().setTextValue("The chromatogram selection port object is exported.");
		//
		return new NodeDescription27Proxy(doc);
	}

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
