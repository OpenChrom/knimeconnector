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
package net.openchrom.knime.node.nmr.ft.portobject;

 import java.io.IOException;

 import javax.swing.JComponent;

 import org.knime.core.node.port.PortObjectSpec;
 import org.knime.core.node.port.PortObjectSpecZipInputStream;
 import org.knime.core.node.port.PortObjectSpecZipOutputStream;

 public class NMRMeasurementPortObjectSpec implements PortObjectSpec {

	 public static final class Serializer extends PortObjectSpecSerializer<NMRMeasurementPortObjectSpec> {

		 @Override
		 public void savePortObjectSpec(final NMRMeasurementPortObjectSpec portObjectSpec,
				 final PortObjectSpecZipOutputStream out) throws IOException {
			 // TODO Auto-generated method stub

		 }

		 @Override
		 public NMRMeasurementPortObjectSpec loadPortObjectSpec(final PortObjectSpecZipInputStream in)
				 throws IOException {
			 return new NMRMeasurementPortObjectSpec();
		 }
	 }

	 @Override
	 public JComponent[] getViews() {
		 // TODO Auto-generated method stub
		 return null;
	 }

 }
