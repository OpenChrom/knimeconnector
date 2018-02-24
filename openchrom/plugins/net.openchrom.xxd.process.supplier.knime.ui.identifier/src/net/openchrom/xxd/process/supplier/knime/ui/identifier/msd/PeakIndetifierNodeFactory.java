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
package net.openchrom.xxd.process.supplier.knime.ui.identifier.msd;

import org.eclipse.chemclipse.chromatogram.msd.identifier.core.ISupplier;
import org.eclipse.chemclipse.chromatogram.msd.identifier.exceptions.NoIdentifierAvailableException;
import org.knime.core.node.DynamicNodeFactory;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDescription;
import org.knime.core.node.NodeDescription27Proxy;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeView;
import org.knime.core.node.config.ConfigRO;
import org.knime.core.node.config.ConfigWO;
import org.knime.node2012.FullDescriptionDocument.FullDescription;
import org.knime.node2012.InPortDocument.InPort;
import org.knime.node2012.KnimeNodeDocument;
import org.knime.node2012.KnimeNodeDocument.KnimeNode;
import org.knime.node2012.OutPortDocument.OutPort;
import org.knime.node2012.PortsDocument.Ports;

import net.openchrom.xxd.process.supplier.knime.ui.identifier.support.IdentifierSupport;

public class PeakIndetifierNodeFactory extends DynamicNodeFactory<PeakIndetifierNodeModel> {

	private String indetifierId;

	@Override
	protected NodeDescription createNodeDescription() {

		try {
			KnimeNodeDocument doc = KnimeNodeDocument.Factory.newInstance();
			ISupplier supplier = IdentifierSupport.getSupplierMSD(indetifierId);
			KnimeNode node = doc.addNewKnimeNode();
			node.setType(KnimeNode.Type.MANIPULATOR);
			node.setName(supplier.getIdentifierName());
			node.setIcon("./identifier.png");
			FullDescription description = node.addNewFullDescription();
			description.addNewIntro().newCursor().setTextValue(supplier.getDescription());
			Ports ports = node.addNewPorts();
			InPort inPort1 = ports.addNewInPort();
			inPort1.setIndex(0);
			inPort1.setName("Chromatogram Selection");
			inPort1.newCursor().setTextValue("Use this input if you'd like to develop a method (chromatogram is persisted - slow)");
			OutPort outPort1 = ports.addNewOutPort();
			outPort1.setIndex(0);
			outPort1.setName("Chromatogram Selection");
			outPort1.newCursor().setTextValue("Use this output if you'd like to develop a method (chromatogram is persisted - slow)");
			return new NodeDescription27Proxy(doc);
		} catch(NoIdentifierAvailableException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {

		return null;
	}

	@Override
	public PeakIndetifierNodeModel createNodeModel() {

		return new PeakIndetifierNodeModel(indetifierId);
	}

	@Override
	public NodeView<PeakIndetifierNodeModel> createNodeView(int viewIndex, PeakIndetifierNodeModel nodeModel) {

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

	@Override
	public void loadAdditionalFactorySettings(ConfigRO config) throws InvalidSettingsException {

		indetifierId = config.getString(PeakIndetifierNodeSetFactory.IDENTIRIER_ID_KEY);
		super.loadAdditionalFactorySettings(config);
	}

	@Override
	public void saveAdditionalFactorySettings(ConfigWO config) {

		config.addString(PeakIndetifierNodeSetFactory.IDENTIRIER_ID_KEY, indetifierId);
		super.saveAdditionalFactorySettings(config);
	}
}
