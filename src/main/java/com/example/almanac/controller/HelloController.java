package com.example.almanac.controller;

import com.example.almanac.crafting.CommandLineAlmanac;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;


public class HelloController {
    private CommandLineAlmanac cla = new CommandLineAlmanac();
    @FXML
    private TextField input;
    @FXML
    private Label display;
    @FXML
    private Canvas canvas;
    @FXML
    public void myPrint(ActionEvent ae) {
        /*
        String recipe = cla.getRecipeAsString(input.getText());
        display.setText(recipe);
        System.out.println("done!");
        */
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        Image image = new Image("com/example/almanac/items/Advanced_Wiring_Kit.jpeg");
        gc.drawImage(image,
                  canvas.getWidth() / 2 - (float) image.getWidth() / 2,
                  canvas.getHeight() / 2 - (float) image.getHeight() / 2);


        System.out.println(image.getHeight() + "and" + image.getWidth());
    }
}