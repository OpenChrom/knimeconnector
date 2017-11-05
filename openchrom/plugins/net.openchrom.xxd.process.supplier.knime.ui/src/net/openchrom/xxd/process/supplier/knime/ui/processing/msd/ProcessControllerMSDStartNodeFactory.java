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
package net.openchrom.xxd.process.supplier.knime.ui.processing.msd;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;
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
import org.xml.sax.SAXException;

public class ProcessControllerMSDStartNodeFactory extends NodeFactory<ProcessControllerMSDStartNodeModel> {

	@Override
	protected NodeDescription createNodeDescription() throws SAXException, IOException, XmlException {

		KnimeNodeDocument doc = KnimeNodeDocument.Factory.newInstance();
		KnimeNode node = doc.addNewKnimeNode();
		node.setType(KnimeNode.Type.LOOP_START);
		node.setName("Start Processing");
		FullDescription description = node.addNewFullDescription();
		description.addNewIntro().newCursor().setTextValue("This node set processing mode.");
		//
		Ports ports = node.addNewPorts();
		InPort inPort1 = ports.addNewInPort();
		inPort1.setIndex(0);
		inPort1.setName("Chromatogram Selection");
		inPort1.newCursor().setTextValue("Use this input if you'd like to develop a method (chromatogram is persisted - slow)");
		OutPort outPort1 = ports.addNewOutPort();
		outPort1.setIndex(0);
		outPort1.setName("Chromatogram Selection");
		outPort1.newCursor().setTextValue("The chromatogram selection port object is exported.");
		//
		return new NodeDescription27Proxy(doc);
	}

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
