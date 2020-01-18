package ch.awae.cnc.fx.modal;

import ch.awae.cnc.LoggedComponent;
import javafx.stage.FileChooser;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

@Service
public class FileLocationService extends LoggedComponent {

    private final static FileChooser.ExtensionFilter gcodeFilter = new FileChooser.ExtensionFilter("G-Code File", "*.gcode");

    public Optional<String> prompt(String name, String suffix) {
        LOG.info("prompting for save location");
        Optional<File> file = askUser(name + suffix);
        Optional<String> string = file.map(f -> {
            String ff = f.getAbsolutePath();
            if (suffix == null || ff.endsWith(suffix))
                return ff;
            return ff + suffix;
        });
        if (string.isPresent()) {
            LOG.info("save location: " + string.get());
        } else {
            LOG.info("save file prompt aborted");
        }
        return string;
    }

    private Optional<File> askUser(String name) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        fileChooser.setInitialFileName(name);
        return Optional.ofNullable(fileChooser.showSaveDialog(null));
    }

    public Optional<File> openFile(String name) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.setInitialFileName(name);
        fileChooser.getExtensionFilters().add(gcodeFilter);
        fileChooser.setSelectedExtensionFilter(gcodeFilter);
        return Optional.ofNullable(fileChooser.showOpenDialog(null));
    }

}
