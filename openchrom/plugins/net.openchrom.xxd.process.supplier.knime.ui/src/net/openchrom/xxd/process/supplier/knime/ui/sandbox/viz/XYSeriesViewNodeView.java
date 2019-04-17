/*******************************************************************************
 * Copyright (c) 2017 Universität Konstanz.
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
 * Dr. Martin Horn - initial API and implementation
 * Alexander Kerner - fix type hierarchy
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.sandbox.viz;

import org.knime.core.node.NodeView;

import net.openchrom.xxd.process.supplier.knime.notifier.XYSeriesNotifier;

/**
 * TODO possibly embed the swt-view into a swing/awt frame (couldn't make that
 * work so far - see e.g.
 * http://rolandtapken.de/blog/2011-01/java-howto-embed-swt-widget-swing-jframe
 * or http://www.eclipsezone.com/eclipse/forums/m91978575.html)
 *
 */
public class XYSeriesViewNodeView extends NodeView<XYSeriesViewNodeModel> {

	protected XYSeriesViewNodeView(XYSeriesViewNodeModel nodeModel) {
		super(nodeModel);
	}

	@Override
	protected void callOpenView(String title) {

		getNodeModel().getXSeries(0);
		double[] ySeries = getNodeModel().getYSeries(0);
		//
		XYSeriesNotifier.fireUpdateChange(ySeries, "FFT");
	}

	@Override
	protected void modelChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onClose() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onOpen() {
		// TODO Auto-generated method stub

	}
}
