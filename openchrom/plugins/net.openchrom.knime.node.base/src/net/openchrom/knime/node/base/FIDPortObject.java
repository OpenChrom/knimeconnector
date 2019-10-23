/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *Alexander Kerner - initial API and implementation
 *******************************************************************************/
package net.openchrom.knime.node.base;

import java.util.Collection;
import java.util.List;

import org.eclipse.chemclipse.nmr.model.core.FIDMeasurement;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

/**
 * {@link PortObject} that holds a collection of {@link FIDMeasurement}s.
 * 
 * @author Alexander Kerner
 *
 */
public class FIDPortObject extends GenericPortObject<FIDMeasurement> {

    public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(FIDPortObject.class);

    public static final class Serializer extends AbstractPortObjectSerializer<FIDPortObject> {
    }

    public FIDPortObject() {
	super();

    }

    public FIDPortObject(Collection<? extends FIDMeasurement> measurements, GenericPortObjectSpec portObjectSpec) {
	super(measurements, portObjectSpec);

    }

    public FIDPortObject(Collection<? extends FIDMeasurement> measurements) {
	super(measurements);

    }

    @Override
    public List<FIDMeasurement> getMeasurements() {
	return super.getMeasurements();
    }

}
