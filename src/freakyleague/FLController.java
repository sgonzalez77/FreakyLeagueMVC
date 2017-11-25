/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
THIS APP IS NOT OK. I destroy view controller model:
https://stackoverflow.com/questions/29639881/javafx-how-to-use-a-method-in-a-controller-from-another-controller
*/
package freakyleague;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author sergi
 */
public class FLController implements Initializable {
    @FXML
    private DatePicker dateTourna;
    
    @FXML //fx:id property set to btnM1D1, etc.
    private Button btnM1D1, btnM2D1, btnM1D2, btnM2D2, btnM1D3, btnM2D3,
            btnM1D4, btnM2D4, btnM1D5, btnM2D5, btnM1D6, btnM2D6; 

    @FXML
    private TextField txtId, txtM1D1Flip, txtM1D1Ernie, txtM2D1Monkey, txtM2D1Dog,
            txtM1D2Flip, txtM1D2Monkey, txtM2D2Ernie, txtM2D2Dog, txtM1D3Flip,
            txtM1D3Dog, txtM2D3Ernie, txtM2D3Monkey, txtM1D4Ernie, txtM1D4Flip,
            txtM2D4Dog, txtM2D4Monkey, txtM1D5Monkey, txtM1D5Flip, txtM2D5Dog,
            txtM2D5Ernie, txtM1D6Dog, txtM1D6Flip, txtM2D6Monkey, txtM2D6Ernie;
    
    //array with players and loop it!
    
    private List<TextField> textfields = new ArrayList<>();
    private List<Button> buttons = new ArrayList<>();
    
    @FXML
    private TableView tvLeague  = new TableView();
    
    @FXML
    private TableView tvLeagueGlobals  = new TableView();
    
    @FXML
    NumberAxis xAxis;
    
    @FXML
    CategoryAxis yAxis;
    
    @FXML
    BarChart<String, Number> bc;
    
    @FXML private MenuItem menuNew;

    @FXML
    private void closeApp(ActionEvent event) {
        freakyleague.FLApp.close();
    }
    
    @FXML
    private void newTournament(ActionEvent event) {
        ClassTournament t = freakyleague.FLModel.newTournament();
        //String[] myD = t.getDate().split("-");
        //String newD = myD[2] + "/" + myD[1] + "/" + myD[0];
        
        if (t != null) {
            dateTourna.setDisable(false);
            dateTourna.setValue(t.getDateLD());
            dateTourna.setDisable(true);
            resetData(0);
            txtId.setText(String.valueOf(t.getId()));
            unlockDay(btnM1D1, btnM2D1, txtM1D1Flip,  txtM1D1Ernie, txtM2D1Monkey, txtM2D1Dog);
        }
        
    }
    
    private void unlockDay(Button bt1, Button bt2, TextField txt1, TextField txt2, TextField txt3, TextField txt4) {
        //upgrade: only a param (list) and loop it!
        bt1.setDisable(false);
        bt2.setDisable(false);
        txt1.setDisable(false);
        txt2.setDisable(false);
        txt3.setDisable(false);
        txt4.setDisable(false);    
    }
    
