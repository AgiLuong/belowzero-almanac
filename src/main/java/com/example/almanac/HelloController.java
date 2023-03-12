package com.example.almanac;

import com.example.almanac.crafting.CommandLineAlmanac;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    private CommandLineAlmanac cla = new CommandLineAlmanac();
    @FXML
    private TextField input;
    @FXML
    private Label display;
    @FXML
    public void myPrint(ActionEvent ae) {
        String recipe = cla.getRecipeAsString(input.getText());
        display.setText(recipe);
        System.out.println("done!");
    }
}