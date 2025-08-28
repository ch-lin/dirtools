package ch.dirtools.app.processor.digest;

import java.security.MessageDigest;

public abstract class Digest implements ch.dirtools.app.connector.Processor {

    MessageDigest complete;

    public void update(byte[] data, int numRead) {
        if (complete != null) complete.update(data, 0, numRead);
    }

    public String getChecksum() {
        byte[] b = complete.digest();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }
}