    @FXML
    private void openTournament(ActionEvent event) throws SQLException {
        String strId = "";
        disableTextFields(true);
        disableButtons(true);
        List<ClassTournament> tournaments = freakyleague.FLModel.giveMeTournaments();
        List<String> choices = new ArrayList<>();
        String firstChoice = "";
        ClassTournament t = null;
        System.out.println(tournaments);
        if (tournaments != null) {
            firstChoice = tournaments.get(0).getDateStr() + "- id: " +
                        tournaments.get(0).getId();
            for (int i=1;i<tournaments.size();i++) {
                choices.add(
                        tournaments.get(i).getDateStr() + "- id: " +
                        tournaments.get(i).getId()
                );
            }

            ChoiceDialog<String> dialog = new ChoiceDialog<>(firstChoice, choices);
            dialog.setTitle("Load a tournament");
            dialog.setHeaderText("Select a date and press 'Accept'.");
            dialog.setContentText("Tournaments:");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String[] parts = result.get().split("- id: ");
                //parts[1] id,  parts[0] date
                System.out.println(parts[0]);
                t = new ClassTournament(parts[1], parts[0]);
                if (t != null)
                    loadData(t);
            }
        }
    }
    
    private void loadData(ClassTournament t) throws SQLException {
        ResultSet rs = null;
        List<ClassMatch> matches = null;
        int idTourn =t.getId();
        String myDate = t.getDateStr();
        
        txtId.setText(String.valueOf(idTourn));

        dateTourna.setDisable(false);
        dateTourna.setValue(t.getDateLD());
        dateTourna.setDisable(true);
        
        matches = freakyleague.FLModel.giveMeTournamentData(idTourn);
        
        resetData(1);

        //a possible upgrade to this app could be ask to the user
        //the number of player and build textfields dynamically
        for (int i=0;i<matches.size();i++) {
            
            switch (matches.get(i).getHome()) {           
                case "Flip":
                    switch (matches.get(i).getAway()) {
                        case "Monkey":
                            txtM1D2Flip.setText(String.valueOf(matches.get(i).getScoreHome()));
                            txtM1D2Monkey.setText(String.valueOf(matches.get(i).getScoreAway()));
                            break;
                        case "Dog":
                            txtM1D3Flip.setText(String.valueOf(matches.get(i).getScoreHome()));
                            txtM1D3Dog.setText(String.valueOf(matches.get(i).getScoreAway()));
                            break;
                        case "Ernie":
                            txtM1D1Flip.setText(String.valueOf(matches.get(i).getScoreHome()));
                            txtM1D1Ernie.setText(String.valueOf(matches.get(i).getScoreAway()));
                            break;
                        default:
                            System.out.println("wrong case flip");
                            break;
                    }
                    break;
                case "Monkey":
                    switch (matches.get(i).getAway()) {
                        case "Flip":
                            txtM1D5Monkey.setText(String.valueOf(matches.get(i).getScoreHome()));
                            txtM1D5Flip.setText(String.valueOf(matches.get(i).getScoreAway()));
                            break;
                        case "Dog":
                            txtM2D1Monkey.setText(String.valueOf(matches.get(i).getScoreHome()));
                            txtM2D1Dog.setText(String.valueOf(matches.get(i).getScoreAway()));
                            break;
                        case "Ernie":
                            txtM2D6Monkey.setText(String.valueOf(matches.get(i).getScoreHome()));
                            txtM2D6Ernie.setText(String.valueOf(matches.get(i).getScoreAway()));
                            break;
                        default:
                            System.out.println("wrong case monkey");
                            break;
                    }

                    break;
                case "Dog":
                    switch (matches.get(i).getAway()) {
                        case "Monkey":
                            txtM2D4Dog.setText(String.valueOf(matches.get(i).getScoreHome()));
                            txtM2D4Monkey.setText(String.valueOf(matches.get(i).getScoreAway()));
                            break;
                        case "Flip":
                            txtM1D6Dog.setText(String.valueOf(matches.get(i).getScoreHome()));
                            txtM1D6Flip.setText(String.valueOf(matches.get(i).getScoreAway()));
                            break;
                        case "Ernie":
                            txtM2D5Dog.setText(String.valueOf(matches.get(i).getScoreHome()));
                            txtM2D5Ernie.setText(String.valueOf(matches.get(i).getScoreAway()));
                            break;
                        default:
                            System.out.println("wrong case flip");
                            break;
                    }
                    break;
                case "Ernie":
                    switch (matches.get(i).getAway()) {
                        case "Monkey":
                            txtM2D3Ernie.setText(String.valueOf(matches.get(i).getScoreHome()));
                            txtM2D3Monkey.setText(String.valueOf(matches.get(i).getScoreAway()));
                            break;
                        case "Dog":
                            txtM2D2Ernie.setText(String.valueOf(matches.get(i).getScoreHome()));
                            txtM2D2Dog.setText(String.valueOf(matches.get(i).getScoreAway()));
                            break;
                        case "Flip":
                            txtM1D4Ernie.setText(String.valueOf(matches.get(i).getScoreHome()));
                            txtM1D4Flip.setText(String.valueOf(matches.get(i).getScoreAway()));
                            break;
                        default:
                            System.out.println("wrong case flip");
                            break;
                    }
                    break;
                default:
                    System.out.println("wrong switch");
                    break;

            }

        }    
        loadLeagueTable();
        loadLeagueTableGlobals();
    }
    
    private ClassLeagueResults giveMeLeagueData(String idPlayer, String idTor) {
        ClassLeagueResults dataPlayer = null;
         
        dataPlayer = new ClassLeagueResults(idPlayer,
                freakyleague.FLModel.giveMeData("LW", idPlayer, idTor),
                freakyleague.FLModel.giveMeData("LD", idPlayer, idTor),
                freakyleague.FLModel.giveMeData("LL", idPlayer, idTor),
                freakyleague.FLModel.giveMeData("GF",idPlayer, idTor),
                freakyleague.FLModel.giveMeData("GA",idPlayer, idTor));
        return dataPlayer;
    }
    
    private TableColumn createColumn(String name, int size, boolean sort) {
        TableColumn col = new TableColumn(name);
        col.setMinWidth(size);
        col.setCellValueFactory(
                new PropertyValueFactory<ClassLeagueResults, String>(name));
        if (sort)
            col.setSortType(TableColumn.SortType.DESCENDING);
        return col;
    }
    
    private void loadLeagueTable() {
       
        final ObservableList<ClassLeagueResults> data = FXCollections.observableArrayList(
            giveMeLeagueData("Flip", txtId.getText()),
            giveMeLeagueData("Ernie", txtId.getText()),
            giveMeLeagueData("Monkey", txtId.getText()),
            giveMeLeagueData("Dog", txtId.getText())
        );
        
        //delate previous columns if exist
        if (tvLeague.getColumns().size() > 0) {
            tvLeague.getColumns().remove(0, 8);
        }
            
        TableColumn colTeam = createColumn("Team", 102, false);   
        TableColumn colPoints = createColumn("Points", 75, true);
        TableColumn colPlayed = createColumn("Played", 65, false);
        TableColumn colWon = createColumn("Won", 65, false);
        TableColumn colDrawn = createColumn("Drawn", 65, false);
        TableColumn colLost = createColumn("Lost", 65, false);
        TableColumn colGF = createColumn("GF", 65, false);
        TableColumn colGA = createColumn("GA", 65, false);
        TableColumn colGD = createColumn("GD", 65, false);
        SortedList<ClassLeagueResults> sortedItems = new SortedList<>(data);
        tvLeague.setItems(sortedItems);
        
        sortedItems.comparatorProperty().bind(tvLeague.comparatorProperty());
    
        tvLeague.getColumns().addAll(colTeam, colPoints, colWon, colDrawn,
                                        colLost, colGF, colGA, colGD);
        //not possible sorting by 2 columns
        //https://stackoverflow.com/questions/37807414/sort-javafx-table-on-multiple-columns
        //sorting by this column
        tvLeague.getSortOrder().add(colPoints);
    }
    
    private void loadLeagueTableGlobals() {
       
        final ObservableList<ClassLeagueResults> data = FXCollections.observableArrayList(
            giveMeLeagueData("Flip", txtId.getText()),
            giveMeLeagueData("Ernie", txtId.getText()),
            giveMeLeagueData("Monkey", txtId.getText()),
            giveMeLeagueData("Dog", txtId.getText())
        );
        
        //delate previous columns if exist
        if (tvLeagueGlobals.getColumns().size() > 0) {
            tvLeagueGlobals.getColumns().remove(0, 8);
        }
            
        TableColumn colTeam = createColumn("Team", 102, false);   
        TableColumn colPoints = createColumn("Points", 75, true);
        TableColumn colPlayed = createColumn("Played", 65, false);
        TableColumn colWon = createColumn("Won", 65, false);
        TableColumn colDrawn = createColumn("Drawn", 65, false);
        TableColumn colLost = createColumn("Lost", 65, false);
        TableColumn colGF = createColumn("GF", 65, false);
        TableColumn colGA = createColumn("GA", 65, false);
        TableColumn colGD = createColumn("GD", 65, false);
        SortedList<ClassLeagueResults> sortedItems = new SortedList<>(data);
        tvLeagueGlobals.setItems(sortedItems);
        
        sortedItems.comparatorProperty().bind(tvLeagueGlobals.comparatorProperty());
    
        tvLeagueGlobals.getColumns().addAll(colTeam, colPoints, colWon, colDrawn,
                                        colLost, colGF, colGA, colGD);
        //not possible sorting by 2 columns
        //https://stackoverflow.com/questions/37807414/sort-javafx-table-on-multiple-columns
        //sorting by this column
        tvLeagueGlobals.getSortOrder().add(colPoints);
    }
    
    private void resetData(int begin) {
        for (int i=begin;i<textfields.size();i++) {
            textfields.get(i).setText("");
        }            
    }
    
    private void disableTextFields(Boolean flag) {
        for (int i=0;i<textfields.size();i++) {
            textfields.get(i).setDisable(flag);
        }            
    }
    
    private void disableButtons(Boolean flag) {
        for (int i=0;i<buttons.size();i++) {
            buttons.get(i).setDisable(flag);
        }            
    }
    
    private void handleBtn(String player1, String player2, int txt, int btn) {
        if (freakyleague.FLModel.insertMatch(textfields.get(0), player1,
                player2, textfields.get(txt), textfields.get(txt+1))) {
            lockControls(buttons.get(btn), textfields.get(txt),
                    textfields.get(txt+1));
            //only unlock next day if previous saved
            if(buttons.get(btn).isDisabled() && buttons.get(btn+1).isDisabled()) {
                unlockDay(buttons.get(btn+1), buttons.get(btn+2),
                        textfields.get(txt+2), textfields.get(txt+3),
                        textfields.get(txt+4), textfields.get(txt+5));
                //updating tableview and barchar
                loadLeagueTable();
                loadLeagueTableGlobals();
                bcLoad();
            }
        }
        else {
            //an error ocurred
        }
    }
    
    @FXML
    private void handleBtnM1D1Action(ActionEvent event) {
        handleBtn("Flip", "Ernie", 1, 0);
    }
    
    @FXML
    private void handleBtnM2D1Action(ActionEvent event) {
        handleBtn("Monkey", "Dog", 3, 1);
    }
 
    @FXML
    private void handleBtnM1D2Action(ActionEvent event) {
        handleBtn("Flip", "Monkey", 5, 2);
    }
    
    @FXML
    private void handleBtnM2D2Action(ActionEvent event) {
        handleBtn("Ernie", "Dog", 7, 3);
    }
    
    @FXML
    private void handleBtnM1D3Action(ActionEvent event) {
        handleBtn("Flip", "Dog", 9, 4);
    }
    
    @FXML
    private void handleBtnM2D3Action(ActionEvent event) {
        handleBtn("Ernie", "Monkey", 11, 5);
    }
    
    @FXML
    private void handleBtnM1D4Action(ActionEvent event) {
        handleBtn("Ernie", "Flip", 13, 6);
    }
    
    @FXML
    private void handleBtnM2D4Action(ActionEvent event) {
        handleBtn("Dog", "Monkey", 15, 7);
    }
    
    @FXML
    private void handleBtnM1D5Action(ActionEvent event) {
        handleBtn("Monkey", "Flip", 17, 8);
    }
    
    @FXML
    private void handleBtnM2D5Action(ActionEvent event) {
        handleBtn("Dog", "Ernie", 19, 9);
    }
    
    @FXML
    private void handleBtnM1D6Action(ActionEvent event) {
        handleBtn("Dog", "Flip", 21, 10);
    }
    
    @FXML
    private void handleBtnM2D6Action(ActionEvent event) {
        //handleBtn("Monkey", "Ernie", 23, 11);
        //maybe make an if in handleBtn
        if (freakyleague.FLModel.insertMatch(txtId, "Monkey", "Ernie", txtM2D6Monkey, txtM2D6Ernie)) {
            lockControls(btnM2D6, txtM2D6Monkey, txtM2D6Ernie);
            //only unlock next day if previous saved
            if(btnM1D6.isDisabled() && btnM2D6.isDisabled()) {
                //modal dialog with message and focus table??
                //updating tableview and barchar
                loadLeagueTable();
                loadLeagueTableGlobals();
                bcLoad();       
            }
        }
        else {
            //an error ocurred
        }
    }
    
    private void lockDay(boolean value, Button bt1, Button bt2, TextField txt1,
            TextField txt2, TextField txt3, TextField txt4)
    {
        bt1.setDisable(value);
        bt2.setDisable(value);
        txt1.setDisable(value);
        txt2.setDisable(value);
        txt3.setDisable(value);
        txt4.setDisable(value);         
    }
    
    private void lockControls(Button bt1, TextField txt1, TextField txt2) {
        bt1.setDisable(true);
        txt1.setDisable(true);
        txt2.setDisable(true);
    }
    
    private void bcLoadGetData(XYChart.Series ds ,String player) {
        ds.setName(player);
        ds.getData().add(new XYChart.Data("Victory",
                freakyleague.FLModel.giveMeData("GW", player)));
        ds.getData().add(new XYChart.Data("Drawn"  ,
                freakyleague.FLModel.giveMeData("GD", player)));
        ds.getData().add(new XYChart.Data("Lost"  ,
                freakyleague.FLModel.giveMeData("GL", player)));
    }
    
    private void bcLoad() {
        xAxis.setLabel("Number of matches");
        xAxis.setTickLabelRotation(90);
        //yAxis.setLabel("Player");
        //remove series
        bc.getData().remove(0, bc.getData().size());
        //Flip, Ernie. Monkey, Dog
        XYChart.Series dsFlip = new XYChart.Series();
        XYChart.Series dsErnie = new XYChart.Series();
        XYChart.Series dsMonkey = new XYChart.Series();
        XYChart.Series dsDog = new XYChart.Series();
        
        bcLoadGetData(dsFlip, "Flip");
        bcLoadGetData(dsErnie, "Ernie");
        bcLoadGetData(dsMonkey, "Monkey");
        bcLoadGetData(dsDog, "Dog");

        
        //adding series
        bc.getData().add(dsFlip);
        bc.getData().add(dsErnie);
        bc.getData().add(dsMonkey);
        bc.getData().add(dsDog);
    }
    
    private void setMenu() {
        //only admin user is allowed to create new tournaments
        if (freakyleague.FLModel.getUser().equals("guest")) {
            menuNew.setDisable(true);
        }
    }
    
    private void validateTFAsInt(TextField tf) {
        //https://stackoverflow.com/questions/35884398/javafx-textfield-validation-for-integer-input-and-also-allow-either-k-or-kfor-t
        tf.textProperty().addListener((ObservableValue<? extends String> obs, String oldValue, String newValue) -> {
           System.out.println("Text change from "+oldValue+" to "+newValue);
        });

        UnaryOperator<Change> filter = change -> {
            if (change.isAdded()) {
                String addedText = change.getText();
                //https://regex101.com/
                if (addedText.matches("/^([0-1]?[0-9]|20)$/")) {
                    return change ;
                }
                // remove illegal characters:
                int length = addedText.length();
                addedText = addedText.replaceAll("[^0-9kKmM]", "");
                // replace "k" and "K" with "000":
                addedText = addedText.replaceAll("[kK]", "000");
                // replace "m" and "M" with "000000":
                addedText = addedText.replaceAll("[mM]", "000000");
                change.setText(addedText);

                // modify caret position if size of text changed:
                int delta = addedText.length() - length ;
                change.setCaretPosition(change.getCaretPosition() + delta);  
                change.setAnchor(change.getAnchor() + delta);
                System.out.println(change.toString());
            }
            return change;
        };

        tf.setTextFormatter(new TextFormatter<String>(filter));
    
    }
    
    private void setTFValidation() {
        for (int i=0;i<textfields.size();i++) {
            validateTFAsInt(textfields.get(i));
        }            
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {     
        bcLoad();
        textfields.addAll(Arrays.asList(
            txtId, txtM1D1Flip, txtM1D1Ernie, txtM2D1Monkey, txtM2D1Dog,
            txtM1D2Flip, txtM1D2Monkey, txtM2D2Ernie, txtM2D2Dog, txtM1D3Flip,
            txtM1D3Dog, txtM2D3Ernie, txtM2D3Monkey, txtM1D4Ernie, txtM1D4Flip,
            txtM2D4Dog, txtM2D4Monkey, txtM1D5Monkey, txtM1D5Flip, txtM2D5Dog,
            txtM2D5Ernie, txtM1D6Dog, txtM1D6Flip, txtM2D6Monkey, txtM2D6Ernie
        ));
        
        buttons.addAll(Arrays.asList(
            btnM1D1, btnM2D1, btnM1D2, btnM2D2, btnM1D3, btnM2D3,
            btnM1D4, btnM2D4, btnM1D5, btnM2D5, btnM1D6, btnM2D6
        ));
        
        //force TextFields as integer 
        setTFValidation();
                
        setMenu();
    }
}
