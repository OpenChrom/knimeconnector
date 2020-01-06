/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
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
 * Alexander Kerner - initial API and implementation
 *******************************************************************************/
package net.openchrom.knime.node.nmr.io.bruker.nmr;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;



/**
 * {@link NodeFactory} for the Bruker 1r/1i reader node.
 * 
 * @author Alexander Kerner
 *
 */
public class NMRReaderNodeFactory extends NodeFactory<NMRReaderNodeModel> {

    @Override
    public NMRReaderNodeModel createNodeModel() {
	return new NMRReaderNodeModel();
    }

    @Override
    protected int getNrNodeViews() {
	return 0;
    }

    @Override
    public NodeView<NMRReaderNodeModel> createNodeView(final int viewIndex, final NMRReaderNodeModel nodeModel) {
	return null;
    }

    @Override
    protected boolean hasDialog() {
	return true;
    }

    @Override
    protected NodeDialogPane createNodeDialogPane() {
	return new NMRReaderNodeDialogPane();
    }

}
