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
package net.openchrom.xxd.process.supplier.knime.ui.reports.msd;

import java.util.Map.Entry;

import javax.swing.JFileChooser;

import org.eclipse.chemclipse.chromatogram.xxd.report.core.IChromatogramReportSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.report.exceptions.NoReportSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.xxd.report.settings.IChromatogramReportSettings;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDescription;
import org.knime.core.node.NodeDescription27Proxy;
import org.knime.core.node.NodeView;
import org.knime.core.node.config.ConfigRO;
import org.knime.core.node.config.ConfigWO;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.node2012.FullDescriptionDocument.FullDescription;
import org.knime.node2012.InPortDocument.InPort;
import org.knime.node2012.KnimeNodeDocument;
import org.knime.node2012.KnimeNodeDocument.KnimeNode;
import org.knime.node2012.OptionDocument.Option;
import org.knime.node2012.PortsDocument.Ports;
import org.knime.node2012.TabDocument.Tab;

import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.SettingsDialogFactory;
import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.SettingsObjectWrapper;
import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.property.JacksonPropertyDialogFactory;
import net.openchrom.xxd.process.supplier.knime.ui.dialoggeneration.DialogGenerationNodeFactory;
import net.openchrom.xxd.process.supplier.knime.ui.reports.support.ReportsSupport;

public class ChromatogramReportsNodeFactory extends DialogGenerationNodeFactory<ChromatogramReportsNodeModel, IChromatogramReportSettings> {

	public ChromatogramReportsNodeFactory() {
		int i = 1;
	}

	private String id;
	private String fileExtension;

	@Override
	protected NodeDescription createNodeDescription() {

		KnimeNodeDocument doc = KnimeNodeDocument.Factory.newInstance();
		try {
			IChromatogramReportSupplier reportSupplier = ReportsSupport.getSupplier(id);
			fileExtension = reportSupplier.getFileExtension();
			KnimeNode node = doc.addNewKnimeNode();
			node.setIcon("./report.png");
			node.setType(KnimeNode.Type.SINK);
			node.setName(reportSupplier.getReportName());
			FullDescription description = node.addNewFullDescription();
			description.addNewIntro().newCursor().setTextValue(reportSupplier.getDescription());
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
		} catch(NoReportSupplierAvailableException e) {
			throw new RuntimeException(e);
		}
		return new NodeDescription27Proxy(doc);
	}

	@Override
	protected int getNrNodeViews() {

		return 0;
	}

	@Override
	public ChromatogramReportsNodeModel createNodeModel(SettingsObjectWrapper<IChromatogramReportSettings> settingsObjectWrapper) {

		return new ChromatogramReportsNodeModel(id, settingsObjectWrapper);
	}

	@Override
	protected SettingsDialogFactory<IChromatogramReportSettings> createSettingsDialogFactory() {

		JacksonPropertyDialogFactory<IChromatogramReportSettings> factory = new JacksonPropertyDialogFactory<IChromatogramReportSettings>() {

			@Override
			protected void builtDialogPane(DefaultNodeSettingsPane defaultNodeSettingsPane) {

				DialogComponentFileChooser dialogComponentFileChooser = new DialogComponentFileChooser(ChromatogramReportsNodeModel.SETTING_REPORT_FILE_OUTPUT, "", JFileChooser.SAVE_DIALOG, fileExtension);
				defaultNodeSettingsPane.addDialogComponent(dialogComponentFileChooser);
				super.builtDialogPane(defaultNodeSettingsPane);
			}
		};
		Class<? extends IChromatogramReportSettings> reportSettingsClass;
		try {
			reportSettingsClass = ReportsSupport.getSupplier(id).getChromatogramReportSettingsClass();
			if(reportSettingsClass == null) {
				throw new IllegalStateException("Report settings class for report id '" + id + "' cannot be resolved. Class migt not be provided by the respective extension point.");
			}
		} catch(NoReportSupplierAvailableException e) {
			// TODO better exception handling
			throw new RuntimeException(e);
		}
		factory.setSettingsObjectClass(reportSettingsClass);
		return factory;
	}

	@Override
	public NodeView<ChromatogramReportsNodeModel> createNodeView(int viewIndex, ChromatogramReportsNodeModel nodeModel) {

		return null;
	}

	@Override
	public void loadAdditionalFactorySettings(ConfigRO config) throws InvalidSettingsException {

		id = config.getString(ChromatogramReportsNodeSetFactory.REPORT_KEY);
		super.loadAdditionalFactorySettings(config);
	}

	@Override
	public void saveAdditionalFactorySettings(ConfigWO config) {

		config.addString(ChromatogramReportsNodeSetFactory.REPORT_KEY, id);
		super.saveAdditionalFactorySettings(config);
	}
}
