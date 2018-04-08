package us.hgmtrebing.calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class App extends Application{


    @Override
    public void start (Stage primaryStage) {
        try {

            Parent root = FXMLLoader.load(App.class.getClassLoader().getResource("Gui.fxml"));
            Scene scene = new Scene (root);
            scene.getStylesheets().add (App.class.getClassLoader().getResource("Gui.css").toExternalForm());
            primaryStage.setScene (scene);
            primaryStage.setResizable(false); //calculator is designed to be viewed at a specific size
            primaryStage.show();

            //FYI - This is needed to allow Keyboard event handlers to work, after removing focusTraversable from all Buttons in ...
            //... GuiController.java. I have no idea why focusTraversable and requestFocus() even effects keyboard event handlers (but not other event handlers) ...
            //... And I have no idea why this needs to be in start() method. I tried to invoke requestFocus() in GuiController.initialize() ...
            //... And nothing happened.
            root.requestFocus();

        } catch (Exception e) {
            System.out.println(e.getClass() + e.getMessage() + " -- FAILURE");
            for (StackTraceElement s : e.getStackTrace()) {
                System.out.println (s.toString());
            }
        }
    }

    public static void main( String[] args ) {

        launch(args);
        /*
        Calculator c = new Calculator();
        Scanner s = new Scanner(System.in);
        String userInput = "";

        while (true) {
            userInput = s.nextLine();
            try {
                System.out.println (c.evaluate(userInput));
            } catch (Exception e) {
                System.out.println (e.getMessage());
            }
        }
        */
    }
}

