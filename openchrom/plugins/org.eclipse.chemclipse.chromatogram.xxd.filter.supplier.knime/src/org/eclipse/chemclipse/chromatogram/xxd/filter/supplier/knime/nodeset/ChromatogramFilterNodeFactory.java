/*******************************************************************************
 * Copyright (c) 2017 hornm.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * hornm - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.nodeset;

import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.ChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.filter.exceptions.NoChromatogramFilterSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.dialogfactory.SettingsDialogFactory;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.dialogfactory.SettingsDialogManager;
import org.knime.core.node.DynamicNodeFactory;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDescription;
import org.knime.core.node.NodeDescription27Proxy;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeView;
import org.knime.core.node.config.ConfigRO;
import org.knime.core.node.config.ConfigWO;
import org.knime.node2012.KnimeNodeDocument;

public class ChromatogramFilterNodeFactory extends DynamicNodeFactory<ChromatogramFilterNodeModel> {

	private String filterId;

	@Override
	public ChromatogramFilterNodeModel createNodeModel() {

		return new ChromatogramFilterNodeModel(filterId);
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
	protected boolean hasDialog() {

		try {
			Class<? extends IChromatogramFilterSettings> filterSettingsClass = ChromatogramFilter.getChromatogramFilterSupport().getFilterSupplier(filterId).getFilterSettingsClass();
			if(filterSettingsClass == null) {
				return false;
			} else {
				return SettingsDialogManager.getSettingsDialogFactoryFor(filterSettingsClass).isPresent();
			}
		} catch(NoChromatogramFilterSupplierAvailableException e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {

		try {
			Class<? extends IChromatogramFilterSettings> filterSettingsClass = ChromatogramFilter.getChromatogramFilterSupport().getFilterSupplier(filterId).getFilterSettingsClass();
			return SettingsDialogManager.getSettingsDialogFactoryFor(filterSettingsClass).get().createDialog((Class<Object>)filterSettingsClass);
		} catch(NoChromatogramFilterSupplierAvailableException e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	@Override
	protected NodeDescription createNodeDescription() {

		KnimeNodeDocument doc = KnimeNodeDocument.Factory.newInstance();
		try {
			doc.addNewKnimeNode().setName(ChromatogramFilter.getChromatogramFilterSupport().getFilterSupplier(filterId).getFilterName());
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
