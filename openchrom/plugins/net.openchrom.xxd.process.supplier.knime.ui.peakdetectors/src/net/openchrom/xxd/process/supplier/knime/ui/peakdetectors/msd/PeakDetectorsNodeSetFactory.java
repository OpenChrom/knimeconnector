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
package net.openchrom.xxd.process.supplier.knime.ui.peakdetectors.msd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.chromatogram.msd.peak.detector.settings.IPeakDetectorCSDSettings;
import org.eclipse.chemclipse.chromatogram.peak.detector.exceptions.NoPeakDetectorAvailableException;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSetFactory;
import org.knime.core.node.NodeSettings;
import org.knime.core.node.config.ConfigRO;

import net.openchrom.xxd.process.supplier.knime.ui.peakdetectors.support.PeakDetektorsSupport;

public class PeakDetectorsNodeSetFactory implements NodeSetFactory {

	private static final NodeLogger LOGGER = NodeLogger.getLogger(PeakDetectorsNodeSetFactory.class);
	static final String PEAK_DETEKTORS_KEY = "peak-detektors-key";
	private List<String> ids;

	public PeakDetectorsNodeSetFactory() {
		ids = new ArrayList<>();
		try {
			ids.addAll(PeakDetektorsSupport.getIDsPeakDectorsMSD().stream().filter(f -> {
				try {
					Class<? extends IPeakDetectorCSDSettings> settings = PeakDetektorsSupport.getSupplierMSD(f).getPeakDetectorSettingsClass();
					if(settings == null) {
						LOGGER.warn("Peak detectors settings class for detecter id '" + f + "' cannot be resolved. Class migt not be provided by the respective extension point.");
						return false;
					} else {
						return true;
					}
				} catch(NoPeakDetectorAvailableException e) {
					LOGGER.warn("A problem occurred loading filter with id '" + f + "'.", e);
					return false;
				}
			}).collect(Collectors.toList()));
			ids = Collections.unmodifiableList(ids);
		} catch(NoPeakDetectorAvailableException e) {
			LOGGER.warn(e);
		}
	}

	@Override
	public ConfigRO getAdditionalSettings(String id) {

		NodeSettings settings = new NodeSettings("peak-detectors-factory");
		settings.addString(PEAK_DETEKTORS_KEY, id);
		return settings;
	}

	@Override
	public String getAfterID(String id) {

		return "";
	}

	@Override
	public String getCategoryPath(String id) {

		return "/openchrom/msd/detector";
	}

	@Override
	public Class<? extends NodeFactory<? extends NodeModel>> getNodeFactory(String id) {

		return PeakDetectorsNodeFactory.class;
	}

	@Override
	public Collection<String> getNodeFactoryIds() {

		return ids;
	}
}
