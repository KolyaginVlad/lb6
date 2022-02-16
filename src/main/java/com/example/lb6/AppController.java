package com.example.lb6;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.io.*;
import java.util.List;
import java.util.Random;

public class AppController {
    static Random random = new Random();


    @FXML
    private ComboBox<String> choose;

    @FXML
    private Button show;
    
    @FXML
    private Label error1;

    @FXML
    private Label error11;

    @FXML
    private Label out;

    @FXML
    private Spinner<Integer> start;

    @FXML
    private Spinner<Integer> stop;

    @FXML
    private Button btn1;

    @FXML
    private Button btn2;

    @FXML
    private Button btnInfo;

    @FXML
    private TableView<List<String>> table1;

    @FXML
    private TableColumn<InfoAboutSort, Long> countCompare;

    @FXML
    private TableView<InfoAboutSort> infoTable;

    @FXML
    private TableColumn<InfoAboutSort, String> nameSorting;

    @FXML
    private TableColumn<InfoAboutSort, Long> read;

    @FXML
    private TableView<List<String>> table2;

    @FXML
    private Spinner<Integer> spinnerLength;

    @FXML
    private TableColumn<InfoAboutSort, Long> time;

    @FXML
    private TableColumn<InfoAboutSort, Long> write;

    @FXML
    void initialize() {
        error1.setVisible(false);
        error11.setVisible(false);

        choose.setItems( FXCollections.observableArrayList("A", "B", "C", "A1", "B1", "C1", "D1", "E1"));

        spinnerLength.setEditable(true);

        stop.setEditable(true);
        start.setEditable(true);

        nameSorting.setCellValueFactory(new PropertyValueFactory<InfoAboutSort, String>("nameSorting"));
        countCompare.setCellValueFactory(new PropertyValueFactory<InfoAboutSort, Long>("countCompare"));
        read.setCellValueFactory(new PropertyValueFactory<InfoAboutSort, Long>("read"));
        write.setCellValueFactory(new PropertyValueFactory<InfoAboutSort, Long>("write"));
        time.setCellValueFactory(new PropertyValueFactory<InfoAboutSort, Long>("time"));

        btn1.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            generateFileForDemo();
            fillTable(table1, SolutionWithInfo::simple1);
        });

        btn2.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            generateFileForDemo();
            fillTable(table2, SolutionWithInfo::simple2);
        });

        btnInfo.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (!isDigit(spinnerLength.getValue().toString())){
                error1.setVisible(true);
                return;
            }
            error1.setVisible(false);
            generateFile(spinnerLength.getValue());
            infoTable.getItems().clear();
            ObservableList<InfoAboutSort> rows = FXCollections.observableArrayList();

            long[] simple2 = Solution.simple2(spinnerLength.getValue());
            long[] simple1 = Solution.simple1(spinnerLength.getValue());

            rows.add(InfoAboutSort.create("Двухфазная", simple2));
            rows.add(InfoAboutSort.create("Однофазная", simple1));

            infoTable.setItems(rows);
        });
        
        show.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            out.setText("");
            try {
                if (!isDigit(stop.getValue().toString()) || !isDigit(start.getValue().toString())) {
                    error11.setText("Введите целые положительные числа");
                    error11.setVisible(true);
                    return;
                }
            }catch (NumberFormatException ee){
                error11.setText("Введите целые положительные числа");
                error11.setVisible(true);
            }
            if (start.getValue()==0|| stop.getValue()==0){
                error11.setText("Числа должны быть больше 0");
                error11.setVisible(true);
                return;
            }
            if (start.getValue()>= stop.getValue()){
                error11.setText("Первое число должно быть меньше второго");
                error11.setVisible(true);
                return;
            }
            error11.setVisible(false);
            switch (choose.getValue()){
                case "A":
                    out.setText(readFile("a.txt", start.getValue(), stop.getValue()));
                    break;
                case "B":
                    out.setText(readFile("b.txt", start.getValue(), stop.getValue()));
                    break;
                case "C":
                    out.setText(readFile("c.txt", start.getValue(), stop.getValue()));
                    break;
                case "A1":
                    out.setText(readFile("a1.txt", start.getValue(), stop.getValue()));
                    break;
                case "B1":
                    out.setText(readFile("b1.txt", start.getValue(), stop.getValue()));
                    break;
                case "C1":
                    out.setText(readFile("c1.txt", start.getValue(), stop.getValue()));
                    break;
                case "D1":
                    out.setText(readFile("d1.txt", start.getValue(), stop.getValue()));
                    break;
                case "E1":
                    out.setText(readFile("e1.txt", start.getValue(), stop.getValue()));
                    break;
            }
        });
    }

    private String readFile(String name, int start, int stop){
        try {
            InputStream reader = new FileInputStream(name);
            for (int i = 0; i < start; i++) {
                getNumber(reader);
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < stop - start; i++) {
                int a=  getNumber(reader);
                if (a!= -1)
                stringBuilder.append(a+", ");
            }
            if (stringBuilder.length()>0){
                stringBuilder.replace(stringBuilder.length()-2, stringBuilder.length(), "");
            }
            return stringBuilder.toString();
        }catch (Exception e){return "";}
    }

    private void fillTable(TableView<List<String>> table, SortMethod method) {
        table.getColumns().clear();

        List<List<String>> allSteps = method.sort();
        ObservableList<List<String>> data = FXCollections.observableArrayList();
        data.addAll(allSteps);

        for (int i = 0; i < allSteps.get(0).size(); i++) {
            TableColumn tc = new TableColumn<>(i == 0 ? "" : (i - 1) + "");
            tc.setSortable(false);
            int number = i;
            tc.setCellValueFactory((Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>) arg ->
                    new SimpleStringProperty(arg.getValue().get(number)));
            table.getColumns().add(tc);
        }
        table.setItems(data);
    }

    private void generateFileForDemo() {
        try (FileWriter writer = new FileWriter("a2.txt")) {
            for (int i = 0; i < 15; i++) {
                writer.write(random.nextInt(10000) + " ");
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateFile(long count) {
        try (FileWriter writer = new FileWriter("tmp.txt")) {
            for (int i = 0; i < count; i++) {
                writer.write(random.nextInt(10000) + " ");
            }
            writer.flush();
            cloneFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void cloneFile() throws IOException {
        PrintWriter writerToA = new PrintWriter(new FileWriter("a.txt"));
        PrintWriter writerToA1 = new PrintWriter(new FileWriter("a1.txt"));
        InputStream reader = new FileInputStream("tmp.txt");
        int num = getNumber(reader);
        while (num != -1) {
            writerToA.write( num +" ");
            writerToA1.write( num +" ");
            num = getNumber(reader);
        }
        writerToA.flush();
        writerToA.close();
        writerToA1.flush();
        writerToA1.close();
        reader.close();
    }

    private int getNumber(InputStream reader) throws IOException {
        int character;
        StringBuilder currentElem = new StringBuilder();
        while (reader.available() != 0) {
            character = reader.read();
            if (character != 32) {
                currentElem.append((char) character);
            } else {
                return Integer.parseInt(currentElem.toString());
            }
        }
        return -1;
    }
    private boolean isDigit(String str){
        for (int i = 0; i < str.length(); i++) {
            if(!Character.isDigit(str.charAt(i))) return false;
        }
        return true;
    }
}
