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
package net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.property;

import java.util.List;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.SettingsDialogFactory;

/**
 * A {@link PropertyDialogFactory} implementation that extracts the properties from jackson-annotated classes.
 * <code>@JsonProperty</code> and <code>@JsonPropertyDescription</code> annotations are considered.
 *
 * @author Martin Horn, University of Konstanz
 *
 * @param <SO>
 *            see {@link SettingsDialogFactory}
 */
public class JacksonPropertyDialogFactory<SO> extends PropertyDialogFactory<SO> {

	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public SO createSettingsObject(Class<? extends SO> obj, PropertyProvider prov) {

		ObjectNode objectNode = mapper.createObjectNode();
		JavaType javaType = mapper.getSerializationConfig().constructType(obj);
		BeanDescription beanDesc = mapper.getSerializationConfig().introspect(javaType);
		List<BeanPropertyDefinition> props = beanDesc.findProperties();
		for(BeanPropertyDefinition p : props) {
			Class<?> rawType = p.getField().getRawType();
			//
			if(rawType == int.class || rawType == Integer.class) {
				objectNode.put(p.getName(), prov.getIntProperty(p.getName()));
			} else if(rawType == float.class || rawType == Float.class) {
				objectNode.put(p.getName(), prov.getFloatProperty(p.getName()));
			} else if(rawType == double.class || rawType == Double.class) {
				objectNode.put(p.getName(), prov.getDoubleProperty(p.getName()));
			} else if(rawType == String.class) {
				objectNode.put(p.getName(), prov.getStringProperty(p.getName()));
			} else if(rawType == boolean.class || rawType == Boolean.class) {
				objectNode.put(p.getName(), prov.getBooleanProperty(p.getName()));
			}
		}
		return mapper.convertValue(objectNode, obj);
	}

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
			//
			if(rawType == int.class || rawType == Integer.class) {
				coll.addIntProperty(p.getName(), p.getName(), defaultVal == null ? 0 : Integer.valueOf(defaultVal));
				coll.addPropertyDescriptions(p.getName(), desc);
			} else if(rawType == float.class || rawType == Float.class) {
				coll.addFloatProperty(p.getName(), p.getName(), Float.valueOf(defaultVal));
				coll.addPropertyDescriptions(p.getName(), desc);
			} else if(rawType == double.class || rawType == Double.class) {
				coll.addDoubleProperty(p.getName(), p.getName(), Double.valueOf(defaultVal));
				coll.addPropertyDescriptions(p.getName(), desc);
			} else if(rawType == String.class) {
				coll.addStringProperty(p.getName(), p.getName(), defaultVal);
				coll.addPropertyDescriptions(p.getName(), desc);
			} else if(rawType == boolean.class || rawType == Boolean.class) {
				coll.addBooleanProperty(p.getName(), p.getName(), Boolean.valueOf(defaultVal));
				coll.addPropertyDescriptions(p.getName(), desc);
			}
		}
	}
}
