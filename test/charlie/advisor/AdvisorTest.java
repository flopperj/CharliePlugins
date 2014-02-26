/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package charlie.advisor;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jamesarama
 */
public class AdvisorTest {

    public AdvisorTest() {
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
     * Test of advise method, of class Advisor.
     */
    @Test
    public void testAdvise() {

        //========== A and 2 of clubs=========================================
        System.out.println("advise => Header: A,2 and Dealer: 2");
        Hid hid = new Hid(Seat.YOU,0 ,0);
        Hand myHand = new Hand(hid);

        Card ace = new Card(Card.ACE, Card.Suit.CLUBS);
        Card two = new Card(2, Card.Suit.CLUBS);
        myHand.hit(ace);
        myHand.hit(two);

        Card upCard = new Card(2, Card.Suit.CLUBS);
        Advisor instance = new Advisor();

        Play expResult = Play.HIT;
        Play result = instance.advise(myHand, upCard);
        assertEquals(expResult, result);

        //========== 2s of clubs, Diamonds, clubs and Hearts ===================
        System.out.println("advise => Header: 2,2 and Dealer: 2");
        Hid hid2 = new Hid(Seat.YOU,0 ,0);
        Hand myHand2 = new Hand(hid2);

        Card two1 = new Card(2, Card.Suit.DIAMONDS);
        Card two2 = new Card(2, Card.Suit.CLUBS);
        myHand2.hit(two1);
        myHand2.hit(two2);

        Card upCard2 = new Card(2, Card.Suit.HEARTS);
        Advisor instance2 = new Advisor();

        Play expResult2 = Play.SPLIT;
        Play result2 = instance2.advise(myHand2, upCard2);
        assertEquals(expResult2, result2);

        //========== Ace of Diamonds, 2 of clubs and 6 of Hearts ===============
        System.out.println("advise => Header: A,5 and Dealer: 2");
        Hid hid3 = new Hid(Seat.YOU,0 ,0);
        Hand myHand3 = new Hand(hid3);

        Card ace2 = new Card(Card.ACE, Card.Suit.DIAMONDS);
        Card five = new Card(2, Card.Suit.CLUBS);
        myHand3.hit(ace2);
        myHand3.hit(five);

        Card upCard3 = new Card(6, Card.Suit.HEARTS);
        Advisor instance3 = new Advisor();

        Play expResult3 = Play.DOUBLE_DOWN;
        Play result3 = instance3.advise(myHand3, upCard3);
        assertEquals(expResult3, result3);
    }

}
