package com.munien;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.xml.soap.Text;
import java.util.ArrayList;

public class Controller {
    @FXML private VBox addVBox;
    @FXML private VBox addToColumn;
    @FXML private Button addButton;
    @FXML private Label grade;
    @FXML private Label weight;

    private Alert error = new Alert(Alert.AlertType.ERROR);

    private int count = 1;
    private double dweighting;
    private double currentWeight;
    private double dmarks;
    private String number = "";
    private int dnumber;
    private double dgrade = 0;

    ArrayList<TextField> arrayWeighting = new ArrayList<>();
    ArrayList<TextField> arrayMarks = new ArrayList<>();
    ArrayList<Double> arrayGrade = new ArrayList<>();

    public void initialize() {
        HBox hbox = new HBox();
        Label marks = new Label("Marks");
        Label weighting = new Label("Weighting");
        hbox.getChildren().addAll(marks, weighting);
        hbox.setMargin(marks, new Insets(5, 0, 0, 19));
        hbox.setMargin(weighting, new Insets(5, 0, 0, 18));
        addToColumn.getChildren().add(hbox);
        weight.setText("0 / 100");
        addColumn();
    }

    public void calculate() {
        if(checkValues()) {
            calculateGrade();
            if(dgrade < 49)
                grade.setText("Fail - " + dgrade);
            else if (dgrade > 50 && dgrade < 64)
                grade.setText("Pass - " + dgrade);
            else if (dgrade > 65 && dgrade <74)
                grade.setText("Credit - " + dgrade);
            else if (dgrade > 75 && dgrade < 84)
                grade.setText("Distinction - " + dgrade);
            else if (dgrade > 85 && dgrade < 100)
                grade.setText("High Distinction - " + dgrade);
            arrayGrade.clear();
            dgrade = 0;
        }
    }

    public void calculateGrade() {
        // marks / total marks
        int count = 0;
        for (TextField t: arrayMarks
             ) {
            double result = Integer.parseInt(t.getText());
            double percentage = result / 100;
            double grade = percentage * Integer.parseInt(arrayWeighting.get(count).getText());
            arrayGrade.add(grade);
            count++;
        }
        arrayGrade.forEach((d) -> dgrade+=d);
    }

    public boolean checkValues() {
        for(TextField t: arrayWeighting) {
            int value = Integer.parseInt(t.getText());
            dweighting += value;
        }
        if(!checkMarks() || dweighting > 100 || (!checkMarks() && dweighting > 100)) {
            if(!checkMarks()) {
                error.setHeaderText("Marks not valid");
            } else if(!checkMarks() && dweighting > 100) {
                error.setHeaderText("Marks and Weighting not valid");
            } else {
                error.setHeaderText("Weighting not valid");
            }
            error.showAndWait();
            dweighting = 0;
            return false;
        } else {
            dweighting = 0;
            return true;
        }
    }

    public boolean checkMarks() {
        boolean result = true;
        for(TextField t: arrayMarks) {
            if(Integer.parseInt(t.getText()) > 100) {
                result = false;
            }
        }
        if(!result)
            return false;
        else
            return true;
    }

    public void addToWeight(String s) {
        // Get the current weight,
        // String build;
    }

    public void addColumn() {
        HBox hbox = new HBox();
        TextField marks = new TextField();
        TextField weighting = new TextField();
        Label label = new Label();
        label.setText("Course " + count);
        count++;
        weighting.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    System.out.println("Focused");
                    weighting.setOnKeyReleased(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                            if(event.getCode() == KeyCode.BACK_SPACE) {
                                System.out.println("Backspace Pressed");
                                number = number.substring(0, number.length() -1);
                                if(number.length() == 0) {
                                    weight.setText("0 / 100");
                                    dnumber = 0;
                                } else {
                                    weight.setText(number + " / 100");
                                    dnumber = Integer.parseInt(number);
                                    System.out.println(dnumber);
                                }
                            } else {
                                System.out.println(event.getText());
                                System.out.println(event.getCode());
                                number += event.getText();
                                if(dnumber > 100) {
                                    number = number.substring(0, number.length() - 1);
                                    weight.setText(number + " / 100");
                                    weighting.deletePreviousChar();
                                    error.setHeaderText("Weight is too large");
                                    error.showAndWait();
                                }
                            }
                        }
                    });
                } else {
                    System.out.println("Out of Focus");
                    if(!number.isEmpty()) {
                        dnumber += Integer.parseInt(number);
                    }
                    weight.setText(dnumber + " / 100");
                    System.out.println(dnumber);
                    number = "";
                }
            }
        });
        weighting.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                // Refractor all this eventually
//               if(event.getCode() == KeyCode.BACK_SPACE) {
//                   System.out.println("Backspace Pressed");
//                   number = number.substring(0, number.length() -1);
//                   if(number.length() == 0) {
//                       weight.setText("0 / 100");
//                   } else {
//                       weight.setText(number + " / 100");
//                   }
//               } else {
//                   System.out.println(event.getText());
//                   System.out.println(event.getCode());
//                   number += event.getText();
//                   if(number.length() > 3 || Integer.parseInt(number) > 100) {
//                       number = number.substring(0, number.length() -1);
//                       weight.setText(number + " / 100");
//                       weighting.deletePreviousChar();
//                       error.setHeaderText("Weight is too large");
//                       error.showAndWait();
//                   } else {
//                       weight.setText(number + " / 100");
//                   }
//               }
            }
        });
        arrayWeighting.add(weighting);
        arrayMarks.add(marks);
        marks.setPrefWidth(50);
        weighting.setPrefWidth(60);
        hbox.setMargin(marks, new Insets(15, 0, 0, 15));
        hbox.setMargin(weighting, new Insets(15, 0, 0, 5));
        hbox.setMargin(label, new Insets(15, 0, 0, 15));
        hbox.getChildren().addAll(marks, weighting, label);
        addToColumn.getChildren().add(hbox);
    }
}
