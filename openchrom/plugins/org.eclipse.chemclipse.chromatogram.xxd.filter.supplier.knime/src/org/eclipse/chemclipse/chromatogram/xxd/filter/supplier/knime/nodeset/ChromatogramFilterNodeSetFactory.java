/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.nodeset;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.ChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.ChromatogramFilterSupport;
import org.eclipse.chemclipse.chromatogram.filter.exceptions.NoChromatogramFilterSupplierAvailableException;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSetFactory;
import org.knime.core.node.NodeSettings;
import org.knime.core.node.config.ConfigRO;

/**
 * Node set factory that generates all possible chromatogram filter nodes. A node for a specific filter id can only be generated, if it's filter settings class is registered at the respective extension point.
 * 
 * @author Martin Horn, University of Konstanz
 *
 */
public class ChromatogramFilterNodeSetFactory implements NodeSetFactory {

	private static final NodeLogger LOGGER = NodeLogger.getLogger(ChromatogramFilterNodeSetFactory.class);
	static final String FILTER_ID_KEY = "filterid-key";
	private List<String> filterIds;

	public ChromatogramFilterNodeSetFactory() {
		try {
			filterIds = ChromatogramFilter.getChromatogramFilterSupport().getAvailableFilterIds();
			// filter those nodes that provide a filter settings class - otherwise no dialog would be available
			filterIds = Collections.unmodifiableList(filterIds.stream().filter(s -> {
				try {
					Class filterSettingsClass = org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.ChromatogramFilter.getChromatogramFilterSupport().getFilterSupplier(s).getFilterSettingsClass();
					if(filterSettingsClass == null) {
						LOGGER.warn("Filter settings class for filter id '" + s + "' cannot be resolved. Class migt not be provided by the respective extension point.");
						return false;
					} else {
						return true;
					}
				} catch(NoChromatogramFilterSupplierAvailableException e) {
					LOGGER.warn("A problem occurred loading filter with id '" + s + "'.", e);
					return false;
				}
			}).collect(Collectors.toList()));
		} catch(NoChromatogramFilterSupplierAvailableException e) {
			LOGGER.warn(e);
		}
	}

	@Override
	public Collection<String> getNodeFactoryIds() {

		return filterIds;
	}

	@Override
	public Class<? extends NodeFactory<? extends NodeModel>> getNodeFactory(String id) {

		return ChromatogramFilterNodeFactory.class;
	}

	@Override
	public String getCategoryPath(String id) {

		return "/openchrom/filter";
	}

	@Override
	public String getAfterID(String id) {

		return "";
	}

	@Override
	public ConfigRO getAdditionalSettings(String id) {

		NodeSettings settings = new NodeSettings("chromatogram-filter-factory");
		settings.addString(FILTER_ID_KEY, id);
		return settings;
	}
}
