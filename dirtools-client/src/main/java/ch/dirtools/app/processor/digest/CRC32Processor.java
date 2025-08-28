package ch.dirtools.app.processor.digest;

import ch.dirtools.app.files.Data;

public class CRC32Processor extends Digest {

    @Override
    public void process(Data data) {
        this.update(data.getData());
    }
}