package ch.awae.cnc.fx;

import ch.awae.cnc.file.FileMapping;
import ch.awae.cnc.file.FileRepository;
import ch.awae.cnc.fx.modal.ErrorReportService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Arrays;

@Controller
public class FileViewController {

    public TextArea textArea;
    public Button reloadButton;

    private FileMapping currentFile;

    private final RootController rootController;
    private final FileRepository fileRepository;
    private final ErrorReportService errorReportService;

    @Autowired
    public FileViewController(
            RootController rootController,
            FileRepository fileRepository,
            ErrorReportService errorReportService) {
        this.rootController = rootController;
        this.fileRepository = fileRepository;
        this.errorReportService = errorReportService;
    }

    @FXML
    public void onBack(ActionEvent actionEvent) {
        rootController.switchTo(RootController.Tab.FILE_LIST);
    }

    public void loadFile(FileMapping file) {
        currentFile = file;
        textArea.setText(fileRepository
                .getFileContent(file)
                .stream()
                .reduce((a, b) -> a + "\n" + b)
                .orElse(null));
        reloadButton.setVisible(file.isReloadable());
    }

    @FXML
    public void onReload(ActionEvent actionEvent) {
        try {
            fileRepository.reloadFileContents(currentFile);
            loadFile(currentFile);
        } catch (IOException e) {
            errorReportService.report(e);
        }
    }

    @FXML
    public void onSave(ActionEvent actionEvent) {
        fileRepository.updateFileContent(currentFile, Arrays.asList(textArea.getText().split("\n")));
    }
}
