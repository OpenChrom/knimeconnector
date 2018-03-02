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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.chromatogram.filter.exceptions.NoChromatogramFilterSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.xxd.report.exceptions.NoReportSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.xxd.report.settings.IChromatogramReportSettings;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSetFactory;
import org.knime.core.node.NodeSettings;
import org.knime.core.node.config.ConfigRO;

import net.openchrom.xxd.process.supplier.knime.ui.reports.support.ReportsSupport;

public class ChromatogramReportsNodeSetFactory implements NodeSetFactory {

	static final String REPORT_KEY = "report-key";
	private List<String> retportIds;
	private static final NodeLogger LOGGER = NodeLogger.getLogger(ChromatogramReportsNodeSetFactory.class);

	public ChromatogramReportsNodeSetFactory() {
		try {
			retportIds = new ArrayList<>();
			retportIds.addAll(ReportsSupport.getIDsReportMSD().stream().filter(f -> {
				try {
					Class<? extends IChromatogramReportSettings> reportSettingsClass = ReportsSupport.getSupplier(f).getChromatogramReportSettingsClass();
					if(reportSettingsClass == null) {
						LOGGER.warn("Report settings class for report id '" + f + "' cannot be resolved. Class migt not be provided by the respective extension point.");
						return false;
					} else {
						return true;
					}
				} catch(NoReportSupplierAvailableException e) {
					LOGGER.warn("A problem occurred loading report with id '" + f + "'.", e);
					return false;
				}
			}).collect(Collectors.toList()));
			retportIds = Collections.unmodifiableList(retportIds);
		} catch(NoChromatogramFilterSupplierAvailableException e) {
			LOGGER.warn(e);
		}
	}

	@Override
	public Collection<String> getNodeFactoryIds() {

		return retportIds;
	}

	@Override
	public Class<? extends NodeFactory<? extends NodeModel>> getNodeFactory(String id) {

		return ChromatogramReportsNodeFactory.class;
	}

	@Override
	public String getCategoryPath(String id) {

		return "/openchrom/msd/report";
	}

	@Override
	public String getAfterID(String id) {

		return "";
	}

	@Override
	public ConfigRO getAdditionalSettings(String id) {

		NodeSettings settings = new NodeSettings("chromatogram-report-factory");
		settings.addString(REPORT_KEY, id);
		return settings;
	}
}
