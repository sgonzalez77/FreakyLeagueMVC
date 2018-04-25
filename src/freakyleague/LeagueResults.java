/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freakyleague;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author sergi
 */
public class LeagueResults {

    private final SimpleStringProperty  team;
    private final SimpleIntegerProperty  points;
    private final SimpleIntegerProperty   played;
    private final SimpleIntegerProperty   won;
    private final SimpleIntegerProperty   drawn;
    private final SimpleIntegerProperty  lost;
    private final SimpleIntegerProperty   GF;
    private final SimpleIntegerProperty   GA;
    private final SimpleIntegerProperty   GD;

    LeagueResults(String fTeam, int fWon,
                    int fDrawn, int fLost, int fGF, int fGA) {
        this.team = new SimpleStringProperty(fTeam);
        this.points = new SimpleIntegerProperty(fWon*3 +fDrawn);
        this.played = new SimpleIntegerProperty(fWon + fLost + fDrawn);
        this.won = new SimpleIntegerProperty(fWon);
        this.drawn = new SimpleIntegerProperty(fDrawn);
        this.lost = new SimpleIntegerProperty(fLost);
        this.GF = new SimpleIntegerProperty(fGF);
        this.GA = new SimpleIntegerProperty(fGA);
        this.GD = new SimpleIntegerProperty(fGF - fGA);
    }

    public String getTeam() {
        return team.get();
    }
    
    public void setTeam(String fTeam) {
        this.team.set(fTeam);
    }
    
    public int getPoints() {
        return points.get();
    }
    
    public void setPoints(int fPoints) {
        this.points.set(this.drawn.get() + this.won.get()*3);
    }
    
    public int getPlayed() {
        return played.get();
    }
    
    public void setPlayed(int fPlayed) {
        this.played.set(this.drawn.get() + this.won.get() + this.lost.get());
    }
    
    public int getWon() {
        return won.get();
    }
    
    public void setWon(int fWon) {
        this.won.set(fWon);
    }
    
    public int getDrawn() {
        return drawn.get();
    }
    
    public void setDrawn(int fDrawn) {
        this.drawn.set(fDrawn);
    }
    
    public int getGF() {
        return GF.get();
    }
    
    public void setGF(int fGF) {
        this.GF.set(fGF);
    }
    
    public int getGA() {
        return GA.get();
    }
    
    public void setGA(int fGA) {
        this.GA.set(fGA);
    }
    
    public int getGD() {
        return GD.get();
    }
    
    public void setGD(int fGD) {
        this.GD.set(this.GF.get() - this.GA.get());
    }
    
    public int getLost() {
        return lost.get();
    }
    
    public void setLost(int fLost) {
        this.lost.set(fLost);
    }
    
    
    
    

}
