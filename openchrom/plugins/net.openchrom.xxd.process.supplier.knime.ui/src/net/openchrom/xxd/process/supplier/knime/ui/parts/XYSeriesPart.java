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
package net.openchrom.xxd.process.supplier.knime.ui.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import net.openchrom.xxd.process.supplier.knime.notifier.IXYSeriesListener;
import net.openchrom.xxd.process.supplier.knime.ui.swt.XYSeriesChart;

public class XYSeriesPart {

	@Inject
	private Composite composite;
	@Inject
	private IEventBroker eventBroker;
	@Inject
	private EventHandler eventHandler;
	@Inject
	private MPart part;
	@Inject
	private EPartService partService;
	//
	private XYSeriesChart xySeriesChart;

	private boolean isPartVisible() {

		if(partService != null && partService.isPartVisible(part)) {
			return true;
		}
		return false;
	}

	@PostConstruct
	public void postConstruct() {

		xySeriesChart = new XYSeriesChart(composite, SWT.BORDER);
		subscribe();
	}

	@PreDestroy
	public void preDestroy() {

		unsubscribe();
	}

	@Focus
	public void setFocus() {

		xySeriesChart.setFocus();
	}

	/**
	 * Subscribes the selection update events.
	 */
	private void subscribe() {

		if(eventBroker != null) {
			eventHandler = new EventHandler() {

				@Override
				public void handleEvent(Event event) {

					double[] ySeries = (double[])event.getProperty(IXYSeriesListener.PROPERTY_Y_SERIES);
					String id = (String)event.getProperty(IXYSeriesListener.PROPERTY_ID);
					update(ySeries, id);
				}
			};
			eventBroker.subscribe(IXYSeriesListener.TOPIC_XY_SERIES_PART_UPDATE, eventHandler);
		}
	}

	private void unsubscribe() {

		if(eventBroker != null && eventHandler != null) {
			eventBroker.unsubscribe(eventHandler);
		}
	}

	private void update(double[] ySeries, String id) {

		if(isPartVisible()) {
			xySeriesChart.update(ySeries, id);
		}
	}
}