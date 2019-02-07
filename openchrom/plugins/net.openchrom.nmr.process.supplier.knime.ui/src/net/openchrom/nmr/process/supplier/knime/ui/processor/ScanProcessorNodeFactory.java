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
 *******************************************************************************/
package net.openchrom.nmr.process.supplier.knime.ui.processor;

import java.util.Map.Entry;

import org.eclipse.chemclipse.nmr.processor.core.IScanProcessorSupplier;
import org.eclipse.chemclipse.nmr.processor.core.ScanProcessorNMR;
import org.eclipse.chemclipse.nmr.processor.exceptions.NoProcessorAvailableException;
import org.eclipse.chemclipse.nmr.processor.settings.IProcessorSettings;
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

/**
 * Factory for chromatogram filter nodes. Dialog is generated from the filter settings class (jackson annotated) belonging to a respective filter id (see {@link JacksonPropertyDialogFactory}).
 *
 * @author Martin Horn, University of Konstanz
 *
 */
public class ScanProcessorNodeFactory extends DialogGenerationDynamicNodeFactory<ScanProcessorNodeModel, IProcessorSettings> {

	private String id;

	@Override
	protected NodeDescription createNodeDescription() {

		KnimeNodeDocument doc = KnimeNodeDocument.Factory.newInstance();
		try {
			IScanProcessorSupplier filterSupplier = ScanProcessorNMR.getScanProcessorSupport().getSupplier(id);
			KnimeNode node = doc.addNewKnimeNode();
			node.setIcon("./filter.png");
			node.setType(KnimeNode.Type.MANIPULATOR);
			node.setName(filterSupplier.getProcessorName());
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
			inPort1.setName("NMR Data");
			inPort1.newCursor().setTextValue("NMR Data");
			OutPort outPort1 = ports.addNewOutPort();
			outPort1.setIndex(0);
			outPort1.setName("NMR Data");
			outPort1.newCursor().setTextValue("NMR Data");
		} catch(NoProcessorAvailableException e) {
			// TODO
			throw new RuntimeException(e);
		}
		return new NodeDescription27Proxy(doc);
	}

	@Override
	public ScanProcessorNodeModel createNodeModel(SettingsObjectWrapper<IProcessorSettings> settingsObjectWrapper) {

		return new ScanProcessorNodeModel(id, settingsObjectWrapper);
	}

	@Override
	public NodeView<ScanProcessorNodeModel> createNodeView(int viewIndex, ScanProcessorNodeModel nodeModel) {

		return null;
	}

	@Override
	protected SettingsDialogFactory<IProcessorSettings> createSettingsDialogFactory() {

		JacksonPropertyDialogFactory<IProcessorSettings> factory = new JacksonPropertyDialogFactory<>();
		Class<? extends IProcessorSettings> settingsClass;
		try {
			settingsClass = ScanProcessorNMR.getScanProcessorSupport().getSupplier(id).getSettingsClass();
			if(settingsClass == null) {
				throw new IllegalStateException("Processor NMR settings class for filter id '" + id + "' cannot be resolved. Class migt not be provided by the respective extension point.");
			}
		} catch(NoProcessorAvailableException e) {
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

		id = config.getString(ScanProcessorNodeSetFactory.PROCESSOR_ID_KEY);
		super.loadAdditionalFactorySettings(config);
	}

	@Override
	public void saveAdditionalFactorySettings(ConfigWO config) {

		config.addString(ScanProcessorNodeSetFactory.PROCESSOR_ID_KEY, id);
		super.saveAdditionalFactorySettings(config);
	}
}
