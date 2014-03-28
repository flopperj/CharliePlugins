package charlie.advisor;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.util.Play;
import java.util.HashMap;

/**
 * This is the basic strategy class
 *
 * @author jamesarama
 * @author gabriela
 */
public class BasicStrategy {

    /**
     * @private @property HashMap<String, Play> _valuePlays - All plays that are
     * based on total value
     */
    private final HashMap<String, Play> _valuePlays;
    /**
     * @private @property HashMap<String, Play> _acePlays - All plays that
     * contain an Ace
     */
    private final HashMap<String, Play> _acePlays;
    /**
     * @private @property HashMap<String, Play> _pairPlays - All plays that are
     * pairs
     */
    private final HashMap<String, Play> _pairPlays;

    /**
     * BasicStrategy Constructor
     */
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

        Play advisedPlay;
        String dealer;
        String handCard1;
        String handCard2;
        String hand;
        String key;
        int handValue;
        dealer = upCard.getName();

        // Check for J,K,Q upcards and assign correct values
        switch (dealer) {
            case "J":
                dealer = "10";
                break;
            case "K":
                dealer = "10";
                break;
            case "Q":
                dealer = "10";
                break;
        }

        // check for aces, pairs & value plays
        if (myHand.size() == 2) {
            handCard1 = myHand.getCard(0).getName();
            handCard2 = myHand.getCard(1).getName();
            hand = this._isNumeric(handCard1) ? handCard2 + "," + handCard1 : handCard1 + "," + handCard2;

            key = hand + "|" + dealer;

            // check for pair
            if (myHand.isPair() && handCard1.equals(handCard2)) {
                advisedPlay = this._pairPlays.get(key);
            } // check for ace
            else if (this._containsAce(hand)) {
                advisedPlay = this._acePlays.get(key);
            } // Otherwise use value plays
            else {
                handValue = (Integer) myHand.getValue();
                hand = handValue < 17 ? Integer.toString(handValue) : "17+";
                key = hand + "|" + dealer;
                advisedPlay = this._valuePlays.get(key);
            }

        } // Use value plays 
        else {
            handValue = myHand.getValue();
            hand = handValue < 17 ? Integer.toString(handValue) : "17+";
            key = hand + "|" + dealer;

            advisedPlay = this._valuePlays.get(key);
        }

