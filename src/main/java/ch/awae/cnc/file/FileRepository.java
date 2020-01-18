package ch.awae.cnc.file;

import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Validated
@Repository
public class FileRepository {

    private final HashMap<UUID, List<String>> fileContents = new HashMap<>();

    public FileMapping createVirtualFile() {
        VirtualFileMapping mapping = new VirtualFileMapping();
        fileContents.put(mapping.getFileId(), new ArrayList<>());
        return mapping;
    }

    public FileMapping loadFile(@NotNull File file) throws IOException {
        FilesystemBackedFileMapping mapping = new FilesystemBackedFileMapping(file);
        loadFileContent(file, mapping.getFileId());
        return mapping;
    }

    private void loadFileContent(File file, UUID fileId) throws IOException {
        fileContents.put(fileId, Files.readAllLines(file.toPath()));
    }

    public void discardFile(@NotNull FileMapping mapping) {
        fileContents.remove(mapping.getFileId());
    }

    public List<String> getFileContent(@NotNull FileMapping mapping) {
        List<String> content = fileContents.get(mapping.getFileId());
        if (content == null) {
            throw new NoSuchElementException("no such element: " + mapping);
        }
        return Collections.unmodifiableList(content);
    }

    public void reloadFileContents(@NotNull FileMapping mapping) throws IOException {
        if (mapping.isReloadable() && mapping instanceof FilesystemBackedFileMapping) {
            loadFileContent(((FilesystemBackedFileMapping) mapping).getFile(), mapping.getFileId());
        } else {
            throw new IllegalArgumentException("file cannot be reloaded!");
        }
    }

    public void updateFileContent(@NotNull FileMapping mapping, @NotNull List<String> lines) {
        fileContents.put(mapping.getFileId(), lines);
    }

}
