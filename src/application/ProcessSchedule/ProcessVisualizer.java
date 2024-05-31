package application.ProcessSchedule;

import IntervalSet.IntervalSet;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProcessVisualizer extends Application {
    private static ProcessIntervalSet<Process> processProcessIntervalSet;
    private static boolean launched = false;

    public static void setProcessIntervalSet(ProcessIntervalSet<Process> processIntervalSet) {
        processProcessIntervalSet = processIntervalSet;
    }

    public static boolean isLaunched() {
        return launched;
    }

    @Override
    public void start(Stage primaryStage) {
        launched = true;
        refreshUI(primaryStage);
    }

    public static void refresh() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            refreshUI(stage);
        });
    }

    private static void refreshUI(Stage stage) {
        ListView<String> listView = new ListView<>();
        listView.getItems().add("ID\tName\truntime interval\n");
        for (Process process : processProcessIntervalSet.labels()) {
            StringBuilder processInfo = new StringBuilder();
            processInfo.append(process.getID()+"\t").append(process.getName()+"\t").append(": ");
            IntervalSet<Integer> intervals = processProcessIntervalSet.intervals(process);
            for (int i = 0; i < intervals.labels().size(); i++) {
                processInfo.append("[").append(intervals.start(i)).append(", ").append(intervals.end(i)).append("] ");
            }
            listView.getItems().add(processInfo.toString());
        }

        VBox vbox = new VBox(listView);
        Scene scene = new Scene(vbox, 400, 400);

        stage.setTitle("Process Visualizer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
