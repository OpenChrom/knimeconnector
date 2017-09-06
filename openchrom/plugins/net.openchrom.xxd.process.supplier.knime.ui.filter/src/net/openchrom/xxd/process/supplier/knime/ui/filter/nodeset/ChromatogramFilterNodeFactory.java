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
package net.openchrom.xxd.process.supplier.knime.ui.filter.nodeset;

import java.util.Map.Entry;

import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.ChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.IChromatogramFilterSupplier;
import org.eclipse.chemclipse.chromatogram.filter.exceptions.NoChromatogramFilterSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
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

import net.openchrom.xxd.process.supplier.knime.ui.filter.dialogfactory.SettingsDialogFactory;
import net.openchrom.xxd.process.supplier.knime.ui.filter.dialogfactory.SettingsObjectWrapper;
import net.openchrom.xxd.process.supplier.knime.ui.filter.dialogfactory.property.JacksonPropertyDialogFactory;
import net.openchrom.xxd.process.supplier.knime.ui.filter.dialoggeneration.DialogGenerationNodeFactory;

/**
 * Factory for chromatogram filter nodes. Dialog is generated from the filter settings class (jackson annotated) belonging to a respective filter id (see {@link JacksonPropertyDialogFactory}).
 * 
 * @author Martin Horn, University of Konstanz
 *
 */
public class ChromatogramFilterNodeFactory extends DialogGenerationNodeFactory<ChromatogramFilterNodeModel, IChromatogramFilterSettings> {

	private String filterId;

	@Override
	protected SettingsDialogFactory<IChromatogramFilterSettings> createSettingsDialogFactory() {

		JacksonPropertyDialogFactory<IChromatogramFilterSettings> factory = new JacksonPropertyDialogFactory<>();
		Class<? extends IChromatogramFilterSettings> filterSettingsClass;
		try {
			filterSettingsClass = org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.ChromatogramFilter.getChromatogramFilterSupport().getFilterSupplier(filterId).getFilterSettingsClass();
			if(filterSettingsClass == null) {
				throw new IllegalStateException("Filter settings class for filter id '" + filterId + "' cannot be resolved. Class migt not be provided by the respective extension point.");
			}
		} catch(NoChromatogramFilterSupplierAvailableException e) {
			// TODO better exception handling
			throw new RuntimeException(e);
		}
		factory.setSettingsObjectClass(filterSettingsClass);
		return factory;
	}

	@Override
	public ChromatogramFilterNodeModel createNodeModel(SettingsObjectWrapper<IChromatogramFilterSettings> settingsObjectWrapper) {

		return new ChromatogramFilterNodeModel(filterId, settingsObjectWrapper);
	}

	@Override
	protected int getNrNodeViews() {

		return 0;
	}

	@Override
	public NodeView<ChromatogramFilterNodeModel> createNodeView(int viewIndex, ChromatogramFilterNodeModel nodeModel) {

		return null;
	}

	@Override
	protected NodeDescription createNodeDescription() {

		KnimeNodeDocument doc = KnimeNodeDocument.Factory.newInstance();
		try {
			IChromatogramFilterSupplier filterSupplier = ChromatogramFilter.getChromatogramFilterSupport().getFilterSupplier(filterId);
			KnimeNode node = doc.addNewKnimeNode();
			node.setIcon("icons/filter.png");
			node.setType(KnimeNode.Type.MANIPULATOR);
			node.setName(filterSupplier.getFilterName());
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
			inPort1.newCursor().setTextValue("TODO description");
			InPort inPort2 = ports.addNewInPort();
			inPort2.setIndex(1);
			inPort2.setName("Chromatogram Filter");
			inPort2.newCursor().setTextValue("TODO description");
			OutPort outPort1 = ports.addNewOutPort();
			outPort1.setIndex(0);
			outPort1.setName("Chromatogram Selection");
			outPort1.newCursor().setTextValue("TODO description");
			OutPort outPort2 = ports.addNewOutPort();
			outPort2.setIndex(1);
			outPort2.setName("Chromatogram Filter");
			outPort2.newCursor().setTextValue("TODO description");
		} catch(NoChromatogramFilterSupplierAvailableException e) {
			// TODO
			throw new RuntimeException(e);
		}
		return new NodeDescription27Proxy(doc);
	}

	@Override
	public void loadAdditionalFactorySettings(ConfigRO config) throws InvalidSettingsException {

		filterId = config.getString(ChromatogramFilterNodeSetFactory.FILTER_ID_KEY);
		super.loadAdditionalFactorySettings(config);
	}

	@Override
	public void saveAdditionalFactorySettings(ConfigWO config) {

		config.addString(ChromatogramFilterNodeSetFactory.FILTER_ID_KEY, filterId);
		super.saveAdditionalFactorySettings(config);
	}
}
