package com.example.almanac.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("recipe-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

//    @Override
//    public void start(Stage stage) {
//
//        StackPane root = new StackPane();
//        Scene s = new Scene(root, 400, 400, Color.BLACK);
//
//        final Canvas canvas = new Canvas(300, 300);
//        GraphicsContext gc = canvas.getGraphicsContext2D();
//
//        gc.setFill(Color.BLUE);
//        gc.fillRect(10, 10, 300, 300);
//
//        ImageView image = new ImageView("https://cdn0.iconfinder.com/data/icons/toys/256/teddy_bear_toy_6.png");
//
//        // Listener for MouseClick
//        image.setOnMouseClicked(e -> {
//            System.out.println("Hi");
//        });
//
//        root.getChildren().addAll(canvas, image);
//
//        stage.setScene(s);
//        stage.show();
//    }

    public static void main(String[] args) {
        launch();
    }
}