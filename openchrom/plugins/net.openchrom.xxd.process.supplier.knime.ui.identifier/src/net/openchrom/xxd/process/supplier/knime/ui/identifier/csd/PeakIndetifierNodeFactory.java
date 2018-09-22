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
package net.openchrom.xxd.process.supplier.knime.ui.identifier.csd;

import org.eclipse.chemclipse.chromatogram.csd.identifier.settings.IPeakIdentifierSettingsCSD;
import org.eclipse.chemclipse.model.exceptions.NoIdentifierAvailableException;
import org.eclipse.chemclipse.model.identifier.core.ISupplier;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDescription;
import org.knime.core.node.NodeDescription27Proxy;
import org.knime.core.node.NodeView;
import org.knime.core.node.config.ConfigRO;
import org.knime.core.node.config.ConfigWO;
import org.knime.node2012.FullDescriptionDocument.FullDescription;
import org.knime.node2012.InPortDocument.InPort;
import org.knime.node2012.KnimeNodeDocument;
import org.knime.node2012.KnimeNodeDocument.KnimeNode;
import org.knime.node2012.OutPortDocument.OutPort;
import org.knime.node2012.PortsDocument.Ports;

import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.SettingsDialogFactory;
import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.SettingsObjectWrapper;
import net.openchrom.xxd.process.supplier.knime.ui.dialoggeneration.DialogGenerationDynamicNodeFactory;
import net.openchrom.xxd.process.supplier.knime.ui.identifier.model.IdentifierPropertyDialogFactory;
import net.openchrom.xxd.process.supplier.knime.ui.identifier.support.IdentifierSupport;

public class PeakIndetifierNodeFactory extends DialogGenerationDynamicNodeFactory<PeakIndetifierNodeModel, IPeakIdentifierSettingsCSD> {

	private String indetifierId;

	@Override
	protected NodeDescription createNodeDescription() {

		try {
			KnimeNodeDocument doc = KnimeNodeDocument.Factory.newInstance();
			ISupplier supplier = IdentifierSupport.getSupplierCSD(indetifierId);
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
	protected int getNrNodeViews() {

		return 0;
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

	@Override
	protected SettingsDialogFactory<IPeakIdentifierSettingsCSD> createSettingsDialogFactory() {

		IdentifierPropertyDialogFactory<IPeakIdentifierSettingsCSD> factory = new IdentifierPropertyDialogFactory<>();
		Class<? extends IPeakIdentifierSettingsCSD> setingClass;
		try {
			setingClass = IdentifierSupport.getSupplierCSD(indetifierId).getIdentifierSettingsClass();
			if(setingClass == null) {
				throw new IllegalStateException("Peak integrator settings class for filter id '" + indetifierId + "' cannot be resolved. Class migt not be provided by the respective extension point.");
			}
		} catch(NoIdentifierAvailableException e) {
			throw new RuntimeException(e);
		}
		factory.setSettingsObjectClass(setingClass);
		return factory;
	}

	@Override
	public PeakIndetifierNodeModel createNodeModel(SettingsObjectWrapper<IPeakIdentifierSettingsCSD> settingsObjectWrapper) {

		return new PeakIndetifierNodeModel(indetifierId, settingsObjectWrapper);
	}

	@Override
	public NodeView<PeakIndetifierNodeModel> createNodeView(int viewIndex, PeakIndetifierNodeModel nodeModel) {

		return null;
	}
}
