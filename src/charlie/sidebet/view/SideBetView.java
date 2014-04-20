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
package charlie.sidebet.view;

import charlie.audio.Effect;
import charlie.audio.SoundFactory;
import charlie.card.Hid;
import charlie.plugin.ISideBetView;
import charlie.view.AMoneyManager;
import charlie.view.sprite.Chip;
import charlie.view.sprite.ChipButton;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the side bet view
 *
 * @author Ron Coleman, Ph.D.
 * @author James Arama
 * @author Gabriela Rosales
 */
public class SideBetView implements ISideBetView {

    private final Logger LOG = LoggerFactory.getLogger(SideBetView.class);

    public final static int X = 400;
    public final static int Y = 200;
    public final static int DIAMETER = 50;

    protected Font font = new Font("Arial", Font.BOLD, 18);
    protected BasicStroke stroke = new BasicStroke(3);

    // See http://docs.oracle.com/javase/tutorial/2d/geometry/strokeandfill.html
    protected float dash1[] = {10.0f};
    protected BasicStroke dashed
            = new BasicStroke(3.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10.0f, dash1, 0.0f);

    protected List<ChipButton> buttons;
    protected int amt = 0;
    protected AMoneyManager moneyManager;
    protected Hid hid;

    protected ArrayList<Chip> sideBetChips = new ArrayList<>();
    protected int[] sideBetAmounts = {100, 25, 5};
    protected Random random = new Random();
    protected boolean hasWinningStreak = false;
    protected boolean hasEnded = false;

    protected Color winColorBg = new Color(116, 255, 4);
    protected Color winColorFg = Color.BLACK;
    protected Color loseColorBg = new Color(250, 58, 5);
    protected Color loseColorFg = Color.WHITE;
    protected Color rulesColorBg = new Color(203, 241, 115);

    /**
     * Default constructor
     */
    public SideBetView() {
        LOG.info("side bet view constructed");
    }

    /**
     * Sets the money manager.
     *
     * @param moneyManager
     */
    @Override
    public void setMoneyManager(AMoneyManager moneyManager) {
        this.moneyManager = moneyManager;
        this.buttons = moneyManager.getButtons();
    }

    /**
     * Registers a click for the side bet.
     *
     * @param clickX X coordinate
     * @param clickY Y coordinate
     */
    @Override
    public void click(int clickX, int clickY) {
        int oldAmt = amt;
        int chipIndex = 0;

        // Test if any chip button has been pressed.
        for (ChipButton button : buttons) {
            chipIndex++;
            if (button.isPressed(clickX, clickY)) {
                amt += button.getAmt();

                int n = this.sideBetChips.size();
                int width = 40;
                int height = 40;

                int placeX = X + n * width / 3 + random.nextInt(10) + 25;

                int placeY = Y + random.nextInt(5) - 22;

                Chip chip = new Chip(button.getImage(), placeX, placeY, sideBetAmounts[chipIndex % 3]);

                sideBetChips.add(chip);
                LOG.info("A. side bet amount " + button.getAmt() + " updated new amt = " + amt);

                SoundFactory.play(Effect.CHIPS_IN);
            }
        }

        // use circle as touch area
        if (clickX > (X - DIAMETER / 2)
                && clickX < (X + DIAMETER / 2)
                && clickY > (Y - DIAMETER / 2)
                && clickY < (Y + DIAMETER / 2)) {
            if (oldAmt == amt) {
                amt = 0;
                sideBetChips.clear();
                LOG.info("B. side bet amount cleared");

                SoundFactory.play(Effect.CHIPS_OUT);
            }
        }
    }

    /**
     * Informs view the game is over and it's time to update the bankroll for
     * the hand.
     *
     * @param hid Hand id
     */
    @Override
    public void ending(Hid hid) {
        double bet = hid.getSideAmt();
        this.hid = hid;

        hasWinningStreak = false;
        hasEnded = true;
        if (bet == 0) {
            return;
        } else if (bet > 0) {
            hasWinningStreak = true;
        }

        LOG.info("side bet outcome = " + bet);

        // Update the bankroll
        moneyManager.increase(bet);

        LOG.info("new bankroll = " + moneyManager.getBankroll());
    }

    /**
     * Informs view the game is starting
     */
    @Override
    public void starting() {
        this.hasEnded = false;
        this.hid = null;
    }

    /**
     * Gets the side bet amount.
     *
     * @return Bet amount
     */
    @Override
    public Integer getAmt() {
        return amt;
    }

    /**
     * Updates the view
     */
    @Override
    public void update() {
    }

    /**
     * Renders the view
     *
     * @param g Graphics context
     */
    @Override
    public void render(Graphics2D g) {
        // Draw the at-stake place on the table
        g.setColor(Color.RED);
        g.setStroke(dashed);
        g.drawOval(X - DIAMETER / 2, Y - DIAMETER / 2, DIAMETER, DIAMETER);

        // Draw the at-stake amount
        g.setFont(font);
        g.setColor(Color.WHITE);

        int length = (int) (Math.log10(amt) + 1);

        // center sidebet amount inside the dashed circle
        if (length <= 2) {
            g.drawString("" + amt, X - 5, Y + 5);
        } else if (length == 3) {
            g.drawString("" + amt, X - 15, Y + 5);
        } else if (length == 4) {
            g.drawString("" + amt, X - 20, Y + 5);
        } else {
            g.drawString("" + amt, X - 25, Y + 5);
        }

        // Draw side bet rules    
        g.setColor(rulesColorBg);
        g.fillRoundRect(X + 40, Y + 70, 245, 90, 5, 5);
        g.setColor(Color.BLACK);
        g.drawString("SUPER 7 pays 3:1", X + 50, Y + 100);
        g.drawString("ROYAL MATCH pays 25:1", X + 50, Y + 120);
        g.drawString("EXACTLY 13 pays 1:1", X + 50, Y + 140);

        // Draw side bet chips
        for (Chip chip : sideBetChips) {
            chip.render(g);
        }

        //check if we need to render outcome of our side bet
        double bet = 0.0;
        if (this.hid != null) {
            bet = hid.getSideAmt();
        }

        // we also only want to render when the game has ended
        if (bet != 0.0 && hasEnded) {
            if (hasWinningStreak) {
                g.setColor(winColorBg);
                g.fillRoundRect(X + 50, Y - 15, 65, 25, 5, 5);
                g.setColor(winColorFg);
                g.drawString("WIN!", X + 63, Y + 5);
            } else {
                g.setColor(loseColorBg);
                g.fillRoundRect(X + 50, Y - 15, 75, 25, 5, 5);
                g.setColor(loseColorFg);
                g.drawString("LOSE!", X + 59, Y + 5);
            }
        }
    }
}
