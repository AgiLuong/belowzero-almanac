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
    public void myPrint(ActionEvent ae) {
        System.out.println(input.getText());
    }
}