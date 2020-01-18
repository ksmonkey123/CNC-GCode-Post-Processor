package ch.awae.cnc.logic;

import ch.awae.cnc.file.FileMapping;
import ch.awae.cnc.file.FileRepository;
import ch.awae.cnc.fx.RootController;
import ch.awae.cnc.fx.modal.ErrorReportService;
import ch.awae.cnc.fx.modal.FileLocationService;
import ch.awae.cnc.fx.modal.PopupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProcessingService {

    private final FileRepository fileRepository;
    private final FileLocationService fileLocationService;
    private final PopupService popupService;
    private final RootController rootController;
    private final ErrorReportService errorReportService;
    private final ThreadLocal<ProcessingParameters> params = new ThreadLocal<>();

    @Autowired
    public ProcessingService(
            FileRepository fileRepository,
            FileLocationService fileLocationService,
            PopupService popupService,
            RootController rootController,
            ErrorReportService errorReportService) {
        this.fileRepository = fileRepository;
        this.fileLocationService = fileLocationService;
        this.popupService = popupService;
        this.rootController = rootController;
        this.errorReportService = errorReportService;
    }

    public void process(ProcessingParameters params, List<FileMapping> fileList) {
        this.params.set(params);
        Stream<String> body = fileList.stream()
                .map(fileRepository::getFileContent)
                .flatMap(List::stream)
                .filter(line -> line.startsWith("G0"))
                .map(this::stripFeedrate)
                .filter(line -> line.length() > 3)
                .map(this::addCustomFeedrate);

        Stream<String> fullStream = Stream.concat(Stream.concat(createHeader(), body), createFooter());

        Optional<File> file = fileLocationService.prompt("output", ".gcode")
                .map(File::new);

        if (file.isPresent()) {
            try {
                Files.write(file.get().toPath(), fullStream.collect(Collectors.toList()));
                popupService.info("Processing Completed");
                rootController.switchTo(RootController.Tab.FILE_LIST);
            } catch (IOException e) {
                errorReportService.report(e);
            }
        } else {
            popupService.warn("Processing Aborted");
        }
    }

    private Stream<String> createHeader() {
        List<String> lines = new ArrayList<>();

        if (params.get().getHomingMode() == HomingMode.HOME_ALL) {
            lines.add("G28");
            if(params.get().isDoBedLevelling()) {
                lines.add("G29");
                lines.add("M400");
                lines.add("M500");
            }
            lines.add("G00 X" + params.get().getZeroX() + " Y" + params.get().getZeroY());
            lines.add("G92 X0 Y0");
        } else {
            lines.add("G28 Z");
        }
        lines.add("M400");
        lines.add("M420 S");
        lines.add("M300 S440 P500");
        lines.add("G4 S20");
        lines.add("M300 S660 P500");
        lines.add("M3");
        lines.add("G4 S5");

        return lines.stream();
    }

    private Stream<String> createFooter() {
        List<String> lines = new ArrayList<>();

        lines.add("M400");
        lines.add("G00 Z40");
        lines.add("M5");
        lines.add("G4 S2");
        lines.add("M300 S880 P250");

        return lines.stream();
    }

    private String addCustomFeedrate(String line) {
        if (line.startsWith("G00")) {
            return line + " F" + params.get().getTravelSpeed();
        } else {
            return line + " F" + params.get().getWorkSpeed();
        }
    }

    private String stripFeedrate(String line) {
        return Stream.of(line.split(" "))
                .filter(token -> !token.startsWith("F"))
                .reduce((a, b) -> a + " " + b)
                .orElse("");
    }
}
