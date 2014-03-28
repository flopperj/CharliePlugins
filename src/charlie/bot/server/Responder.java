package charlie.bot.server;

import charlie.advisor.Advisor;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.dealer.Dealer;
import charlie.plugin.IPlayer;
import charlie.util.Play;

/**
 * This is the Responder Class
 * @author James Arama & Gabriela Rosales
 */
class Responder implements Runnable {

    /**
     * @private 
     * @property b9 - This is our b9 bot
     */
    private final IPlayer b9;
    /**
     * @private
     * @property myHand - Player/bot's hand
     */
    private final Hand myHand;
    /**
     * @private
     * @property dealer - Dealer
     */
    private final Dealer dealer;
    /**
     * private 
     * @property dealerHand - dealer's hand
     */
    private final Hand dealerHand;
    /**
     * private
     * @property advisor - play advisor
     */
    private final Advisor advisor;
    /**
     * private
     * @property THINK  - Delay time for thinking
     */
    private final int THINK;

    /**
     * Responder Constructor
     * 
     * @param bot
     * @param myHand
     * @param dealer
     * @param dealerHand 
     */
    public Responder(IPlayer bot, Hand myHand, Dealer dealer, Hand dealerHand) {
        this.b9 = bot;
        this.myHand = myHand;
        this.dealer = dealer;
        this.dealerHand = dealerHand;
        this.advisor = new Advisor();
        this.THINK = (int) (Math.random() * (5000 - 2000));
    }

    /**
     * Run method that will help determine the decisions B9 will make
     */
    @Override
    public void run() {

        // Get dealer's upcard
        Card dealerUpCard = dealerHand.getCard(dealerHand.size() - 1);
        Play[] plays = {Play.DOUBLE_DOWN, Play.HIT, Play.NONE, Play.SPLIT, Play.STAY};
        
        // Set up decision for B9
        int randomPlay = (int) (Math.random() * plays.length);        
        boolean randomChoice = (THINK % (Math.random() * 10) == 0);        

        Play advice;

        // sometimes b9 will ignore advice provided
        if (randomChoice) {
            advice = plays[randomPlay];
        } else {
            advice = this.advisor.advise(this.myHand, dealerUpCard);
        }

        // Make B9 appear to be thinking
        try {
            Thread.sleep(THINK);
        } catch (InterruptedException e) {
        }

        // Tell the dealer what plays B9 will make
        if (advice == Play.DOUBLE_DOWN && myHand.size() == 2) {
            dealer.doubleDown(b9, myHand.getHid());
        } else if (advice == Play.SPLIT) {
            if (myHand.getValue() >= 17 || (myHand.getValue() <= 16 && dealerUpCard.value() <= 6)) {
                dealer.stay(b9, myHand.getHid());
            } else if (myHand.getValue() <= 10 || (myHand.getValue() <= 16 && dealerUpCard.value() >= 7 && dealerUpCard.value() <= 11)) {
                dealer.hit(b9, myHand.getHid());
            } else if (myHand.getValue() == 11 && myHand.size() == 2) {
                dealer.doubleDown(b9, myHand.getHid());
            }
        } else if (advice == Play.STAY) {
            dealer.stay(b9, myHand.getHid());
        } else {
            dealer.hit(b9, myHand.getHid());
        }
    }

}
