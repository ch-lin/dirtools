package ch.dirtools.app.files;

import ch.dirtools.app.connector.Processor;

import java.util.ArrayList;

public class FolderExplorer {

    String baseFolder;

    ArrayList<Processor> processors;

    public FolderExplorer(String baseFolder) {
        this.baseFolder = baseFolder;
    }

    public ArrayList<Processor> getProcessors() {
        return this.processors;
    }

    public void setProcessors(ArrayList<Processor> processors) {
        this.processors = processors;
    }

    public void explore(boolean processSubFolder) {

        if(this.processors != null && this.processors.size() != 0) {


        }
    }
}
