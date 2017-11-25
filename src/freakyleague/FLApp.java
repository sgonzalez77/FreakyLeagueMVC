/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freakyleague;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author sergi
 */
public class FLApp extends Application {
    public static Stage theStage = null;
    public static Scene scene1 = null;
    public static Scene scene2 = null;
    
    @Override
    public void start(Stage stage) throws Exception {     
        Parent root1 = FXMLLoader.load(getClass().getResource("FLLogin.fxml"));
        //Parent root2 = FXMLLoader.load(getClass().getResource("FLView.fxml"));
        
        theStage = stage;
        
        scene1 = new Scene(root1);
        //scene2 = new Scene(root2);
        
        theStage.setScene(scene1);
        theStage.show();
        
        theStage.setOnCloseRequest(e -> close());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static void close() {
        System.out.println("closing..."); 
        //db.close();
        //can I do it here? MVC rules...
        freakyleague.FLModel.deleteDBGarbage();
        Platform.exit();
        System.exit(0);
    }
    
 
}
    
