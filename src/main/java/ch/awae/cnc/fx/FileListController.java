package ch.awae.cnc.fx;

import ch.awae.cnc.file.FileMapping;
import ch.awae.cnc.file.FileRepository;
import ch.awae.cnc.fx.modal.ErrorReportService;
import ch.awae.cnc.fx.modal.FileLocationService;
import ch.awae.cnc.fx.modal.PopupService;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.swing.event.DocumentEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class FileListController implements FxController {

    public ListView<FileMapping> fileList;
    public Button nextButton;

    private final ObservableList<FileMapping> files = FXCollections.observableArrayList();

    private final RootController rootController;
    private final FileViewController fileViewController;
    private final FileLocationService fileLocationService;
    private final ErrorReportService errorReportService;
    private final FileRepository fileRepository;

    @Autowired
    public FileListController(
            RootController rootController,
            FileViewController fileViewController,
            FileLocationService fileLocationService,
            ErrorReportService errorReportService,
            FileRepository fileRepository) {
        this.rootController = rootController;
        this.fileViewController = fileViewController;
        this.fileLocationService = fileLocationService;
        this.errorReportService = errorReportService;
        this.fileRepository = fileRepository;
    }

    @FXML
    public void initialize() {
        nextButton.setDisable(files.isEmpty());
        fileList.setItems(files);
        fileList.setCellFactory(list -> new FileListCell());
        files.addListener((ListChangeListener<FileMapping>) change -> nextButton.setDisable(change.getList().isEmpty()));
    }

    @FXML
    public void onAddFile(ActionEvent actionEvent) {
        fileLocationService
                .openFile(null)
                .flatMap(this::loadFile)
                .ifPresent(files::add);
    }

    private Optional<FileMapping> loadFile(File file) {
        try {
            return Optional.of(fileRepository.loadFile(file));
        } catch (IOException e) {
            errorReportService.report(e);
            return Optional.empty();
        }
    }

    private void onShowFile(FileMapping file) {
        rootController.switchTo(RootController.Tab.FILE_VIEW);
        fileViewController.loadFile(file);
    }

    private void onRemoveFile(FileMapping file) {
        files.remove(file);
        fileRepository.discardFile(file);
    }

    @FXML
    public void onNext(ActionEvent actionEvent) {
        rootController.switchTo(RootController.Tab.OPTION_SELECTION);
    }

    @FXML
    public void onCreateFile(ActionEvent actionEvent) {
        files.add(fileRepository.createVirtualFile());
    }

    List<FileMapping> getFileList() {
        return new ArrayList<>(files);
    }

    private class FileListCell extends ListCell<FileMapping> {
        BorderPane box = new BorderPane();
        Label label = new Label("(empty)");
        Button button = new Button("X");

        FileListCell() {
            super();
            prefWidthProperty().bind(fileList.widthProperty().subtract(2));
            setMaxWidth(Control.USE_PREF_SIZE);
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.getChildren().add(label);
            box.setCenter(hbox);
            box.setRight(button);
            label.setTextOverrun(OverrunStyle.LEADING_ELLIPSIS);
            button.setOnAction(event -> onRemoveFile(getItem()));
            label.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                    onShowFile(getItem());
                }
            });
        }

        @Override
        protected void updateItem(FileMapping item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                label.setText(item.getPath());
                setGraphic(box);
            }
        }

    }

}
