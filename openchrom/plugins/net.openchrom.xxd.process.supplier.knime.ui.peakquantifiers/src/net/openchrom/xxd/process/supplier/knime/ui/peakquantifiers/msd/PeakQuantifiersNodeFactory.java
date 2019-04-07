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
package net.openchrom.xxd.process.supplier.knime.ui.peakquantifiers.msd;

import java.util.Map.Entry;

import org.eclipse.chemclipse.chromatogram.msd.quantitation.core.IPeakQuantifierSupplier;
import org.eclipse.chemclipse.chromatogram.msd.quantitation.core.PeakQuantifier;
import org.eclipse.chemclipse.chromatogram.msd.quantitation.exceptions.NoPeakQuantifierAvailableException;
import org.eclipse.chemclipse.chromatogram.msd.quantitation.settings.IPeakQuantifierSettings;
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
import org.knime.node2012.OptionDocument.Option;
import org.knime.node2012.OutPortDocument.OutPort;
import org.knime.node2012.PortsDocument.Ports;
import org.knime.node2012.TabDocument.Tab;

import net.openchrom.process.supplier.knime.ui.dialogfactory.SettingsDialogFactory;
import net.openchrom.process.supplier.knime.ui.dialogfactory.SettingsObjectWrapper;
import net.openchrom.process.supplier.knime.ui.dialogfactory.property.JacksonPropertyDialogFactory;
import net.openchrom.process.supplier.knime.ui.dialoggeneration.DialogGenerationDynamicNodeFactory;
import net.openchrom.xxd.process.supplier.knime.ui.peakquantifiers.support.PeakQuantifiersSupport;

public class PeakQuantifiersNodeFactory extends DialogGenerationDynamicNodeFactory<PeakQuantifiersNodeModel, IPeakQuantifierSettings> {

	private String id;

	@Override
	protected NodeDescription createNodeDescription() {

		KnimeNodeDocument doc = KnimeNodeDocument.Factory.newInstance();
		try {
			IPeakQuantifierSupplier supplier = PeakQuantifier.getPeakQuantifierSupport().getPeakQuantifierSupplier(id);
			KnimeNode node = doc.addNewKnimeNode();
			node.setIcon("./peakQuantifiers.gif");
			node.setType(KnimeNode.Type.MANIPULATOR);
			node.setName(supplier.getPeakQuantifierName());
			FullDescription description = node.addNewFullDescription();
			description.addNewIntro().newCursor().setTextValue(supplier.getDescription());
			Tab optionTab = description.addNewTab();
			optionTab.setName("Options");
			for(Entry<String, String> optionDesc : getDialogOptionsDescriptions().entrySet()) {
				Option newOption = optionTab.addNewOption();
				newOption.setName(optionDesc.getKey());
				newOption.newCursor().setTextValue(optionDesc.getValue());
			}
			Ports ports = node.addNewPorts();
			InPort inPort1 = ports.addNewInPort();
			inPort1.setIndex(0);
			inPort1.setName("Chromatogram Selection");
			inPort1.newCursor().setTextValue("Use this input if you'd like to develop a method (chromatogram is persisted - slow)");
			OutPort outPort1 = ports.addNewOutPort();
			outPort1.setIndex(0);
			outPort1.setName("Chromatogram Selection");
			outPort1.newCursor().setTextValue("Use this output if you'd like to develop a method (chromatogram is persisted - slow)");
		} catch(NoPeakQuantifierAvailableException e) {
			throw new RuntimeException(e);
		}
		return new NodeDescription27Proxy(doc);
	}

	@Override
	public PeakQuantifiersNodeModel createNodeModel(SettingsObjectWrapper<IPeakQuantifierSettings> settingsObjectWrapper) {

		return new PeakQuantifiersNodeModel(id, settingsObjectWrapper);
	}

	@Override
	public NodeView<PeakQuantifiersNodeModel> createNodeView(int viewIndex, PeakQuantifiersNodeModel nodeModel) {

		return null;
	}

	@Override
	protected SettingsDialogFactory<IPeakQuantifierSettings> createSettingsDialogFactory() {

		JacksonPropertyDialogFactory<IPeakQuantifierSettings> factory = new JacksonPropertyDialogFactory<>();
		Class<? extends IPeakQuantifierSettings> quantifierSettingsClass;
		try {
			quantifierSettingsClass = PeakQuantifiersSupport.getSupplierMSD(id).getSettingsClass();
			if(quantifierSettingsClass == null) {
				throw new IllegalStateException("Quantifier settings class for filter id '" + id + "' cannot be resolved. Class migt not be provided by the respective extension point.");
			}
		} catch(NoPeakQuantifierAvailableException e) {
			// TODO better exception handling
			throw new RuntimeException(e);
		}
		factory.setSettingsObjectClass(quantifierSettingsClass);
		return factory;
	}

	@Override
	protected int getNrNodeViews() {

		return 0;
	}

	@Override
	public void loadAdditionalFactorySettings(ConfigRO config) throws InvalidSettingsException {

		id = config.getString(PeakQuantifiersNodeSetFactory.PEAK_QUANTIFIER_ID_KEY);
		super.loadAdditionalFactorySettings(config);
	}

	@Override
	public void saveAdditionalFactorySettings(ConfigWO config) {

		config.addString(PeakQuantifiersNodeSetFactory.PEAK_QUANTIFIER_ID_KEY, id);
		super.saveAdditionalFactorySettings(config);
	}
}
