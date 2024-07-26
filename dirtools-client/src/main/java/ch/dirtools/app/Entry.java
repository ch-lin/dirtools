package ch.dirtools.app;

import ch.dirtools.app.connector.Processor;
import ch.dirtools.app.connector.ProcessorFactory;
import ch.dirtools.app.connector.ProcessorType;
import ch.dirtools.app.files.FolderExplorer;

import java.util.ArrayList;

public class Entry {

    public static void main(String[] args) {

        ArrayList<Processor> Processors = new ArrayList<>();
        Processors.add(ProcessorFactory.getConnector(ProcessorType.MD5Connector));
        Processors.add(ProcessorFactory.getConnector(ProcessorType.CRC32Connector));
        Processors.add(ProcessorFactory.getConnector(ProcessorType.SHA1Connector));
        Processors.add(ProcessorFactory.getConnector(ProcessorType.HttpConnector));

        FolderExplorer explorer = new FolderExplorer();
        explorer.explore("", Processors);
    }
}
