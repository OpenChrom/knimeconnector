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
package net.openchrom.xxd.process.supplier.knime.ui.peakintegrators.msd;

import java.util.Map.Entry;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.peaks.IPeakIntegratorSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.IPeakIntegrationSettings;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.exceptions.NoIntegratorAvailableException;
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

import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.SettingsDialogFactory;
import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.SettingsObjectWrapper;
import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.property.JacksonPropertyDialogFactory;
import net.openchrom.xxd.process.supplier.knime.ui.dialoggeneration.DialogGenerationNodeFactory;
import net.openchrom.xxd.process.supplier.knime.ui.peakintegrators.support.PeakIntegratorsSupport;

public class PeakIntegratorsNodeFactory extends DialogGenerationNodeFactory<PeakIntegratorsNodeModel, IPeakIntegrationSettings> {

	private String id;

	@Override
	protected NodeDescription createNodeDescription() {

		KnimeNodeDocument doc = KnimeNodeDocument.Factory.newInstance();
		try {
			IPeakIntegratorSupplier supplier = PeakIntegratorsSupport.getSupplier(id);
			KnimeNode node = doc.addNewKnimeNode();
			node.setIcon("./peakIntegrators.png");
			node.setType(KnimeNode.Type.MANIPULATOR);
			node.setName(supplier.getIntegratorName());
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
		} catch(NoIntegratorAvailableException e) {
			throw new RuntimeException(e);
		}
		return new NodeDescription27Proxy(doc);
	}

	@Override
	public PeakIntegratorsNodeModel createNodeModel(SettingsObjectWrapper<IPeakIntegrationSettings> settingsObjectWrapper) {

		return new PeakIntegratorsNodeModel(id, settingsObjectWrapper);
	}

	@Override
	public NodeView<PeakIntegratorsNodeModel> createNodeView(int viewIndex, PeakIntegratorsNodeModel nodeModel) {

		return null;
	}

	@Override
	protected SettingsDialogFactory<IPeakIntegrationSettings> createSettingsDialogFactory() {

		JacksonPropertyDialogFactory<IPeakIntegrationSettings> factory = new JacksonPropertyDialogFactory<>();
		Class<? extends IPeakIntegrationSettings> setingClass;
		try {
			setingClass = PeakIntegratorsSupport.getSupplier(id).getPeakIntegrationSettingsClass();
			if(setingClass == null) {
				throw new IllegalStateException("Peak integrator settings class for filter id '" + id + "' cannot be resolved. Class migt not be provided by the respective extension point.");
			}
		} catch(NoIntegratorAvailableException e) {
			throw new RuntimeException(e);
		}
		factory.setSettingsObjectClass(setingClass);
		return factory;
	}

	@Override
	protected int getNrNodeViews() {

		return 0;
	}

	@Override
	public void loadAdditionalFactorySettings(ConfigRO config) throws InvalidSettingsException {

		id = config.getString(PeakIntegratorsNodeSetFactory.PEAK_INTEGRATOR_ID_KEY);
		super.loadAdditionalFactorySettings(config);
	}

	@Override
	public void saveAdditionalFactorySettings(ConfigWO config) {

		config.addString(PeakIntegratorsNodeSetFactory.PEAK_INTEGRATOR_ID_KEY, id);
		super.saveAdditionalFactorySettings(config);
	}
}
