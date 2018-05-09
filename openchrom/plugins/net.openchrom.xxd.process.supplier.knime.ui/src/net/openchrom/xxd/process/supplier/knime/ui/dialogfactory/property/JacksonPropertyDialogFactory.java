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
 * Jan Holy - implementation
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
import org.eclipse.chemclipse.support.settings.IntSettingsProperty.Validation;
import org.eclipse.chemclipse.support.settings.IonsSelectionSettingProperty;
import org.eclipse.chemclipse.support.settings.MultiFileSettingProperty;
import org.eclipse.chemclipse.support.settings.StringSelectionRadioButtonsSettingProperty;
import org.eclipse.chemclipse.support.settings.StringSelectionSettingProperty;
import org.knime.core.node.util.ButtonGroupEnumInterface;

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

	@SuppressWarnings("incomplete-switch")
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
				final String name = p.getName();
				final String id = p.getName();
				final String desc = p.getMetadata().getDescription();
				final String defaultVal = p.getMetadata().getDefaultValue();
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
						Validation validation = intSettingsProperty.validation();
						switch(validation) {
							case ODD_NUMBER:
								if(step % 2 != 0) {
									step = 2;
								}
								coll.addIntOddNumberProperty(id, name, defaultVal == null ? 0 : Integer.valueOf(defaultVal), desc, step, min, max);
								continue;
						}
					}
					coll.addIntProperty(id, name, defaultVal == null ? 0 : Integer.valueOf(defaultVal), desc, step, min, max);
					continue;
				}
				if(rawType == float.class || rawType == Float.class) {
					float min = -Float.MAX_VALUE;
					float max = Float.MAX_VALUE;
					int step = 1;
					FloatSettingsProperty floatSettingsProperty = annotatedField.getAnnotation(FloatSettingsProperty.class);
					if(floatSettingsProperty != null) {
						min = floatSettingsProperty.minValue();
						max = floatSettingsProperty.maxValue();
						step = floatSettingsProperty.step();
					}
					coll.addFloatProperty(id, name, defaultVal == null ? 0.0f : Float.valueOf(defaultVal), desc, step, min, max);
					continue;
				}
				if(rawType == double.class || rawType == Double.class) {
					double min = -Double.MAX_VALUE;
					double max = Double.MAX_VALUE;
					int step = 1;
					DoubleSettingsProperty doubleSettingsProperty = annotatedField.getAnnotation(DoubleSettingsProperty.class);
					if(doubleSettingsProperty != null) {
						min = doubleSettingsProperty.minValue();
						max = doubleSettingsProperty.maxValue();
						step = doubleSettingsProperty.step();
					}
					coll.addDoubleProperty(id, name, defaultVal == null ? 0.0 : Double.valueOf(defaultVal), desc, step, min, max);
					continue;
				}
				if(rawType == String.class) {
					FileSettingProperty fileSettingProperty = annotatedField.getAnnotation(FileSettingProperty.class);
					if(fileSettingProperty != null) {
						coll.addFileProperty(id, name, defaultVal, desc, obj.getName(), fileSettingProperty.validExtensions());
						continue;
					}
					MultiFileSettingProperty multiFileSettingProperty = annotatedField.getAnnotation(MultiFileSettingProperty.class);
					if(multiFileSettingProperty != null) {
						coll.addMultiFileProperty(id, name, defaultVal, desc, obj.getName(), multiFileSettingProperty.validExtensions());
						continue;
					}
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
						continue;
					}
					IonsSelectionSettingProperty ionSelectionSettingProperty = annotatedField.getAnnotation(IonsSelectionSettingProperty.class);
					if(ionSelectionSettingProperty != null) {
						coll.addIonSelectionProperty(id, name, defaultVal, desc);
						continue;
					}
					StringSelectionRadioButtonsSettingProperty stringSelectionRadioButtonsSettingProperty = annotatedField.getAnnotation(StringSelectionRadioButtonsSettingProperty.class);
					if(stringSelectionRadioButtonsSettingProperty != null) {
						final String[] labels = stringSelectionRadioButtonsSettingProperty.labels();
						final String[] ids = stringSelectionRadioButtonsSettingProperty.ids();
						final String[] tooltips = stringSelectionRadioButtonsSettingProperty.tooltips();
						final int size = labels.length;
						ButtonGroupEnumInterface[] listButtons = new ButtonGroupEnumInterface[size];
						for(int i = 0; i < size; i++) {
							final int j = i;
							ButtonGroupEnumInterface buttonGroupEnumInterface = new ButtonGroupEnumInterface() {

								@Override
								public boolean isDefault() {

									return ids[j].equals(defaultVal);
								}

								@Override
								public String getToolTip() {

									if(tooltips.length > 0) {
										return tooltips[j];
									}
									return null;
								}

								@Override
								public String getText() {

									return labels[j];
								}

								@Override
								public String getActionCommand() {

									return ids[j];
								}
							};
							listButtons[i] = buttonGroupEnumInterface;
						}
						coll.addStringProperty(id, name, defaultVal, desc, listButtons);
						continue;
					}
					coll.addStringProperty(id, name, defaultVal, desc);
					continue;
				}
				if(rawType == boolean.class || rawType == Boolean.class) {
					coll.addBooleanProperty(id, name, Boolean.valueOf(defaultVal), desc);
					continue;
				}
			}
		}
	}
}
