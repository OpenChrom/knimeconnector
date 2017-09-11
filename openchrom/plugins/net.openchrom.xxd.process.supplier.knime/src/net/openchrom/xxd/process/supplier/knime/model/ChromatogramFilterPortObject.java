/*******************************************************************************
 * Copyright (c) 2017 hornm. Jan Holy
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * hornm - initial API and implementation
 * Jan Holy- implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.ChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.filter.exceptions.NoChromatogramFilterSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.ModelContentWO;
import org.knime.core.node.port.AbstractSimplePortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChromatogramFilterPortObject extends AbstractSimplePortObject {

	/**
	 * @noreference This class is not intended to be referenced by clients.
	 * @since 3.0
	 */
	public static final class Serializer extends AbstractSimplePortObjectSerializer<ChromatogramFilterPortObject> {
	}

	/** Convenience accessor for the port type. */
	public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(ChromatogramFilterPortObject.class);
	public static final PortType TYPE_OPTIONAL = PortTypeRegistry.getInstance().getPortType(ChromatogramFilterPortObject.class, true);
	private List<String> filterIds = new ArrayList<>();
	private List<IChromatogramFilterSettings> filterSettings = new ArrayList<>();
	private final ObjectMapper mapper = new ObjectMapper();

	/**
	 * Adds a new filter to the list of filters.
	 *
	 * @param filterId
	 *            the filter id of the filter to be added
	 * @param settings
	 *            settings class must be 'compatible' with the given filter (represented by its id)
	 * @throws IllegalArgumentException
	 *             if the settings class is not the one expected by the passed filter
	 * @throws NoChromatogramFilterSupplierAvailableException
	 */
	public void addChromatogramFilter(String filterId, IChromatogramFilterSettings settings) throws NoChromatogramFilterSupplierAvailableException {

		filterIds.add(filterId);
		Class<? extends IChromatogramFilterSettings> filterSettingsClass = ChromatogramFilter.getChromatogramFilterSupport().getFilterSupplier(filterId).getFilterSettingsClass();
		if(!filterSettingsClass.isAssignableFrom(settings.getClass())) {
			throw new IllegalArgumentException("The given filter '" + filterId + "' requires '" + filterSettingsClass.getName() + "as settings class, but '" + settings.getClass().getName() + "' passed.");
		}
		filterSettings.add(settings);
	}

	/**
	 * List of filter ids to be applied in the given order and the corresponding settings given by {@link #getFilterSettings()}.
	 *
	 * @return list of filter ids
	 */
	public List<String> getFilterIds() {

		return Collections.unmodifiableList(filterIds);
	}

	public List<IChromatogramFilterSettings> getFilterSettings() {

		return Collections.unmodifiableList(filterSettings);
	}

	@Override
	public PortObjectSpec getSpec() {

		return new ChromatogramFilterPortObjectSpec();
	}

	@Override
	public String getSummary() {

		return "Chromatogram Filter";
	}

	@Override
	protected void load(ModelContentRO model, PortObjectSpec spec, ExecutionMonitor exec) throws InvalidSettingsException, CanceledExecutionException {

		filterIds.addAll(Arrays.asList(model.getStringArray("filter_ids")));
		String[] settings = model.getStringArray("filter_settings");
		for(int i = 0; i < settings.length; i++) {
			try {
				Class<? extends IChromatogramFilterSettings> filterSettingsClass = ChromatogramFilter.getChromatogramFilterSupport().getFilterSupplier(filterIds.get(i)).getFilterSettingsClass();
				if(settings[i] != null) {
					filterSettings.add(mapper.readValue(settings[i], filterSettingsClass));
				} else {
					filterSettings.add(filterSettingsClass.newInstance());
				}
			} catch(IOException | NoChromatogramFilterSupplierAvailableException
					| InstantiationException | IllegalAccessException e) {
				// TODO exception handling
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	protected void save(ModelContentWO model, ExecutionMonitor exec) throws CanceledExecutionException {

		model.addStringArray("filter_ids", filterIds.toArray(new String[filterIds.size()]));
		String[] settings = filterSettings.stream().map(s -> {
			s.getClass();
			if(mapper.canSerialize(s.getClass())) {
				try {
					return mapper.writeValueAsString(s);
				} catch(JsonProcessingException e) {
					// TODO exception handling
					throw new RuntimeException(e);
				}
			} else {
				return null;
			}
		}).collect(Collectors.toList()).toArray(new String[filterSettings.size()]);
		model.addStringArray("filter_settings", settings);
	}
}
