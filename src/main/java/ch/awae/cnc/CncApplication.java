package ch.awae.cnc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@EnableConfigurationProperties
@SpringBootApplication
public class CncApplication extends Application {

    private ConfigurableApplicationContext springContext;
    private Parent rootNode;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        SpringApplication app = new SpringApplication(CncApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        springContext = app.run();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Root.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();
    }

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(rootNode));
        stage.show();
        stage.setTitle("CNC G-Code Post-Processor");
    }

    @Override
    public void stop() {
        springContext.close();
    }

}
