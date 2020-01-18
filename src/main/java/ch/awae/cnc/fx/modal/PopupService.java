package ch.awae.cnc.fx.modal;

public interface PopupService {

    void info(String content);

    void warn(String content);

    void error(String content);

    boolean confirm(String content);
}
