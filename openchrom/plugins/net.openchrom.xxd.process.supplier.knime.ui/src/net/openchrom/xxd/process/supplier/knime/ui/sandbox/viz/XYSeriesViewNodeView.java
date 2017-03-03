package net.openchrom.xxd.process.supplier.knime.ui.sandbox.viz;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.knime.core.node.AbstractNodeView;

/**
 * TODO possibly embed the swt-view into a swing/awt frame (couldn't make that
 * work so far - see e.g.
 * http://rolandtapken.de/blog/2011-01/java-howto-embed-swt-widget-swing-jframe
 * or http://www.eclipsezone.com/eclipse/forums/m91978575.html)
 *
 */
public class XYSeriesViewNodeView extends AbstractNodeView<XYSeriesViewNodeModel> {

	protected XYSeriesViewNodeView(XYSeriesViewNodeModel nodeModel) {
		super(nodeModel);
	}

	@Override
	protected void callOpenView(String title) {
		//active the graph view containing (using the swt-graph library)
	}

	@Override
	protected void callCloseView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void modelChanged() {
		// TODO Auto-generated method stub

	}

}
