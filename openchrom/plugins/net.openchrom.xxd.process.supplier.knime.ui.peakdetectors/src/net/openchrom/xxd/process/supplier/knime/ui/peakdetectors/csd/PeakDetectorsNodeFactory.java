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
package net.openchrom.xxd.process.supplier.knime.ui.peakdetectors.csd;

import java.util.Map.Entry;

import org.eclipse.chemclipse.chromatogram.csd.peak.detector.settings.IPeakDetectorSettingsCSD;
import org.eclipse.chemclipse.chromatogram.peak.detector.core.IPeakDetectorSupplier;
import org.eclipse.chemclipse.chromatogram.peak.detector.exceptions.NoPeakDetectorAvailableException;
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
import net.openchrom.xxd.process.supplier.knime.ui.peakdetectors.support.PeakDetektorsSupport;

public class PeakDetectorsNodeFactory extends DialogGenerationDynamicNodeFactory<PeakDetectorsNodeModel, IPeakDetectorSettingsCSD> {

	private String id;

	@Override
	protected NodeDescription createNodeDescription() {

		KnimeNodeDocument doc = KnimeNodeDocument.Factory.newInstance();
		try {
			IPeakDetectorSupplier filterSupplier = PeakDetektorsSupport.getSupplierCSD(id);
			KnimeNode node = doc.addNewKnimeNode();
			node.setIcon("./peakdetectors.png");
			node.setType(KnimeNode.Type.MANIPULATOR);
			node.setName(filterSupplier.getPeakDetectorName());
			FullDescription description = node.addNewFullDescription();
			description.addNewIntro().newCursor().setTextValue(filterSupplier.getDescription());
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
		} catch(NoPeakDetectorAvailableException e) {
			// TODO
			throw new RuntimeException(e);
		}
		return new NodeDescription27Proxy(doc);
	}

	@Override
	public PeakDetectorsNodeModel createNodeModel(SettingsObjectWrapper<IPeakDetectorSettingsCSD> settingsObjectWrapper) {

		return new PeakDetectorsNodeModel(id, settingsObjectWrapper);
	}

	@Override
	public NodeView<PeakDetectorsNodeModel> createNodeView(int viewIndex, PeakDetectorsNodeModel nodeModel) {

		return null;
	}

	@Override
	protected SettingsDialogFactory<IPeakDetectorSettingsCSD> createSettingsDialogFactory() {

		JacksonPropertyDialogFactory<IPeakDetectorSettingsCSD> factory = new JacksonPropertyDialogFactory<>();
		Class<? extends IPeakDetectorSettingsCSD> settingsClass;
		try {
			settingsClass = PeakDetektorsSupport.getSupplierCSD(id).getPeakDetectorSettingsClass();
			if(settingsClass == null) {
				throw new IllegalStateException("Peak detectors settings class for detector id '" + id + "' cannot be resolved. Class migt not be provided by the respective extension point.");
			}
		} catch(NoPeakDetectorAvailableException e) {
			// TODO better exception handling
			throw new RuntimeException(e);
		}
		factory.setSettingsObjectClass(settingsClass);
		return factory;
	}

	@Override
	protected int getNrNodeViews() {

		return 0;
	}

	@Override
	public void loadAdditionalFactorySettings(ConfigRO config) throws InvalidSettingsException {

		id = config.getString(PeakDetectorsNodeSetFactory.PEAK_DETEKTORS_KEY);
		super.loadAdditionalFactorySettings(config);
	}

	@Override
	public void saveAdditionalFactorySettings(ConfigWO config) {

		config.addString(PeakDetectorsNodeSetFactory.PEAK_DETEKTORS_KEY, id);
		super.saveAdditionalFactorySettings(config);
	}
}
