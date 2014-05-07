package charlie.bot.client;

import charlie.actor.Courier;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.plugin.IGerty;
import charlie.view.AMoneyManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an artificial player
 *
 * @author James Arama
 * @author Gabriela Rosales
 */
public class Gerty implements IGerty {

    protected final Logger LOG = LoggerFactory.getLogger(Gerty.class);
    protected Courier gertyCourier;
    protected AMoneyManager gertyMoneyManager;
    protected Seat mySeat;
    protected Hid hid;
    protected Hid dealerHid;
    protected Hand myHand;
    protected HashMap<Hid, Hand> hands = new HashMap<>();
    protected Date start = new Date();
    protected Date end = new Date();

    protected int bustCount;
    protected int winCount;
    protected int lossCount;
    protected int blackjackCount;
    protected int pushCount;
    protected int charlieCount;
    protected int breakCount;
    protected int trueCount;
    protected int runningCount;
    protected int shoeSize;
    protected int meanBetAmount;
    protected int currentBetAmount;
    protected int maxBetAmount;
    protected int totalBetsAmount;
    protected int gamesPlayed;
    protected double deckSize;
    private boolean myTurn;

    /**
     * Default constructor
     */
    public Gerty() {
        this.bustCount = 0;
        this.blackjackCount = 0;
        this.breakCount = 0;
        this.charlieCount = 0;
        this.lossCount = 0;
        this.pushCount = 0;
        this.runningCount = 0;
        this.trueCount = 0;
        this.winCount = 0;
        this.meanBetAmount = 5;
        this.currentBetAmount = 5;
        this.maxBetAmount = 5;
        this.totalBetsAmount = 0;
        this.shoeSize = 0;
        this.gamesPlayed = 1;
        this.start = new Date();
        this.deckSize = 1.0;
    }

    /**
     * Tells bot it's time to make a bet to start a game. Invoke up bet with
     * minimum bet amount then bet the same amount from the courier which
     * returns an Hid
     */
    @Override
    public void go() {
        this.gertyMoneyManager.upBet(this.currentBetAmount, true);
        this.mySeat = Seat.YOU;
        this.hid = gertyCourier.bet(this.currentBetAmount, 0);
        this.myHand = new Hand(this.hid);
    }

    /**
     * Sets our courier
     *
     * @param courier
     */
    @Override
    public void setCourier(Courier courier) {
        this.gertyCourier = courier;
    }

    /**
     * Sets our money manager
     *
     * @param moneyManager
     */
    @Override
    public void setMoneyManager(AMoneyManager moneyManager) {
        this.gertyMoneyManager = moneyManager;
    }

    @Override
    public void update() {
        this.end = new Date();
    }

    /**
     * Renders the bot.
     *
     * @param g Graphics context.
     */
    @Override
    public void render(Graphics2D g) {
        g.setFont(new Font("ARIAL", Font.BOLD, 14));
        int startX = 10;

        // calculate time
        long diffInSeconds = (this.end.getTime() - this.start.getTime()) / 1000;

        long diff[] = new long[]{0, 0, 0, 0};
        /* sec */
        diff[3] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
        /* min */
        diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
        /* hours */
        diff[1] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
        /* days */
//        diff[0] = (diffInSeconds = (diffInSeconds / 24));

        // Draw side bet rules    
        g.setColor(new Color(203, 241, 115));
        g.fillRoundRect(startX - 5, 30, 135, 320, 5, 5);
        g.setColor(Color.BLACK);
        g.drawString("Hi-Low", startX, 70);
        g.drawString("Shoe Size: " + this.shoeSize, startX, 90);
        g.drawString("Running Count: " + this.runningCount, startX, 110);
        g.drawString("True Count: " + this.trueCount, startX, 130);
        g.drawString("Games Played: " + gamesPlayed, startX, 150);
        g.drawString("Time:\n " + String.format(
                "%02d:%02d:%02d",
                diff[1],
                diff[2],
                diff[3]), startX, 170);
        g.drawString("Max bet: " + this.currentBetAmount, startX, 190);
        g.drawString("Mean bet: " + this.meanBetAmount, startX, 210);
        g.drawString("Blackjacks: " + this.blackjackCount, startX, 230);
        g.drawString("Charlies: " + this.charlieCount, startX, 250);
        g.drawString("Wins: " + this.winCount, startX, 270);
        g.drawString("Breaks: " + this.breakCount, startX, 290);
        g.drawString("Loses: " + this.lossCount, startX, 310);
        g.drawString("Pushes: " + this.pushCount, startX, 330);
    }

