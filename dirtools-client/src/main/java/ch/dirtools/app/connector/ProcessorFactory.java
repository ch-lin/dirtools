package ch.dirtools.app.connector;

import ch.dirtools.app.connector.digest.CRC32Processor;
import ch.dirtools.app.connector.digest.MD5Processor;
import ch.dirtools.app.connector.digest.SHA1Processor;
import ch.dirtools.app.connector.http.HttpProcessor;

import java.util.HashMap;

public class ProcessorFactory {

    private static HashMap<ProcessorType, Processor> map = new HashMap<>();

    static {
        map.put(ProcessorType.HttpConnector, new HttpProcessor());
        map.put(ProcessorType.MD5Connector, new MD5Processor());
        map.put(ProcessorType.SHA1Connector, new SHA1Processor());
        map.put(ProcessorType.CRC32Connector, new CRC32Processor());
    }

    public static Processor getConnector(ProcessorType type) {
        return map.get(type);
    }

}