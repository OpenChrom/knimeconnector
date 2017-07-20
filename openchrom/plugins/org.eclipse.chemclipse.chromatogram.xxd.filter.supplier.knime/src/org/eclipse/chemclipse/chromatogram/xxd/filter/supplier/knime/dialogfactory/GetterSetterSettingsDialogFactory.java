/*******************************************************************************
 * Copyright (c) 2017 hornm.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * hornm - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.dialogfactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.port.PortObjectSpec;

public class GetterSetterSettingsDialogFactory<S> implements SettingsDialogFactory<S> {

	@Override
	public int getPriority() {

		// should be of very low priority
		return Integer.MIN_VALUE;
	}

	@Override
	public NodeDialogPane createDialog(Class<? extends S> settingsObjectClass) {

		Map<String, String> props = extractProperties(settingsObjectClass);
		return new DefaultNodeSettingsPane() {

			{
				for(Entry<String, String> entry : props.entrySet()) {
					if(entry.getValue().equals("int")) {
						addDialogComponent(new DialogComponentNumber(new SettingsModelInteger(entry.getKey(), 0), entry.getKey(), 1));
					}
				}
			}
		};
	}
	
	
	@Override
	public SettingsObject<S> createSettings(Class<? extends S> settingsObjectClass) {
		
		Map<String, String> props = extractProperties(settingsObjectClass);
		
		final S settingsObject;
		try {
			settingsObject = settingsObjectClass.newInstance();
		} catch(InstantiationException | IllegalAccessException e) {
			//TODO
			throw new RuntimeException(e);
		}
	
		return new SettingsObject<S>() {
			@Override
			public void saveSettingsTo(NodeSettingsWO settings) {

				
				for(Entry<String, String> entry : props.entrySet()) {
					if(entry.getValue().equals("int")) {
						int value = (int)invokeGetMethod(entry.getKey(), settingsObject);
						SettingsModelInteger sm = new SettingsModelInteger(entry.getKey(), 0);
						sm.setIntValue(value);
						sm.saveSettingsTo(settings);
					}
				}
			}

			@Override
			public void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {

				Map<String, String> props = extractProperties((Class<S>)settingsObject.getClass());
				for(Entry<String, String> entry : props.entrySet()) {
					if(entry.getValue().equals("int")) {
						SettingsModelInteger sm = new SettingsModelInteger(entry.getKey(), 0);
						sm.loadSettingsFrom(settings);
						invokeSetMethod(entry.getKey(), settingsObject, sm.getIntValue(), int.class);
					}
				}
			}

			@Override
			public void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

				Map<String, String> props = extractProperties((Class<S>)settingsObject.getClass());
				for(Entry<String, String> entry : props.entrySet()) {
					if(entry.getValue().equals("int")) {
						int value = (int)invokeGetMethod(entry.getKey(), settingsObject);
						SettingsModelInteger sm = new SettingsModelInteger(entry.getKey(), 0);
						sm.setIntValue(value);
						sm.validateSettings(settings);
					}
				}
			}
			
			@Override
			public S getObject() {
			
				return settingsObject;
			}
		};
	}

	

	@Override
	public boolean conforms(Class<S> settingsObjectClass) {

		return true;
	}

	@Override
	public boolean conforms(S settingsObject) {

		return true;
	}

	/**
	 * Extract the properties via reflection represented by getter and setter methods.
	 * 
	 * @param
	 */
	private Map<String, String> extractProperties(Class<? extends S> obj) {

		Method[] methods = obj.getDeclaredMethods();
		Map<String, String> getter = new HashMap<>();
		Map<String, String> setter = new HashMap<>();
		Map<String, String> props = new HashMap<>();
		for(Method method : methods) {
			// public methods only
			if(Modifier.isPublic(method.getModifiers())) {
				// try to find getter AND setter method with the same name and return/parameter type
				String propName = null;
				if(method.getName().startsWith("get")) {
					propName = method.getName().substring(3);
					getter.put(propName, method.getReturnType().getCanonicalName());
				} else if(method.getName().startsWith("set") && method.getParameterTypes().length == 1) {
					propName = method.getName().substring(3);
					setter.put(propName, method.getParameterTypes()[0].getCanonicalName());
				}
				if(propName != null && getter.get(propName) != null && setter.get(propName) != null && getter.get(propName).equals(setter.get(propName))) {
					props.put(propName, getter.get(propName));
				}
			}
		}
		return props;
	}

	private Object invokeGetMethod(String prop, S settingsObj) {

		try {
			return settingsObj.getClass().getMethod("get" + prop).invoke(settingsObj);
		} catch(NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	private void invokeSetMethod(String prop, S settingsObj, Object value, Class<?> paramClass) {

		try {
			settingsObj.getClass().getMethod("set" + prop, paramClass).invoke(settingsObj, value);
		} catch(NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO
			throw new RuntimeException(e);
		}
	}
}
