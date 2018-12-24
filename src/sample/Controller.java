package sample;

import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controller {

    public Button open_file_button, parse_data_button, create_chart_button, previous, next;
    public TextField file_path_field;
    public TextArea data_area, sValues;
    public LineChart chart_for_data;
    public Label line_ids;

    private ArrayList<LineModel> models = new ArrayList<>();
    private ArrayList<LineModel> currentModels = new ArrayList<>();
    private DataBaseConnection dataBaseConnection;

    private File mFile;

    private int ids = 1;

    private final FileChooser fileChooser = new FileChooser();

    public void openFile(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        dataBaseConnection = new DataBaseConnection();
        mFile = fileChooser.showOpenDialog(Main.getStage());
        file_path_field.setText(mFile.getName());
    }

    public void parseData(ActionEvent actionEvent) {
        try {
            int id = dataBaseConnection.checkFileName(mFile.getName());
            if (id == 0) {
                id = dataBaseConnection.pasteFileName(mFile.getName());
                data_area.setText(parse(mFile, id, 0));
            } else {
                data_area.setText(parse(mFile, id, 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createChart(ActionEvent actionEvent) {

        chart_for_data.getData().clear();
        currentModels.clear();

        for (LineModel model : models) {
            if (model.getLineId() == ids) {
                currentModels.add(model);
            }
        }

        XYChart.Series series_points = new XYChart.Series();
        series_points.setName("Точки");

        XYChart.Series series_aproximacia = new XYChart.Series();
        series_aproximacia.setName("Апроксимация");

        String sVal = "";

        double[] values = new Approximation(currentModels, currentModels.size(), 2).getValues();

        for (int i = 0; i < currentModels.size(); i++) {
            if (currentModels.get(i).getLineId() == ids) {
                series_points.getData().add(new XYChart.Data(currentModels.get(i).getSpead(), currentModels.get(i).getHeadvay()));
                series_aproximacia.getData().add(new XYChart.Data(currentModels.get(i).getSpead(), values[i]));
                sVal += values[i] + "\n";
            }
        }

        chart_for_data.setCreateSymbols(true);
        chart_for_data.getData().addAll(series_points, series_aproximacia);
        sValues.setText(sVal);
    }

    private String parse(File file, int id, int status) {
        try {
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            if (status == 0) {
                String line = reader.readLine();
                while (line != null) {
                    if (line.length() > 2) {
                        String[] str = line.split("\\s{1,9}");
                        saveInfo(str, id);
                    }
                    line = reader.readLine();
                }
            }
            return getLines(id);
        } catch (IOException e) {
            return "";
        }
    }

    private String getLines(int id) {
        String s = "LANE  -  Speed  -  Headway \n";
        try {
            models = dataBaseConnection.getLine(id);
            for (LineModel model : models) {
                s += model.getLineId() + " - " + model.getSpead() + " - " + model.getHeadvay() + "\n";
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return s;
    }

    private void saveInfo(String[] str, int id) {
        LineModel lineModel = new LineModel();
        lineModel.setSpead(Float.valueOf(str[4].replace(',', '.')));
        lineModel.setHeadvay(Float.valueOf(str[14].replace(',', '.')));
        lineModel.setFilename(id);
        switch (str[1]) {
            case "LANE_01": {
                lineModel.setLineId(1);
                break;
            }
            case "LANE_02": {
                lineModel.setLineId(2);
                break;
            }
            case "LANE_03": {
                lineModel.setLineId(3);
                break;
            }
            case "LANE_04": {
                lineModel.setLineId(4);
                break;
            }
            case "LANE_05": {
                lineModel.setLineId(5);
                break;
            }
            case "LANE_06": {
                lineModel.setLineId(6);
                break;
            }
            case "LANE_07": {
                lineModel.setLineId(7);
                break;
            }
            case "LANE_08": {
                lineModel.setLineId(8);
                break;
            }
            case "LANE_09": {
                lineModel.setLineId(9);
                break;
            }
            case "LANE_10": {
                lineModel.setLineId(10);
                break;
            }
        }
        try {
            dataBaseConnection.pasteLines(lineModel);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void previosAction(ActionEvent actionEvent) {
        if (ids == 1) {
            ids *= 10;
        } else {
            ids--;
        }
        line_ids.setText(String.valueOf(ids));
    }

    public void nextAction(ActionEvent actionEvent) {
        if (ids == 10) {
            ids /= 10;
        } else {
            ids++;
        }
        line_ids.setText(String.valueOf(ids));
    }
}