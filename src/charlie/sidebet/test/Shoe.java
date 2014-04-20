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
package charlie.sidebet.test;

import charlie.card.Card;

/**
 *
 * @author Marist User
 */
public class Shoe extends charlie.card.Shoe {

    @Override
    public void init() {
        cards.clear();
        //First hand: 7 9 3 K 9
        cards.add(new Card(7, Card.Suit.DIAMONDS));
        cards.add(new Card(9, Card.Suit.SPADES));
        cards.add(new Card(3, Card.Suit.HEARTS));
        cards.add(new Card(Card.KING, Card.Suit.CLUBS));
        cards.add(new Card(9, Card.Suit.DIAMONDS));

        //Second hand: 7 k 9 8 3
        cards.add(new Card(7, Card.Suit.CLUBS));
        cards.add(new Card(Card.KING, Card.Suit.CLUBS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(8, Card.Suit.HEARTS));
        cards.add(new Card(3, Card.Suit.SPADES));

        //Third hand: 9 k 7 8 3
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(Card.KING, Card.Suit.DIAMONDS));
        cards.add(new Card(7, Card.Suit.SPADES));
        cards.add(new Card(8, Card.Suit.HEARTS));
        cards.add(new Card(3, Card.Suit.CLUBS));

        //Fourth hand: 7 k 9 10 3
        cards.add(new Card(7, Card.Suit.CLUBS));
        cards.add(new Card(Card.KING, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.SPADES));
        cards.add(new Card(10, Card.Suit.DIAMONDS));
        cards.add(new Card(3, Card.Suit.CLUBS));

        //Fifth hand: 9 k 7 10 3
        cards.add(new Card(9, Card.Suit.HEARTS));
        cards.add(new Card(Card.KING, Card.Suit.CLUBS));
        cards.add(new Card(7, Card.Suit.DIAMONDS));
        cards.add(new Card(10, Card.Suit.HEARTS));
        cards.add(new Card(3, Card.Suit.SPADES));

        //Sixth hand: K (suited) K Q(suited) 8
        cards.add(new Card(Card.KING, Card.Suit.CLUBS));
        cards.add(new Card(Card.KING, Card.Suit.SPADES));
        cards.add(new Card(Card.QUEEN, Card.Suit.CLUBS));
        cards.add(new Card(8, Card.Suit.DIAMONDS));

        //Seventh hand: K(unsuited) K Q(unsuited) 8
        cards.add(new Card(Card.KING, Card.Suit.SPADES));
        cards.add(new Card(Card.KING, Card.Suit.CLUBS));
        cards.add(new Card(Card.QUEEN, Card.Suit.DIAMONDS));
        cards.add(new Card(8, Card.Suit.HEARTS));

        //Eighth hand: 8 k 5 6 k
        cards.add(new Card(8, Card.Suit.HEARTS));
        cards.add(new Card(Card.KING, Card.Suit.CLUBS));
        cards.add(new Card(5, Card.Suit.SPADES));
        cards.add(new Card(6, Card.Suit.DIAMONDS));
        cards.add(new Card(Card.KING, Card.Suit.HEARTS));

        //Ninth hand: 7 K 6 6 K
        cards.add(new Card(7, Card.Suit.SPADES));
        cards.add(new Card(Card.KING, Card.Suit.CLUBS));
        cards.add(new Card(6, Card.Suit.CLUBS));
        cards.add(new Card(6, Card.Suit.DIAMONDS));
        cards.add(new Card(Card.KING, Card.Suit.HEARTS));

        //Tenth hand: 6 K 8 6 K
        cards.add(new Card(6, Card.Suit.SPADES));
        cards.add(new Card(Card.KING, Card.Suit.DIAMONDS));
        cards.add(new Card(8, Card.Suit.SPADES));
        cards.add(new Card(6, Card.Suit.HEARTS));
        cards.add(new Card(Card.KING, Card.Suit.CLUBS));
    }

    @Override
    public boolean shuffleNeeded() {
        return false;
    }
}
