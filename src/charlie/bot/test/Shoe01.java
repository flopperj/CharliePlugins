package charlie.bot.test;

import java.util.Random;

/**
 * This is our shoe for the client bot test
 *
 * @author jamesarama
 */
public class Shoe01 extends charlie.card.Shoe {

    @Override
    public void init() {
        super.ran = new Random(7);

        super.numDecks = 1;

        super.load();

        super.shuffle();
    }
}
