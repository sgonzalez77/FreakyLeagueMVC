package freakyleague;

import static freakyleague.FLApp.scene2;
import static freakyleague.FLApp.theStage;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FLControllerLogin {

    @FXML
    private Button btnLogin;

    @FXML
    private TextField txtUser;

    @FXML
    private TextField txtPassword;
    
    @FXML
    private Label lblError;
 
    @FXML
    private void handleBtnLoginAction(ActionEvent event) throws IOException {
        Parent root2 = null;
        //handleBtn("Ernie", "Flip", 13, 6);
        if (freakyleague.FLModel.validateUser(txtUser.getText(),
                txtPassword.getText())) {
            
            freakyleague.FLModel.setUser(txtUser.getText());
            
            lblError.setText("");
            /*
            theStage.setOnShowing((WindowEvent event1) -> {
                freakyleague.FLController.setMenu();
            });
            */
            root2 = FXMLLoader.load(getClass().getResource("FLView.fxml"));
            scene2 = new Scene(root2);
            theStage.setScene(scene2);
            theStage.show();
            
        } else {
            lblError.setText("Wrog user/password.");
        } 
    }
    
    @FXML
    void initialize() {
        assert btnLogin != null : "fx:id=\"btnLogin\" was not injected: check your FXML file 'FLLogin.fxml'.";
        assert txtUser != null : "fx:id=\"txtUser\" was not injected: check your FXML file 'FLLogin.fxml'.";
        assert txtPassword != null : "fx:id=\"txtPassword\" was not injected: check your FXML file 'FLLogin.fxml'.";

    }
}
