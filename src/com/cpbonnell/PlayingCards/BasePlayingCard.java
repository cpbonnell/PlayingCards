package com.cpbonnell.PlayingCards;

import com.sun.javaws.exceptions.InvalidArgumentException;

/**
 * Provides a representation of a basic playing card.
 *
 */
class BasePlayingCard implements IPlayingCard {

    private Ranks rank;
    private Suits suit;

    /**
     * Instantiate a BasePlayingCard object with the given values.
     * @param r A Ranks value
     * @param s A Suits value
     */
    public BasePlayingCard(Ranks r, Suits s){

        if(r != null && s != null){
            this.rank = r;
            this.suit = s;
        } else {
            this.rank = null;
            this.suit = null;
        }

        //throw new InvalidArgumentException();
    }

    /**
     * Access the rank of the object's face value.
     * @return The rank of this card.
     */
    @Override
    public Ranks rank() {
        return this.rank;
    }

    /**
     * Access the suit of the object's face value.
     * @return The suit of this card.
     */
    @Override
    public Suits suit() {
        return this.suit;
    }

    /**
     * Determines if the face value of the parameter matches the face value of this object.
     * @param c Another object implementing IPlayingCard
     * @return Returns true if this.rank() == c.rank() && this.suit() == c.suit()
     */
    @Override
    public boolean matches(IPlayingCard c) {
        return this.rank == c.rank() && this.suit == c.suit();
    }

}
