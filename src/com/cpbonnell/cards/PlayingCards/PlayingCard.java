package com.cpbonnell.cards.PlayingCards;

/**
 * Provides a representation of a basic playing card.
 *
 */
public class PlayingCard implements IPlayingCard {

    private Ranks rank;
    private Suits suit;

    /**
     * Instantiate a PlayingCard object with the given values.
     * @param r A Ranks value
     * @param s A Suits value
     */
    public PlayingCard(Ranks r, Suits s){

        if(r != null && s != null){
            this.rank = r;
            this.suit = s;
        } else {
            this.rank = null;
            this.suit = null;
        }
        
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

    @Override
    public String toChars() {
        return this.rank.toChars() + this.suit.toChars();
    }
    
    @Override
    public int compareTo(final IPlayingCard o) {
        return this.rank.compareTo(o.rank());
    }
}
