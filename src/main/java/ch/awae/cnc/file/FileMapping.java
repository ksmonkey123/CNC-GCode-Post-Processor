package ch.awae.cnc.file;

import java.util.UUID;

public interface FileMapping {

    UUID getFileId();
    String getPath();
    boolean isReloadable();
}
