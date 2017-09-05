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
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.dialogfactory.property;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.dialogfactory.SettingsDialogFactory;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * A {@link PropertyDialogFactory} implementation that extracts the properties from jackson-annotated classes.
 * <code>@JsonProperty</code> and <code>@JsonPropertyDescription</code> annotations are considered.
 * 
 * @author Martin Horn, University of Konstanz
 *
 * @param <SO> see {@link SettingsDialogFactory}
 */
public class JacksonPropertyDialogFactory<SO> extends PropertyDialogFactory<SO> {

	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public void extractProperties(Class<? extends SO> obj, PropertyCollector coll) {

		// TODO is there a more efficient way?
		JavaType javaType = mapper.getSerializationConfig().constructType(obj);
		BeanDescription beanDesc = mapper.getSerializationConfig().introspect(javaType);
		List<BeanPropertyDefinition> props = beanDesc.findProperties();
		for(BeanPropertyDefinition p : props) {
			Class<?> rawType = p.getField().getRawType();
			String desc = p.getMetadata().getDescription();
			String defaultVal = p.getMetadata().getDefaultValue();
			if(rawType == int.class || rawType == Integer.class) {
				coll.addIntProperty(p.getName(), p.getName(), defaultVal == null ? 0 : Integer.valueOf(defaultVal));
			} else if(rawType == float.class || rawType == Float.class) {
				coll.addFloatProperty(p.getName(), p.getName(), Float.valueOf(defaultVal));
			}
			coll.addPropertyDescriptions(p.getName(), desc);
		}
	}

	@Override
	public SO createSettingsObject(Class<? extends SO> obj, PropertyProvider prov) {
		
		ObjectNode objectNode = mapper.createObjectNode();
		JavaType javaType = mapper.getSerializationConfig().constructType(obj);
		BeanDescription beanDesc = mapper.getSerializationConfig().introspect(javaType);
		List<BeanPropertyDefinition> props = beanDesc.findProperties();
		for(BeanPropertyDefinition p : props) {
			Class<?> rawType = p.getField().getRawType();
			if(rawType == int.class || rawType == Integer.class) {
				objectNode.put(p.getName(), prov.getIntProperty(p.getName()));
			} else if(rawType == float.class || rawType == Float.class) {
				objectNode.put(p.getName(), prov.getFloatProperty(p.getName()));
			}
		}
		return mapper.convertValue(objectNode, obj);
	}
}
