package charlie.bot.test;

import java.util.Random;

/**
 * This is our shoe for the bot test
 *
 * @author jamesarama
 */
public class Shoe extends charlie.card.Shoe {

    @Override
    public void init() {
        ran = new Random(1);
        load();
        shuffle();
    }
}
