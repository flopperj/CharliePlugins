package charlie.advisor;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.plugin.IAdvisor;
import charlie.util.Play;

/**
 *This is the Advisor class
 * @author jamesarama
 * @author gabriela
 */
public class Advisor implements IAdvisor {

    /**
     * This is the advise method
     * @param myHand
     * @param upCard
     * @return advisedPlay
     */
    @Override
    public Play advise(Hand myHand, Card upCard) {
        BasicStrategy basicStrategy = new BasicStrategy();
        Play advisedPlay = basicStrategy.getPlay(myHand, upCard);
        return advisedPlay;
    }

}
