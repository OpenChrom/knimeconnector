/*******************************************************************************
 * Copyright (c) 2018, 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.process.supplier.knime.dialogfactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.chemclipse.support.settings.DoubleSettingsProperty;
import org.eclipse.chemclipse.support.settings.DynamicSettingsProperty;
import org.eclipse.chemclipse.support.settings.EnumSelectionRadioButtonsSettingProperty;
import org.eclipse.chemclipse.support.settings.EnumSelectionSettingProperty;
import org.eclipse.chemclipse.support.settings.FileSettingProperty;
import org.eclipse.chemclipse.support.settings.FileSettingProperty.DialogType;
import org.eclipse.chemclipse.support.settings.FloatSettingsProperty;
import org.eclipse.chemclipse.support.settings.IDynamicSettingProperty;
import org.eclipse.chemclipse.support.settings.IntSettingsProperty;
import org.eclipse.chemclipse.support.settings.IntSettingsProperty.Validation;
import org.eclipse.chemclipse.support.settings.LabelSettingsProperty;
import org.eclipse.chemclipse.support.settings.MultiFileSettingProperty;
import org.eclipse.chemclipse.support.settings.PreferenceProperty;
import org.eclipse.chemclipse.support.settings.RetentionTimeMinutesProperty;
import org.eclipse.chemclipse.support.settings.StringSelectionRadioButtonsSettingProperty;
import org.eclipse.chemclipse.support.settings.StringSelectionSettingProperty;
import org.eclipse.chemclipse.support.settings.StringSettingsProperty;
import org.eclipse.chemclipse.support.util.PreferencePropertyUtils;
import org.knime.core.node.util.ButtonGroupEnumInterface;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyCollector;
import net.openchrom.process.supplier.knime.dialogfactory.property.PropertyProvider;

public class JacksonSettingObjectSupplier<SO> implements SettingObjectSupplier<SO> {

	private ObjectMapper mapper = new ObjectMapper();

	public JacksonSettingObjectSupplier() {
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	@Override
	public SO createSettingsObject(Class<? extends SO> settingsObjectClass, PropertyProvider prov) {

		ObjectNode objectNode = mapper.createObjectNode();
		JavaType javaType = mapper.getSerializationConfig().constructType(settingsObjectClass);
		BeanDescription beanDesc = mapper.getSerializationConfig().introspect(javaType);
		List<BeanPropertyDefinition> props = beanDesc.findProperties();
		for(BeanPropertyDefinition p : props) {
			AnnotatedField annotatedField = p.getField();
			if(annotatedField != null) {
				Class<?> rawType = annotatedField.getRawType();
				final String name = p.getName();
				final String id = p.getName();
				//
				if(rawType == int.class || rawType == Integer.class) {
					objectNode.put(name, prov.getIntProperty(id));
				} else if(rawType == float.class || rawType == Float.class) {
					objectNode.put(name, prov.getFloatProperty(id));
				} else if(rawType == double.class || rawType == Double.class) {
					objectNode.put(name, prov.getDoubleProperty(id));
				} else if(rawType == String.class) {
					objectNode.put(name, prov.getStringProperty(id));
				} else if(rawType == boolean.class || rawType == Boolean.class) {
					objectNode.put(name, prov.getBooleanProperty(id));
				} else if(rawType.isEnum()) {
					Enum<?> enumValue = Enum.valueOf((Class<? extends Enum>)rawType, prov.getStringProperty(id));
					objectNode.putPOJO(name, enumValue);
				}
			}
		}
		return mapper.convertValue(objectNode, settingsObjectClass);
	}

	@Override
	public void extractProperties(Class<? extends SO> settingsObjectClass, PropertyCollector coll) {

		try {
			JavaType javaType = mapper.getSerializationConfig().constructType(settingsObjectClass);
			BeanDescription beanDesc = mapper.getSerializationConfig().introspect(javaType);
			List<BeanPropertyDefinition> props = beanDesc.findProperties();
			for(BeanPropertyDefinition p : props) {
				AnnotatedField annotatedField = p.getField();
				if(annotatedField != null) {
					Class<?> rawType = annotatedField.getRawType();
					final String name = p.getName();
					final String id = p.getName();
					final String desc = p.getMetadata().getDescription();
					final String defaultValue = p.getMetadata().getDefaultValue();
					// Preferences
					PreferenceProperty preferenceProperty = annotatedField.getAnnotation(PreferenceProperty.class);
					// Support dynamic property
					DynamicSettingsProperty dynamicSettingsProperty = annotatedField.getAnnotation(DynamicSettingsProperty.class);
					IDynamicSettingProperty dynamicSetting = null;
					if(dynamicSettingsProperty != null) {
						try {
							dynamicSetting = dynamicSettingsProperty.dynamicSettingPropertyClass().newInstance();
						} catch(InstantiationException
								| IllegalAccessException e) {
						}
					}
					final IDynamicSettingProperty dynamicSettingFinal = dynamicSetting;
					Function<Class<? extends Annotation>, Annotation> getAnnotation = acls -> {
						Annotation a = annotatedField.getAnnotation(acls);
						if(a != null) {
							return a;
						} else {
							if(dynamicSettingFinal != null) {
								return dynamicSettingFinal.getAnnotation(acls);
							}
						}
						return null;
					};
					// add label
					LabelSettingsProperty labelSettingsProperty = (LabelSettingsProperty)getAnnotation.apply(LabelSettingsProperty.class);
					if(labelSettingsProperty != null) {
						coll.addLabel(labelSettingsProperty.label());
					}
					//
					if(rawType == int.class || rawType == Integer.class) {
						int min = Integer.MIN_VALUE;
						int max = Integer.MAX_VALUE;
						int step = 1;
						int defVal = defaultValue == null ? 0 : Integer.valueOf(defaultValue);
						defVal = PreferencePropertyUtils.getPreference(preferenceProperty, defVal);
						IntSettingsProperty intSettingsProperty = (IntSettingsProperty)getAnnotation.apply(IntSettingsProperty.class);
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
									coll.addIntOddNumberProperty(id, name, defVal, desc, step, min, max);
									continue;
								case NONE:
									coll.addIntProperty(id, name, defVal, desc, step, min, max);
									continue;
							}
						}
						RetentionTimeMinutesProperty retentionTimeMinutesProperty = (RetentionTimeMinutesProperty)getAnnotation.apply(RetentionTimeMinutesProperty.class);
						if(retentionTimeMinutesProperty != null) {
							min = retentionTimeMinutesProperty.minValue();
							max = retentionTimeMinutesProperty.maxValue();
							step = retentionTimeMinutesProperty.step();
							coll.addRetentionTimeMinutesProperty(id, name, defVal, desc, step, min, max);
							continue;
						}
						coll.addIntProperty(id, name, defVal, desc, step, min, max);
						continue;
					}
					if(rawType == float.class || rawType == Float.class) {
						float min = -Float.MAX_VALUE;
						float max = Float.MAX_VALUE;
						float step = 1f;
						float defVal = defaultValue == null ? 0.0f : Float.valueOf(defaultValue);
						defVal = PreferencePropertyUtils.getPreference(preferenceProperty, defVal);
						FloatSettingsProperty floatSettingsProperty = (FloatSettingsProperty)getAnnotation.apply(FloatSettingsProperty.class);
						if(floatSettingsProperty != null) {
							min = floatSettingsProperty.minValue();
							max = floatSettingsProperty.maxValue();
							step = floatSettingsProperty.step();
						}
						coll.addFloatProperty(id, name, defVal, desc, step, min, max);
						continue;
					}
					if(rawType == double.class || rawType == Double.class) {
						double min = -Double.MAX_VALUE;
						double max = Double.MAX_VALUE;
						double step = 1.0;
						double defVal = defaultValue == null ? 0.0 : Double.valueOf(defaultValue);
						defVal = PreferencePropertyUtils.getPreference(preferenceProperty, defVal);
						DoubleSettingsProperty doubleSettingsProperty = (DoubleSettingsProperty)getAnnotation.apply(DoubleSettingsProperty.class);
						if(doubleSettingsProperty != null) {
							min = doubleSettingsProperty.minValue();
							max = doubleSettingsProperty.maxValue();
							step = doubleSettingsProperty.step();
						}
						coll.addDoubleProperty(id, name, defVal, desc, step, min, max);
						continue;
					}
					if(rawType == String.class) {
						String pom = defaultValue == null ? "" : defaultValue;
						String defVal = PreferencePropertyUtils.getPreference(preferenceProperty, pom);
						FileSettingProperty fileSettingProperty = (FileSettingProperty)getAnnotation.apply(FileSettingProperty.class);
						if(fileSettingProperty != null) {
							DialogType dialogType = fileSettingProperty.dialogType();
							boolean onlyDirecotry = fileSettingProperty.onlyDirectory();
							coll.addFileProperty(id, name, defVal, desc, settingsObjectClass.getName(), dialogType, onlyDirecotry, fileSettingProperty.validExtensions());
							continue;
						}
						MultiFileSettingProperty multiFileSettingProperty = (MultiFileSettingProperty)getAnnotation.apply(MultiFileSettingProperty.class);
						if(multiFileSettingProperty != null) {
							coll.addMultiFileProperty(id, name, defVal, desc, settingsObjectClass.getName(), multiFileSettingProperty.validExtensions());
							continue;
						}
						StringSelectionSettingProperty selectionSettingProperty = (StringSelectionSettingProperty)getAnnotation.apply(StringSelectionSettingProperty.class);
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
							coll.addStringProperty(id, name, defVal, desc, Arrays.asList(labels), mapIds);
							continue;
						}
						//
						StringSelectionRadioButtonsSettingProperty stringSelectionRadioButtonsSettingProperty = (StringSelectionRadioButtonsSettingProperty)getAnnotation.apply(StringSelectionRadioButtonsSettingProperty.class);
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

										return ids[j].equals(defVal);
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
							coll.addStringProperty(id, name, defVal, desc, listButtons);
							continue;
						}
						String regExpr = "^.*$";
						StringSettingsProperty stringSettingsProperty = (StringSettingsProperty)getAnnotation.apply(StringSettingsProperty.class);
						if(stringSettingsProperty != null) {
							regExpr = stringSettingsProperty.regExp();
						}
						coll.addStringProperty(id, name, defVal, desc, regExpr);
						continue;
					}
					if(rawType == boolean.class || rawType == Boolean.class) {
						boolean pom = defaultValue == null ? false : Boolean.valueOf(defaultValue);
						boolean defVal = PreferencePropertyUtils.getPreference(preferenceProperty, pom);
						coll.addBooleanProperty(id, name, defVal, desc);
						continue;
					}
					if(rawType.isEnum()) {
						String pom = defaultValue == null ? "" : defaultValue;
						String defVal = PreferencePropertyUtils.getPreference(preferenceProperty, pom);
						EnumSelectionRadioButtonsSettingProperty enumSelectionRadioButtonsSettingProperty = (EnumSelectionRadioButtonsSettingProperty)getAnnotation.apply(EnumSelectionRadioButtonsSettingProperty.class);
						if(enumSelectionRadioButtonsSettingProperty != null) {
							Enum[] enums = (Enum[])rawType.getEnumConstants();
							int size = enums.length;
							ButtonGroupEnumInterface[] listButtons = new ButtonGroupEnumInterface[size];
							for(int i = 0; i < size; i++) {
								final int j = i;
								ButtonGroupEnumInterface buttonGroupEnumInterface = new ButtonGroupEnumInterface() {

									@Override
									public boolean isDefault() {

										return enums[j].name().equals(defVal);
									}

									@Override
									public String getText() {

										return enums[j].toString();
									}

									@Override
									public String getActionCommand() {

										return enums[j].name();
									}

									@Override
									public String getToolTip() {

										return null;
									}
								};
								listButtons[i] = buttonGroupEnumInterface;
							}
							coll.addStringProperty(id, name, defVal, desc, listButtons);
							continue;
						}
						EnumSelectionSettingProperty selectionSettingProperty = (EnumSelectionSettingProperty)getAnnotation.apply(EnumSelectionSettingProperty.class);
						if(selectionSettingProperty != null) {
							Enum[] enums = (Enum[])rawType.getEnumConstants();
							Map<String, String> mapIds = new HashMap<>();
							for(int i = 0; i < enums.length; i++) {
								mapIds.put(enums[i].toString(), enums[i].name());
							}
							String[] labels = Arrays.stream(enums).map(Enum::toString).toArray(String[]::new);
							coll.addStringProperty(id, name, defVal, desc, Arrays.asList(labels), mapIds);
							continue;
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println(settingsObjectClass);
		}
	}
}
