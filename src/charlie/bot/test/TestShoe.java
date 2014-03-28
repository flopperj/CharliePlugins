/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package charlie.bot.test;
import charlie.card.Shoe;
import java.util.Random;

/**
 *
 * @author jamesarama
 */
public class TestShoe extends Shoe {

    @Override
    public void init() {
        ran = new Random(1);
        load();
        shuffle();
    }
}
