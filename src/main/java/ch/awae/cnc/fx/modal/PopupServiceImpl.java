package ch.awae.cnc.fx.modal;


import ch.awae.cnc.LoggedComponent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@Validated
@Service
@ConfigurationProperties("popup")
class PopupServiceImpl extends LoggedComponent implements PopupService {

    private @NotEmpty String info;
    private @NotEmpty String warning;
    private @NotEmpty String error;
    private @NotEmpty String confirm;

    public void info(String content) {
        LOG.info("showing information popup: " + content);
        alert(Alert.AlertType.INFORMATION, info, content);
    }

    public void warn(String content) {
        LOG.info("showing warning popup: " + content);
        alert(Alert.AlertType.WARNING, warning, content);
    }

    public void error(String content) {
        LOG.info("showing error popup: " + content);
        alert(Alert.AlertType.ERROR, error, content);
    }

    public boolean confirm(String content) {
        LOG.info("showing confirmation popup: " + content);
        return alert(Alert.AlertType.CONFIRMATION, confirm, content).filter(ButtonType.OK::equals).isPresent();
    }

    private Optional<ButtonType> alert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        Optional<ButtonType> button = alert.showAndWait();
        LOG.info("popup closed");
        return button;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getInfo() {
        return info;
    }

    public String getWarning() {
        return warning;
    }

    public String getError() {
        return error;
    }

    public String getConfirm() {
        return confirm;
    }
}
