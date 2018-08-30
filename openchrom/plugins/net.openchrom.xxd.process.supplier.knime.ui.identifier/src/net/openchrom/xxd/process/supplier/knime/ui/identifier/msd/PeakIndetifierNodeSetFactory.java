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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.chromatogram.msd.identifier.settings.IPeakIdentifierSettingsMSD;
import org.eclipse.chemclipse.model.exceptions.NoIdentifierAvailableException;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSetFactory;
import org.knime.core.node.NodeSettings;
import org.knime.core.node.config.ConfigRO;

import net.openchrom.xxd.process.supplier.knime.ui.identifier.support.IdentifierSupport;

public class PeakIndetifierNodeSetFactory implements NodeSetFactory {

	static final String IDENTIRIER_ID_KEY = "indetifier-key";
	private static final NodeLogger LOGGER = NodeLogger.getLogger(PeakIndetifierNodeSetFactory.class);
	private List<String> ids;

	public PeakIndetifierNodeSetFactory() {
		List<String> ids = new ArrayList<>();
		try {
			ids.addAll(IdentifierSupport.getIDsPeakIdentifierMSD().stream().filter(f -> {
				try {
					Class<? extends IPeakIdentifierSettingsMSD> peakSettingsClass = IdentifierSupport.getSupplierMSD(f).getIdentifierSettingsClass();
					if(peakSettingsClass == null) {
						LOGGER.warn("Peak settings class for peak id '" + f + "' cannot be resolved. Class migt not be provided by the respective extension point.");
						return false;
					} else {
						return true;
					}
				} catch(NoIdentifierAvailableException e) {
					LOGGER.warn("A problem occurred loading identifier with id '" + f + "'.", e);
					return false;
				}
			}).collect(Collectors.toList()));
			this.ids = Collections.unmodifiableList(ids);
		} catch(NoIdentifierAvailableException e) {
			LOGGER.warn(e);
		}
	}

	@Override
	public ConfigRO getAdditionalSettings(String id) {

		NodeSettings settings = new NodeSettings("indetifier-factory");
		settings.addString(IDENTIRIER_ID_KEY, id);
		return settings;
	}

	@Override
	public String getAfterID(String id) {

		return "";
	}

	@Override
	public String getCategoryPath(String id) {

		return "/openchrom/msd/identifier";
	}

	@Override
	public Class<? extends NodeFactory<? extends NodeModel>> getNodeFactory(String id) {

		return PeakIndetifierNodeFactory.class;
	}

	@Override
	public Collection<String> getNodeFactoryIds() {

		return ids;
	}
}
