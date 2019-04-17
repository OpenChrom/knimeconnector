/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.nmr.process.supplier.knime.ui.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.nmr.processor.core.ScanProcessorNMR;
import org.eclipse.chemclipse.nmr.processor.exceptions.NoProcessorAvailableException;
import org.eclipse.chemclipse.nmr.processor.settings.IProcessorSettings;
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
public class ScanProcessorNodeSetFactory implements NodeSetFactory {

	static final String PROCESSOR_ID_KEY = "processor-key";
	private static final NodeLogger LOGGER = NodeLogger.getLogger(ScanProcessorNodeSetFactory.class);
	private List<String> ids;

	public ScanProcessorNodeSetFactory() {

		ids = new ArrayList<>();
		try {
			ids.addAll(ScanProcessorNMR.getScanProcessorSupport().getAvailableProcessorIds().stream().filter(p -> {
				Class<? extends IProcessorSettings> settingClass;
				try {
					settingClass = ScanProcessorNMR.getScanProcessorSupport().getSupplier(p).getSettingsClass();
					if(settingClass == null) {
						return false;
					} else {
						return true;
					}
				} catch(NoProcessorAvailableException e) {
					return false;
				}
			}).collect(Collectors.toList()));
		} catch(NoProcessorAvailableException e) {
		}
		ids = Collections.unmodifiableList(ids);
	}

	@Override
	public ConfigRO getAdditionalSettings(String id) {

		NodeSettings settings = new NodeSettings("scannmr-processor-factory");
		settings.addString(PROCESSOR_ID_KEY, id);
		return settings;
	}

	@Override
	public String getAfterID(String id) {

		return "";
	}

	@Override
	public String getCategoryPath(String id) {

		return "openchrom/nmr/processor";
	}

	@Override
	public Class<? extends NodeFactory<? extends NodeModel>> getNodeFactory(String id) {

		return ScanProcessorNodeFactory.class;
	}

	@Override
	public Collection<String> getNodeFactoryIds() {

		return ids;
	}
}
