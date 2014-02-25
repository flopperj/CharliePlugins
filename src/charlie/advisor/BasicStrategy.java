/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package charlie.advisor;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.util.Play;
import java.util.HashMap;

/**
 *
 * @author jamesarama
 */
public class BasicStrategy {

    private HashMap<String, Play> _valuePlays;
    private HashMap<String, Play> _acePlays;
    private HashMap<String, Play> _pairPlays;
    private HashMap<String, HashMap<String, Play>> _plays;

    public BasicStrategy() {
        this._valuePlays = new HashMap<>();
        this._acePlays = new HashMap<>();
        this._pairPlays = new HashMap<>();

        this._populateValueCards();
        this._populateAcePlays();
        this._populatePairPlays();
    }

    /**
     * Gets a play based on the hand and dealer upCard
     *
     * @public
     * @method getPlay
     * @param myHand
     * @param upCard
     * @return
     */
    public Play getPlay(Hand myHand, Card upCard) {
        return null;
    }

    /**
     * Gets the dealer upCards
     *
     * @public
     * @method getDealerUpCards
     * @return dealerUpCards
     */
    public String[] getDealerUpCards() {

        int numUpCards = 10;
        String[] dealerUpCards = new String[numUpCards];

        // populate the upcards
        for (int i = 0; i < numUpCards; i++) {
            String value = (i + 2) < 11 ? Integer.toString(i + 2) : "A";
            dealerUpCards[i] = value;
        }

        return dealerUpCards;
    }

    /**
     * Gets sanitized cards faces/indexes that we'll use internally
     *
     * @private
     * @method _getSanitizedCards
     * @param cards
     * @return newCards
     */
    private int[] _getSanitizeCards(String[] cards) {
        int[] newCards = new int[cards.length];
        int i = 0;
        for (String card : cards) {

            int val;
            if (card.equals("A")) {
                val = 11;
            } else {
                val = Integer.parseInt(card);
            }

            newCards[i++] = val;
        }
        return newCards;
    }

    /**
     * Populates the value card plays
     *
     * @private
     * @method _populateValueCards
     * @returns void
     */
    private void _populateValueCards() {

        String[] upCards = this.getDealerUpCards();
        int[] upCardIndexes = this._getSanitizeCards(upCards);

        // loop through dealer upcards
        for (int dealer : upCardIndexes) {

            int firstCard = 5;
            int limitValue = 17;

            // loop through the values
            for (int value = firstCard; value <= limitValue; value++) {

                //============== Hit ===========================================
                if (value >= 5 && value <= 8) {
                    this._valuePlays.put(upCards[dealer - 2], Play.HIT);
                } else if (value == 9) {
                    if (dealer <= 2 && dealer >= 7) {
                        this._valuePlays.put(upCards[dealer - 2], Play.HIT);
                    }
                } else if (value == 10) {
                    if (dealer >= 10) {
                        this._valuePlays.put(upCards[dealer - 2], Play.HIT);
                    }
                } else if (value == 11) {
                    if (dealer == 11) {
                        this._valuePlays.put(upCards[dealer - 2], Play.HIT);
                    }
                } else if (value >= 12 && value <= 16) {
                    if (value == 12) {
                        if (dealer >= 2 && dealer <= 3) {
                            this._valuePlays.put(upCards[dealer - 2], Play.HIT);
                        } else if (dealer >= 7 && dealer <= 11) {
                            this._valuePlays.put(upCards[dealer - 2], Play.HIT);
                        }
                    } else if (dealer >= 7 && dealer <= 11 && value > 12) {
                        this._valuePlays.put(upCards[dealer - 2], Play.HIT);
                    }
                }

                //============== Stand =========================================
                if (value == 17) {
                    this._valuePlays.put(upCards[dealer - 2], Play.STAY);
                } else if (value >= 12 && value <= 16) {

                    if (value == 12 && dealer >= 4 && dealer <= 6) {
                        this._valuePlays.put(upCards[dealer - 2], Play.STAY);
                    } else if (value > 12 && dealer >= 2 && dealer <= 6) {
                        this._valuePlays.put(upCards[dealer - 2], Play.STAY);
                    }

                }

                //============== Double Down ===================================
                if (value == 9 && dealer >= 3 && dealer <= 6) {
                    this._valuePlays.put(upCards[dealer - 2], Play.STAY);
                } else if (value == 10 && dealer >= 2 && dealer <= 9) {
                    this._valuePlays.put(upCards[dealer - 2], Play.STAY);
                } else if (value == 11 && dealer >= 2 && dealer <= 10) {
                    this._valuePlays.put(upCards[dealer - 2], Play.STAY);
                }

                //============== Split ===================================
                // None
            }

        }
    }

    /**
     * Populates the Ace plays
     *
     * @private
     * @method _populateAcePlays
     * @return void
     */
    private void _populateAcePlays() {
        String[] upCards = this.getDealerUpCards();
        int[] upCardIndexes = this._getSanitizeCards(upCards);

        // loop through dealer upcards
        for (int dealer : upCardIndexes) {

            int firstCard = 5;
            int limitValue = 17;

            // loop through the values
            for (int value = firstCard; value <= limitValue; value++) {
            }

        }
    }

    /**
     * Populates the Pair Plays
     *
     * @private
     * @method _populatePairPlays
     * @return void
     */
    private void _populatePairPlays() {

        String[] upCards = this.getDealerUpCards();
        int[] upCardIndexes = this._getSanitizeCards(upCards);

        // loop through dealer upcards
        for (int dealer : upCardIndexes) {

            int firstCard = 5;
            int limitValue = 17;

            // loop through the values
            for (int value = firstCard; value <= limitValue; value++) {
            }

        }
    }

}
