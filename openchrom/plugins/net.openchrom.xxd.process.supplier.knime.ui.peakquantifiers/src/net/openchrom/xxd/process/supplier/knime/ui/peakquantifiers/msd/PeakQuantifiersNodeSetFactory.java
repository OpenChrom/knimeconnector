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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.chromatogram.msd.quantitation.exceptions.NoPeakQuantifierAvailableException;
import org.eclipse.chemclipse.chromatogram.msd.quantitation.settings.IPeakQuantifierSettings;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSetFactory;
import org.knime.core.node.NodeSettings;
import org.knime.core.node.config.ConfigRO;

import net.openchrom.xxd.process.supplier.knime.ui.peakquantifiers.support.PeakQuantifiersSupport;

public class PeakQuantifiersNodeSetFactory implements NodeSetFactory {

	private static final NodeLogger LOGGER = NodeLogger.getLogger(PeakQuantifiersNodeSetFactory.class);
	static final String PEAK_QUANTIFIER_ID_KEY = "peak-quantifier-key";
	private List<String> ids;

	public PeakQuantifiersNodeSetFactory() {

		try {
			ids = new ArrayList<>();
			ids.addAll(PeakQuantifiersSupport.getIdsPeakQuantifiersMSD().stream().filter(f -> {
				try {
					Class<? extends IPeakQuantifierSettings> settingClass = PeakQuantifiersSupport.getSupplierMSD(f).getSettingsClass();
					if(settingClass == null) {
						LOGGER.warn("Peak quantifier settings class for quantifier id '" + f + "' cannot be resolved. Class migt not be provided by the respective extension point.");
						return false;
					} else {
						return true;
					}
				} catch(NoPeakQuantifierAvailableException e) {
					LOGGER.warn("A problem occurred loading peak quantifier with id '" + f + "'.", e);
					return false;
				}
			}).collect(Collectors.toList()));
			ids = Collections.unmodifiableList(ids);
		} catch(NoPeakQuantifierAvailableException e) {
			LOGGER.warn(e);
		}
	}

	@Override
	public ConfigRO getAdditionalSettings(String id) {

		NodeSettings settings = new NodeSettings("peak-integrator-factory");
		settings.addString(PEAK_QUANTIFIER_ID_KEY, id);
		return settings;
	}

	@Override
	public String getAfterID(String id) {

		return "";
	}

	@Override
	public String getCategoryPath(String id) {

		return "/openchrom/msd/peakquantifier";
	}

	@Override
	public Class<? extends NodeFactory<? extends NodeModel>> getNodeFactory(String id) {

		return PeakQuantifiersNodeFactory.class;
	}

	@Override
	public Collection<String> getNodeFactoryIds() {

		return ids;
	}
}
