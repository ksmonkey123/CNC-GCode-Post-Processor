package ch.awae.cnc.file;

import java.util.UUID;

class VirtualFileMapping implements FileMapping {

    private final UUID fileId = UUID.randomUUID();
    private final String shortName  = "#" + hashCode();

    @Override
    public UUID getFileId() {
        return fileId;
    }

    @Override
    public String getPath() {
        return shortName;
    }

    @Override
    public String toString() {
        return "VirtualFileMapping{" +
                "fileId=" + fileId +
                '}';
    }

    @Override
    public boolean isReloadable() {
        return false;
    }
}
