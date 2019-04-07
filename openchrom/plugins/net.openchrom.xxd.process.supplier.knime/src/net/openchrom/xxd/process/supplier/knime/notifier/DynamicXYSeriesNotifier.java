/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
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
package net.openchrom.xxd.process.supplier.knime.notifier;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;

public class DynamicXYSeriesNotifier implements IXYSeriesListener {

	@Inject
	private IEventBroker eventBroker;
	private Map<String, Object> map;

	public DynamicXYSeriesNotifier() {
		map = new HashMap<String, Object>();
	}

	@Override
	public void update(double[] ySeries, String id) {

		map.clear();
		map.put(IXYSeriesListener.PROPERTY_Y_SERIES, ySeries);
		map.put(IXYSeriesListener.PROPERTY_ID, id);
		eventBroker.send(IXYSeriesListener.TOPIC_XY_SERIES_PART_UPDATE, map);
	}
}
