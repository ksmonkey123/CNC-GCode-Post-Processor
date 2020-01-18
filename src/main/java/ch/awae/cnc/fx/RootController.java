package ch.awae.cnc.fx;

import javafx.scene.control.TabPane;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.NotNull;

@Controller
public class RootController {

    public TabPane tabPane;
    public javafx.scene.control.Tab fileListTab;
    public javafx.scene.control.Tab fileViewTab;
    public javafx.scene.control.Tab selectOptionsTab;

    public void switchTo(RootController.Tab tab) {
        tabPane.getSelectionModel().select(getTab(tab));
    }

    private javafx.scene.control.Tab getTab(Tab tab) {
        switch (tab) {
            case FILE_LIST: return fileListTab;
            case FILE_VIEW: return fileViewTab;
            case OPTION_SELECTION: return selectOptionsTab;
            default: throw new AssertionError("unreachable");
        }
    }

    public enum Tab {
        FILE_LIST, FILE_VIEW, OPTION_SELECTION
    }

}
