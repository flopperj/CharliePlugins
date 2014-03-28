/*
 Copyright (c) 2014 Ron Coleman

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package charlie.bot.server;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Dealer;
import charlie.dealer.Seat;
import charlie.plugin.IBot;
import charlie.util.Constant;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the IBot with the aid of a worker thread.
 * @author Ron Coleman
 */
public class B9 implements IBot {
    private final Logger LOG = LoggerFactory.getLogger(B9.class);
    protected Hid hid;
    protected Dealer dealer;
    protected Seat mine;
    protected HashMap<Hid,Hand> hands = new HashMap<>();
    protected Hid dealerHid;
    private Hand myHand;
    protected boolean myTurn = false;
    protected int shoeSize;
    private Card dealerUpCard;
    
    /**
     * Constructor
     */
    public B9 () {
        
    }
    
    /**
     * Gets the bots hand.
     * @return Hand
     */
    @Override
    public Hand getHand() {
        return myHand;
    }

    /**
     * Sets the dealer to which the bot responds.
     * @param dealer 
     */
    @Override
    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    /**
     * Sits the bot at a given seat.
     * @param seat Seat
     */
    @Override
    public void sit(Seat seat) {
        this.mine = seat;
        
        this.hid = new Hid(seat,Constant.BOT_MIN_BET,0);
        
        this.myHand = new Hand(this.hid);
    }

    /**
     * Starts a game.
     * @param hids Hand ids
     * @param shoeSize Shoe size at game start before cards dealt.
     */
    @Override
    public void startGame(List<Hid> hids, int shoeSize) {
        for(Hid hid_: hids) {
            hands.put(hid_,new Hand(hid_));
            
            if(hid_.getSeat() == Seat.DEALER)
                this.dealerHid = hid_;
        }       
        this.shoeSize = shoeSize;
    }

    /**
     * Ends a game.
     * @param shoeSize Shoe size at game end after cards dealt.
     */
    @Override
    public void endGame(int shoeSize) {
        LOG.info("received endGame shoeSize = "+shoeSize);
    }

    /**
     * Deals a card.
     * @param hid Target hand id
     * @param card Card
     * @param values Hard and soft values of a hand.
     */
    @Override
    public void deal(Hid hid, Card card, int[] values) {
        LOG.info("got card = "+card+" hid = "+hid);
        
        if(card == null)
            return;
        
        // Retrieve the hand
        Hand hand = hands.get(hid);
        
        // If the hand does not exist...this could happen if
        // player splits a hand which we don't yet know about.
        // In this case, we'll create the hand "on the fly", as it were.
        if(hand == null) {
            hand = new Hand(hid);
            hands.put(hid, hand);
        }
        
        // Set dealerupCard
        if (this.dealerHid == null && hid.getSeat() == Seat.DEALER){
            this.dealerHid = hid;
            this.dealerUpCard = card; 
        }
        
        // Hit the hand
        hand.hit(card);

        // It's not my turn if card not mine, my hand broke, or
        // this is the first round of cards in which case it's not
        // my turn!
        if(hid.getSeat() != mine || hand.isBroke() || !myTurn) {
            myTurn = false;
            LOG.info("IT KNOWS ITS NOT ITS TURN");
            return;
        } else {
            LOG.info("IT IS ITS TURN");
            // It's my turn, a card has come my way, and I have to respond
            respond();
        }      
    }
    
    /**
     * Responds when it is my turn.
     */
    protected void respond() {
        if (myTurn == true){
            new Thread(new Responder(this, myHand, dealer, hands.get(dealerHid))).start(); 
            LOG.info("START THREAD");
        } else {
            LOG.info("IT SHOULDNT BE IMPLEMENTING!");
        }
    }
    
    /**
     * Buys insurance -- NOT SUPPORTED.
     */
    @Override
    public void insure() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Handles bust.
     * @param hid Hand id
     */
    @Override
    public void bust(Hid hid) {
    }

    /**
     * Handles win.
     * @param hid Hand id
     */
    @Override
    public void win(Hid hid) {
    }

    /**
     * Handles blackjack.
     * @param hid Hand id
     */
    @Override
    public void blackjack(Hid hid) {
    }

    /**
     * Handles charlie.
     * @param hid Hand id
     */    
    @Override
    public void charlie(Hid hid) {
    }
    
    /**
     * Handles loss.
     * @param hid Hand id
     */ 
    @Override
    public void lose(Hid hid) {
    }
    
    /**
     * Handles push.
     * @param hid Hand id
     */ 
    @Override
    public void push(Hid hid) {
    }
    
    /**
     * Handles shuffling.
     */ 
    @Override
    public void shuffling() {
    }
    
    /**
     * Handles my turn.
     * @param hid Hand id
     */ 
    @Override
    public void play(Hid hid) {
        // If it is not my turn, there's nothing to do
        if(hid.getSeat() != mine){
            myTurn = false;
            LOG.info("STILL not its turn");
            return;
        }else{
        
        // Othewise respond
        LOG.info("turn hid = "+hid);
        myTurn = true;
        
        // It's my turn and I have to respond
        respond();
        }
    }
}