    /**
     * Starts game with list of hand ids and shoe size. The number of decks is
     * shoeSize / 52.
     *
     * @param hids
     * @param shoeSize
     */
    @Override
    public void startGame(List<Hid> hids, int shoeSize) {
        this.shoeSize = shoeSize;

        for (Hid _hid : hids) {
            Hand newHand = new Hand(_hid);
            hands.put(_hid, newHand);
            if (Seat.DEALER == _hid.getSeat()) {
                this.dealerHid = _hid;
            }
        }

        this.deckSize = shoeSize > 52 ? shoeSize / 52 : 1.0;
    }

    /**
     * Ends a game with shoe size.
     *
     * @param shoeSize Shoe size
     */
    @Override
    public void endGame(int shoeSize) {
        this.gamesPlayed++;
        this.myTurn = false;
        this.deckSize = shoeSize > 52 ? shoeSize / 52 : 1.0;
        LOG.info("received endGame shoeSize = " + shoeSize);
    }

    /**
     * Deals a card to player. All players receive all cards which is useful for
     * card counting.
     *
     * @param hid Hand id which might not necessarily belong to player.
     * @param card Card being dealt
     * @param values Hand values, literal and soft
     */
    @Override
    public void deal(Hid hid, Card card, int[] values) {
        LOG.info("got card = " + card + " hid = " + hid);

        if (card == null) {
            return;
        }

        // Retrieve the hand
        Hand hand = hands.get(hid);

        // If the hand does not exist...this could happen if
        // player splits a hand which we don't yet know about.
        // In this case, we'll create the hand "on the fly", as it were.
        if (hand == null) {
            hand = new Hand(hid);
            hands.put(hid, hand);
        }

        // Set dealerupCard
        if (this.dealerHid == null && hid.getSeat() == Seat.DEALER) {
            this.dealerHid = hid;
        }

        // Hit the hand
        hand.hit(card);

        // calculate running Hi-Lo count.
        int currentCardValue = card.getRank();
        if (currentCardValue > 9 || currentCardValue == 1) {
            --runningCount;
        } else if (currentCardValue < 7) {
            ++runningCount;
        }

        // calculate true count
        this.trueCount = (int) Math.ceil(this.runningCount / this.deckSize);

        // update the mean bets
        this.totalBetsAmount += this.currentBetAmount;
        this.meanBetAmount = this.totalBetsAmount / this.gamesPlayed;

        // It's not my turn if card not mine, my hand broke, or
        // this is the first round of cards in which case it's not
        // my turn!
        if (hid.getSeat() != this.mySeat || hand.isBroke() || !this.myTurn) {
            this.myTurn = false;
            LOG.info("GERTY KNOWS ITS NOT ITS TURN");
        } else {
            LOG.info("IT IS GERTYS TURN");
            // It's my turn, a card has come my way, and I have to respond
            play(this.hid);
        }
    }

    /**
     * Responds when it is my turn.
     */
    protected void respond() {
        if (myTurn == true) {
            new Thread(new GertyResponder(this, this.myHand, this.gertyCourier, this.hands.get(this.dealerHid))).start();
            LOG.info("START THREAD");
        } else {
            LOG.info("IT SHOULDNT BE IMPLEMENTING!");
        }
    }

    /**
     * Offers player chance to buy insurance.
     */
    @Override
    public void insure() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Tells player the hand is broke.
     *
     * @param hid Hand id
     */
    @Override
    public void bust(Hid hid) {
        this.bustCount++;
    }

    /**
     * Tells player the hand won.
     *
     * @param hid Hand id
     */
    @Override
    public void win(Hid hid) {
        this.winCount++;
    }

    /**
     * Tells player the hand won with blackjack.
     *
     * @param hid Hand id
     */
    @Override
    public void blackjack(Hid hid) {
        this.blackjackCount++;
    }

    /**
     * Tells player the hand won with Charlie.
     *
     * @param hid Hand id
     */
    @Override
    public void charlie(Hid hid) {
        this.charlieCount++;
    }

    /**
     * Tells player the hand lost to dealer.
     *
     * @param hid Hand id
     */
    @Override
    public void lose(Hid hid) {
        this.lossCount++;
    }

    /**
     * Tells player the hand pushed.
     *
     * @param hid
     */
    @Override
    public void push(Hid hid) {
        this.pushCount++;
    }

    /**
     * Tells player the hand pushed.
     */
    @Override
    public void shuffling() {
        LOG.info("SHUFFLING...");
    }

    /**
     * Tells player to start playing hand.
     *
     * @param hid
     */
    @Override
    public void play(Hid hid) {
        // If it is not my turn, there's nothing to do
        if (hid.getSeat() != this.mySeat) {
            myTurn = false;
            LOG.info("STILL not Gerty's turn");
        } else {

            // Othewise respond
            LOG.info("turn hid = " + hid);
            myTurn = true;

            // It's my turn and I have to respond
            respond();
        }
    }
}
