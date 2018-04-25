/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freakyleague;

/**
 *
 * @author sergi
 */
public class Match {
    private int tournament;
    private String home;
    private String away;
    private int scoreHome;
    private int scoreAway;

    public Match(int pTournament, String pHome, String pAway,
            int pScoreHome, int pScoreAway) {
        tournament = pTournament;
        home = pHome;
        away = pAway;
        scoreHome = pScoreHome;
        scoreAway = pScoreAway;
    }
    
    public int getTournament() {
        return tournament;
    }

    public void setTournament(int tournament) {
        this.tournament = tournament;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public int getScoreHome() {
        return scoreHome;
    }

    public void setScoreHome(int scoreHome) {
        this.scoreHome = scoreHome;
    }

    public int getScoreAway() {
        return scoreAway;
    }

    public void setScoreAway(int scoreAway) {
        this.scoreAway = scoreAway;
    }
    
    
}
