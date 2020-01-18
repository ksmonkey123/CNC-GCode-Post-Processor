package ch.awae.cnc.file;

import java.io.File;
import java.util.UUID;

class FilesystemBackedFileMapping implements FileMapping {

    private final UUID fileId = UUID.randomUUID();
    private final File file;

    FilesystemBackedFileMapping(File file) {
        this.file = file;
    }

    @Override
    public UUID getFileId() {
        return fileId;
    }

    @Override
    public String getPath() {
        return file.getPath();
    }

    @Override
    public boolean isReloadable() {
        return true;
    }

    File getFile() {
        return file;
    }
}
