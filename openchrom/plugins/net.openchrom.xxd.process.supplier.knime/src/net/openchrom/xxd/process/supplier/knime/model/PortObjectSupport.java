/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 *
 * This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.model;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.port.PortObject;

public class PortObjectSupport {

	public static BufferedDataTable getBufferedDataTable(PortObject[] inObjects) {

		for(Object object : inObjects) {
			if(object instanceof BufferedDataTable) {
				return (BufferedDataTable)object;
			}
		}
		return null;
	}

	public static ChromatogramSelectionMSDPortObject getChromatogramSelectionMSDPortObject(PortObject[] inObjects) {

		for(Object object : inObjects) {
			if(object instanceof ChromatogramSelectionMSDPortObject) {
				return (ChromatogramSelectionMSDPortObject)object;
			}
		}
		return null;
	}
}
