package charlie.bot.client;

import charlie.actor.Courier;
import charlie.advisor.Advisor;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.plugin.IGerty;
import charlie.util.Play;

/**
 * This is the Responder Class
 *
 * @author James Arama & Gabriela Rosales
 */
class GertyResponder implements Runnable {

    /**
     * @private @property gerty
     */
    private final IGerty gerty;
    /**
     * @private @property myHand - Player/gerty's hand
     */
    private final Hand myHand;
    /**
     * @private @property dealerHand - dealer's hand
     */
    private final Hand dealerHand;
    /**
     * private
     *
     * @property advisor - play advisor
     */
    private final Advisor advisor;
    /**
     * @private @property THINK - Delay time for thinking
     */
    private final int THINK;
    /**
     * @private @property courier - Our dealer
     */
    private final Courier courier;

    /**
     * Responder Constructor
     *
     * @param gerty
     * @param myHand
     * @param courier
     * @param dealerHand
     */
    public GertyResponder(IGerty gerty, Hand myHand, Courier courier, Hand dealerHand) {
        this.gerty = gerty;
        this.myHand = myHand;
        this.courier = courier;
        this.dealerHand = dealerHand;
        this.advisor = new Advisor();
        this.THINK = (int) (Math.random() * (5000 - 2000));
    }

    /**
     * Run method that will help determine the decisions Gerty will make
     */
    @Override
    public void run() {

        // Get dealer's upcard
        Card dealerUpCard = dealerHand.getCard(dealerHand.size() - 1);
        Play[] plays = {Play.DOUBLE_DOWN, Play.HIT, Play.NONE, Play.SPLIT, Play.STAY};

        // Set up decision for Gerty
        int randomPlay = (int) (Math.random() * plays.length);
        boolean randomChoice = (THINK % (Math.random() * 10) == 0);

        Play advice;

        // sometimes gerty will ignore advice provided
        if (randomChoice) {
            advice = plays[randomPlay];
        } else {
            advice = this.advisor.advise(this.myHand, dealerUpCard);
        }

        // Make Gerty appear to be thinking
        try {
            Thread.sleep(THINK);
        } catch (InterruptedException e) {
        }

        // Tell the dealer what plays B9 will make
        if (myHand.size() == 2 && advice == Play.DOUBLE_DOWN) {
            courier.dubble(myHand.getHid());
        } else if (advice == Play.SPLIT) {
            if (myHand.size() == 2 && myHand.getValue() == 11) {
                courier.dubble(myHand.getHid());
            } else if ((myHand.getValue() <= 16 && dealerUpCard.value() >= 7 && dealerUpCard.value() <= 11) || myHand.getValue() <= 10) {
                courier.hit(myHand.getHid());
            } else if (myHand.getValue() >= 17 || (myHand.getValue() <= 16 && dealerUpCard.value() <= 6)) {
                courier.stay(myHand.getHid());
            }
        } else if (advice == Play.STAY) {
            courier.stay(myHand.getHid());
        } else {
            courier.stay(myHand.getHid());
        }
    }

}
