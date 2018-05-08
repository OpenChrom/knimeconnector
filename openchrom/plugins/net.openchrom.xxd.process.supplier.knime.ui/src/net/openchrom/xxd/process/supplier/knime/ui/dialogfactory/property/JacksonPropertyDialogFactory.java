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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.support.settings.DoubleSettingsProperty;
import org.eclipse.chemclipse.support.settings.FileSettingProperty;
import org.eclipse.chemclipse.support.settings.FloatSettingsProperty;
import org.eclipse.chemclipse.support.settings.IntSettingsProperty;
import org.eclipse.chemclipse.support.settings.IonsSelectionSettingProperty;
import org.eclipse.chemclipse.support.settings.StringSelectionSettingProperty;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
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
			AnnotatedField annotatedField = p.getField();
			if(annotatedField != null) {
				Class<?> rawType = annotatedField.getRawType();
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
			AnnotatedField annotatedField = p.getField();
			if(annotatedField != null) {
				Class<?> rawType = annotatedField.getRawType();
				String name = p.getName();
				String id = p.getName();
				String desc = p.getMetadata().getDescription();
				String defaultVal = p.getMetadata().getDefaultValue();
				//
				if(rawType == int.class || rawType == Integer.class) {
					int min = Integer.MIN_VALUE;
					int max = Integer.MAX_VALUE;
					int step = 1;
					IntSettingsProperty intSettingsProperty = annotatedField.getAnnotation(IntSettingsProperty.class);
					if(intSettingsProperty != null) {
						min = intSettingsProperty.minValue();
						max = intSettingsProperty.maxValue();
						step = intSettingsProperty.step();
					}
					coll.addIntProperty(id, name, defaultVal == null ? 0 : Integer.valueOf(defaultVal), desc, step, min, max);
				} else if(rawType == float.class || rawType == Float.class) {
					float min = Float.MIN_VALUE;
					float max = Float.MAX_VALUE;
					int step = 1;
					FloatSettingsProperty floatSettingsProperty = annotatedField.getAnnotation(FloatSettingsProperty.class);
					if(floatSettingsProperty != null) {
						min = floatSettingsProperty.minValue();
						max = floatSettingsProperty.maxValue();
						step = floatSettingsProperty.step();
					}
					coll.addFloatProperty(id, name, Float.valueOf(defaultVal), desc, step, min, max);
				} else if(rawType == double.class || rawType == Double.class) {
					double min = Double.MIN_VALUE;
					double max = Double.MAX_VALUE;
					int step = 1;
					DoubleSettingsProperty doubleSettingsProperty = annotatedField.getAnnotation(DoubleSettingsProperty.class);
					if(doubleSettingsProperty != null) {
						min = doubleSettingsProperty.minValue();
						max = doubleSettingsProperty.maxValue();
						step = doubleSettingsProperty.step();
					}
					coll.addDoubleProperty(id, name, Double.valueOf(defaultVal), desc, step, min, max);
				} else if(rawType == String.class) {
					FileSettingProperty fileSettingProperty = annotatedField.getAnnotation(FileSettingProperty.class);
					if(fileSettingProperty != null) {
						coll.addFileProperty(id, name, defaultVal, desc, obj.getName());
					} else {
						StringSelectionSettingProperty selectionSettingProperty = annotatedField.getAnnotation(StringSelectionSettingProperty.class);
						if(selectionSettingProperty != null) {
							String[] ids = selectionSettingProperty.ids();
							String[] labels = selectionSettingProperty.labels();
							if(labels.length == 0) {
								labels = ids;
							}
							Map<String, String> mapIds = new HashMap<>();
							for(int i = 0; i < labels.length; i++) {
								mapIds.put(labels[i], ids[i]);
							}
							coll.addStringProperty(id, name, defaultVal, desc, Arrays.asList(labels), mapIds);
						} else {
							IonsSelectionSettingProperty ionSelectionSettingProperty = annotatedField.getAnnotation(IonsSelectionSettingProperty.class);
							if(ionSelectionSettingProperty != null) {
								coll.addIonSelectionProperty(id, name, defaultVal, desc);
							} else {
								coll.addStringProperty(id, name, defaultVal, desc);
							}
						}
					}
				} else if(rawType == boolean.class || rawType == Boolean.class) {
					coll.addBooleanProperty(id, name, Boolean.valueOf(defaultVal), desc);
				}
			}
		}
	}
}