        // Debugging purposes
//        System.out.println(key + " <============ This is the key");
        return advisedPlay;
    }

    /**
     * Gets the dealer upCards
     *
     * @public
     * @method getDealerUpCards
     * @return dealerUpCards
     */
    public String[] getDealerUpCards() {

        int numUpCards = 13;
        String[] dealerUpCards = new String[numUpCards];

        // populate the upcards
        for (int i = 0; i < numUpCards; i++) {
            String value = (i + 2) < 11 ? Integer.toString(i + 2) : "A";
            value = (i + 2) == 12 ? "J" : value;
            value = (i + 2) == 13 ? "K" : value;
            value = (i + 2) == 14 ? "Q" : value;
            dealerUpCards[i] = value;
        }

        return dealerUpCards;
    }

    /**
     * Checks whether a string is numeric
     *
     * @private
     * @method _isNumeric
     * @param s
     * @return boolean
     */
    private boolean _isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    /**
     * checks if a string representation of our hand contains an Ace
     *
     * @private
     * @method _containsAce
     * @param hand
     * @return boolean
     */
    private boolean _containsAce(String hand) {
        return hand.contains("A");
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
            } else if (card.equals("J") || card.equals("K") || card.equals("Q")) {
                val = 10;
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

                String hand = Integer.toString(value);
                hand = (value == 17 ? "17+" : hand);
                String dCard = upCards[dealer - 2];
                String key = hand + "|" + dCard;

                //============== Hit ===========================================
                // Hand: 5-8 and Dealer: 2-10, A
                if (value >= 5 && value <= 8) {
                    this._valuePlays.put(key, Play.HIT);
                } // Hand: 9 and Dealer: 2, 7-10, A
                else if (value == 9) {
                    if (dealer <= 2 || dealer >= 7) {
                        this._valuePlays.put(key, Play.HIT);
                    }
                } // Hand: 10 and Dealer: 10, A
                else if (value == 10 && dealer >= 10) {
                    this._valuePlays.put(key, Play.HIT);
                } // Hand: 11 and Dealer: 11
                else if (value == 11) {
                    if (dealer == 11) {
                        this._valuePlays.put(key, Play.HIT);
                    }
                } // Hand: 12 - 16 and Dealer: 2-3
                else if (value >= 12 && value <= 16) {
                    if (value == 12) {
                        if (dealer >= 2 && dealer <= 3) {
                            this._valuePlays.put(key, Play.HIT);
                        } else if (dealer >= 7 && dealer <= 11) {
                            this._valuePlays.put(key, Play.HIT);
                        }
                    } else if (dealer >= 7 && dealer <= 11 && value > 12) {
                        this._valuePlays.put(key, Play.HIT);
                    }
                }

                //============== Stand =========================================
                // Hand: 17 and Dealer: 2-10,A
                if (value == 17) {
                    System.out.println(key);
                    this._valuePlays.put(key, Play.STAY);
                } // Hand: 12-16 and Dealer: 2-6, 
                else if (value >= 12 && value <= 16) {

                    if (value == 12 && dealer >= 4 && dealer <= 6) {
                        this._valuePlays.put(key, Play.STAY);
                    } else if (value > 12 && dealer >= 2 && dealer <= 6) {
                        this._valuePlays.put(key, Play.STAY);
                    }

                }

                //============== Double Down ===================================
                // Hand: 9 and Dealer: 3 - 6
                if (value == 9 && dealer >= 3 && dealer <= 6) {
                    this._valuePlays.put(key, Play.DOUBLE_DOWN);
                } // Hand: 10 and Dealer: 2 - 9
                else if (value == 10 && dealer >= 2 && dealer <= 9) {
                    this._valuePlays.put(key, Play.DOUBLE_DOWN);
                } // Hand: 11 and Dealer: 2 - 10
                else if (value == 11 && dealer >= 2 && dealer <= 10) {
                    this._valuePlays.put(key, Play.DOUBLE_DOWN);
                }

                //============== Split =========================================
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

            int firstCard = 2;
            int lastCard = 10;

            // loop through the card ranks
            for (int rank = firstCard; rank <= lastCard; rank++) {
                String hand = "A," + rank;
                String dCard = upCards[dealer - 2];
                String key = hand + "|" + dCard;

                //============== Hit ===========================================
                // Hand: A,2-6 and Dealer: 2
                if (dealer == 2 && rank >= 2 && rank <= 6) {
                    this._acePlays.put(key, Play.HIT);
                } // Hand: A,2-5 and Dealer 3
                else if (dealer == 3 && rank >= 2 && rank <= 5) {
                    this._acePlays.put(key, Play.HIT);
                } // Hand: A,2-3 and Dealer: 4
                else if (dealer == 4 && rank >= 2 && rank <= 3) {
                    this._acePlays.put(key, Play.HIT);
                } // Hand: A,2-6 and Dealer: 7-10 or A
                else if (dealer >= 7 && dealer <= 11 && rank >= 2 && rank <= 6) {
                    this._acePlays.put(key, Play.HIT);
                } // Hand: A,7 and Dealer: 9-10 or A
                else if (dealer >= 9 && dealer <= 11 && rank == 7) {
                    this._acePlays.put(key, Play.HIT);
                }

                //============== Stand =========================================
                // Hand: A,8-10 and Dealer: 2-10 or A
                if (rank >= 8 && rank <= 10) {
                    this._acePlays.put(key, Play.STAY);
                } // Hand: A,7 and Dealer: 2,7,8
                else if (rank == 7 && (dealer == 2 || dealer == 7 || dealer == 8)) {
                    this._acePlays.put(key, Play.STAY);
                }

                //============== Double Down ===================================
                // Hand: A, 6-7 and Dealer: 3-6
                if (rank >= 6 && rank <= 7 && dealer >= 3 && dealer <= 6) {
                    this._acePlays.put(key, Play.DOUBLE_DOWN);
                } // Hand: A, 4-5 and Dealer: 4-6
                else if (rank >= 4 && rank <= 5 && dealer >= 4 && dealer <= 6) {
                    this._acePlays.put(key, Play.DOUBLE_DOWN);
                } // Hand: A, 2-3 and Dealer: 5-6
                else if (rank >= 2 && rank <= 3 && dealer >= 5 && dealer <= 6) {
                    this._acePlays.put(key, Play.DOUBLE_DOWN);
                }
                //============== Split =========================================
                // None
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

            int firstCard = 2;
            int lastCard = 10;

            // loop through the values
            for (int rank = firstCard; rank <= lastCard; rank++) {

                String hand = Integer.toString(rank) + "," + Integer.toString(rank);
                String dCard = upCards[dealer - 2];
                String key = hand + "|" + dCard;

                //============== Hit ===========================================
                // Hand: 4,4 and Dealer: 2-4
                if (dealer >= 2 && dealer <= 4 && rank == 4) {
                    this._pairPlays.put(key, Play.HIT);
                } // Hand: 4,4 or 6,6 and Dealer: 7-10 or A
                else if (dealer == 7 && (rank == 4 || rank == 6)) {
                    this._pairPlays.put(key, Play.HIT);
                } // Hand: pairs of (2-7) and Dealer: 8-10 or A
                else if (dealer >= 8 && dealer <= 11 && rank >= 2 && rank <= 7) {
                    if (rank != 5 && dealer != 8 && dealer != 9) {
                        this._pairPlays.put(key, Play.HIT);
                    }
                }

                //============== Stand =========================================
                // Hand: 10,10 and Dealer: 2-10 or A
                if (rank == 10) {
                    this._pairPlays.put(key, Play.STAY);
                } // Hand: 9,9 and Dealer: 7,10,11
                else if (rank == 9 && (dealer == 7 || dealer == 10 || dealer == 11)) {
                    this._pairPlays.put(key, Play.STAY);
                }

                //============== Double Down ===================================
                // Hand: 5,5 and Dealer: 2-9
                if (rank == 5 && dealer >= 2 && dealer <= 9) {
                    this._pairPlays.put(key, Play.DOUBLE_DOWN);
                }

                //============== Split =========================================
                // Hand: pairs of (6-9) and Dealer: 2- 6
                if (rank >= 6 && rank <= 9 && dealer >= 2 && dealer <= 6) {
                    this._pairPlays.put(key, Play.SPLIT);
                } // Hand: 7,7 and Dealer: 7
                else if (rank == 7 && dealer == 7) {
                    this._pairPlays.put(key, Play.SPLIT);
                } // Hand: 9,9 and Dealer: 8-9
                else if (rank == 9 && dealer >= 8 && dealer <= 9) {
                    this._pairPlays.put(key, Play.SPLIT);
                } // Hand: 4,4 and Dealer: 5-6
                else if (rank == 4 && dealer >= 5 && dealer <= 6) {
                    this._pairPlays.put(key, Play.SPLIT);
                } // Hand: pairs of (2-3) and Dealer: 2-7
                else if (rank >= 2 && rank <= 3 && dealer >= 2 && dealer <= 7) {
                    this._pairPlays.put(key, Play.SPLIT);
                } // Hand: A,A or 8,8
                else if (rank == 8 || rank == 11) {
                    this._acePlays.put(key, Play.SPLIT);
                }
            }
        }
    }

}
