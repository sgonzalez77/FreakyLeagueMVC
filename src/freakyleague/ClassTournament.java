/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freakyleague;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.DatePicker;


/**
 *
 * @author sergi
 */
public class ClassTournament {
    private int id;
    private String date;

    ClassTournament(int pId, String pDate) {
        id = pId;
        date = pDate;
    }
    
    ClassTournament(String pId, String pDate) {
        id = Integer.parseInt(pId);
        date = pDate;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }
    
    public LocalDate getDateLD() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate newLD = LocalDate.parse(date, formatter);
        //you can't set a date with datepicker disabled
        return newLD;
    }
    
    public String getDateStr() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    
}
