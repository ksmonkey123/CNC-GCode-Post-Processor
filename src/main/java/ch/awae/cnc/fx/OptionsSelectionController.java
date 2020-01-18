package ch.awae.cnc.fx;

import ch.awae.cnc.fx.modal.ErrorReportService;
import ch.awae.cnc.logic.HomingMode;
import ch.awae.cnc.logic.ProcessingParameters;
import ch.awae.cnc.logic.ProcessingService;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class OptionsSelectionController implements FxController {

    public ComboBox<HomingMode> homingMode;
    public CheckBox updateBedMesh;
    public TextField workSpeed;
    public TextField travelSpeed;
    public TextField zeroX;
    public TextField zeroY;
    public VBox homingExtras;

    private final RootController rootController;
    private final ProcessingService processingService;
    private final FileListController fileListController;
    private final ErrorReportService errorReportService;

    @Autowired
    public OptionsSelectionController(
            RootController rootController,
            ProcessingService processingService,
            FileListController fileListController,
            ErrorReportService errorReportService) {
        this.rootController = rootController;
        this.processingService = processingService;
        this.fileListController = fileListController;
        this.errorReportService = errorReportService;
    }

    @Override
    public void initialize() {
        homingMode.setValue(HomingMode.HOME_ALL);
        homingMode.valueProperty().addListener((observable, oldValue, newValue) -> homingExtras.setDisable(newValue != HomingMode.HOME_ALL));

        workSpeed.textProperty().addListener(createNumericEnforcer(workSpeed));
        travelSpeed.textProperty().addListener(createNumericEnforcer(travelSpeed));
        zeroX.textProperty().addListener(createNumericEnforcer(zeroX));
        zeroY.textProperty().addListener(createNumericEnforcer(zeroY));
    }

    private ChangeListener<String> createNumericEnforcer(TextField tf) {
        return (observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tf.setText(oldValue);
            }
        };
    }

    @FXML
    public void onBack(ActionEvent actionEvent) {
        rootController.switchTo(RootController.Tab.FILE_LIST);
    }


    public void onProcess(ActionEvent actionEvent) {
        try {
            ProcessingParameters params = collectParameters();
            processingService.process(params, fileListController.getFileList());
            rootController.switchTo(RootController.Tab.FILE_LIST);
        } catch (Exception e) {
            errorReportService.report(e);
        }
    }

    private ProcessingParameters collectParameters() {
        ProcessingParameters params = new ProcessingParameters();
        params.setHomingMode(homingMode.getValue());
        if (!homingExtras.isDisable()) {
            params.setDoBedLevelling(updateBedMesh.isSelected());
            params.setZeroX(Integer.parseInt(zeroX.getText()));
            params.setZeroY(Integer.parseInt(zeroY.getText()));
        }
        params.setWorkSpeed(Integer.parseInt(workSpeed.getText()));
        params.setTravelSpeed(Integer.parseInt(travelSpeed.getText()));
        return params;
    }
}
