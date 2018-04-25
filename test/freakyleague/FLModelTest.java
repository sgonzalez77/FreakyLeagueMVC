/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freakyleague;

import java.util.List;
import javafx.scene.control.TextField;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sergi
 */
public class FLModelTest {
    
    public FLModelTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getUser method, of class FLModel.
     */
    @Test
    public void testGetUser() {
        System.out.println("getUser");
        String expResult = "";
        String result = FLModel.getUser();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setUser method, of class FLModel.
     */
    @Test
    public void testSetUser() {
        System.out.println("setUser");
        String pUser = "";
        FLModel.setUser(pUser);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generalReport method, of class FLModel.
     */
    @Test
    public void testGeneralReport() {
        System.out.println("generalReport");
        FLModel.generalReport();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of tournamentReport method, of class FLModel.
     */
    @Test
    public void testTournamentReport() {
        System.out.println("tournamentReport");
        String txtId = "";
        String date = "";
        FLModel.tournamentReport(txtId, date);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of closeTournament method, of class FLModel.
     */
    @Test
    public void testCloseTournament() {
        System.out.println("closeTournament");
        String txtId = "";
        Boolean expResult = null;
        Boolean result = FLModel.closeTournament(txtId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteDBGarbage method, of class FLModel.
     */
    @Test
    public void testDeleteDBGarbage() {
        System.out.println("deleteDBGarbage");
        FLModel.deleteDBGarbage();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of giveMeTournaments method, of class FLModel.
     */
    @Test
    public void testGiveMeTournaments() {
        System.out.println("giveMeTournaments");
        List<Tournament> expResult = null;
        List<Tournament> result = FLModel.giveMeTournaments();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of giveMeTournamentData method, of class FLModel.
     */
    @Test
    public void testGiveMeTournamentData() {
        System.out.println("giveMeTournamentData");
        int idT = 0;
        List<Match> expResult = null;
        List<Match> result = FLModel.giveMeTournamentData(idT);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of newTournament method, of class FLModel.
     */
    @Test
    public void testNewTournament() {
        System.out.println("newTournament");
        Tournament expResult = null;
        Tournament result = FLModel.newTournament();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of giveMeData method, of class FLModel.
     */
    @Test
    public void testGiveMeData_3args() {
        System.out.println("giveMeData");
        String type = "";
        String idTeam = "";
        String idTourn = "";
        int expResult = 0;
        int result = FLModel.giveMeData(type, idTeam, idTourn);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of giveMeData method, of class FLModel.
     */
    @Test
    public void testGiveMeData_String_String() {
        System.out.println("giveMeData");
        String type = "";
        String idTeam = "";
        int expResult = 0;
        int result = FLModel.giveMeData(type, idTeam);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insertMatch method, of class FLModel.
     */
    @Test
    public void testInsertMatch() {
        System.out.println("insertMatch");
        TextField txtId = null;
        String player1 = "";
        String player2 = "";
        TextField sHome = null;
        TextField sAway = null;
        boolean expResult = false;
        boolean result = FLModel.insertMatch(txtId, player1, player2, sHome, sAway);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of validateUser method, of class FLModel.
     */
    @Test
    public void testValidateUser() {
        System.out.println("validateUser");
        String user = "";
        String password = "";
        boolean expResult = false;
        boolean result = FLModel.validateUser(user, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
