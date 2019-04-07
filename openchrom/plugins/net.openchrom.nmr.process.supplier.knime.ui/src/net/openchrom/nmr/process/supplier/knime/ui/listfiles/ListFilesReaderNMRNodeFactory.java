package net.openchrom.nmr.process.supplier.knime.ui.listfiles;

import org.eclipse.chemclipse.nmr.converter.core.ScanConverterNMR;

import net.openchrom.process.supplier.knime.ui.node.listfiles.ListFilesFactory;

public class ListFilesReaderNMRNodeFactory extends ListFilesFactory {

	public ListFilesReaderNMRNodeFactory() {

		super("Select nmr files", ScanConverterNMR.getScanConverterSupport());
	}
}
