package com.cpbonnell.cards.PlayingCards;

/**
 * Provides a safely distributable facade pointing to another IPlayingCard.
 * 
 * This class provides security for the deck objects by pointing to individual
 * cards in the deck and reflecting those values to consumers outside the deck.
 * Once a user has "returned" a card, or the reference is no longer needed, the
 * invalidate method may be invoked, permanently severing the link to the valued
 * card object, and making this object appear to be a blank card.
 * 
 * This class provides additional security to the deck object by allowing it to
 * verify that it is indeed referencing a valid card that belongs to the deck.
 * This prevents spoofing, where an agent might instantiate a card with a
 * desired value (the "Ace Up The Sleeve" trick), or discard a blank card
 * rather than one legitimately issued from the deck.
 *
 */
class SecurePlayingCard implements IPlayingCard {
    
    private static final IPlayingCard BLANK_CARD = new PlayingCard(null, null);
    private IPlayingCard referencedCard;

    /**
     * Instantiate a SecurePlayingCard object pointing to the specified parameter.
     * @param c An object implementing IPlayingCard.
     */
    public SecurePlayingCard(IPlayingCard c){
        this.referencedCard = c;
    }


    /**
     * Access the rank of the object's face value.
     * @return The rank of this card.
     */
    @Override
    public Ranks rank() {
        return referencedCard.rank();
    }

    /**
     * Access the suit of the object's face value.
     * @return The suit of this card.
     */
    @Override
    public Suits suit() {
        return referencedCard.suit();
    }

    /**
     * Determines if the face value of the parameter matches the face value of this object.
     * @param c Another object implementing IPlayingCard
     * @return Returns true if this.rank() == c.rank() && this.suit() == c.suit()
     */
    @Override
    public boolean matches(IPlayingCard c) {
        return this.referencedCard.matches(c);
    }

    @Override
    public String toChars() {
        return this.referencedCard.toChars();
    }

    /**
     * Permanently breaks the objects reference, replacing it with a blank card.
     */
    public void invalidate(){
        this.referencedCard = SecurePlayingCard.BLANK_CARD;
    }

    /**
     * Determines the object that is referenced by this object.
     * @param c An object implementing IPlayingCard.
     * @return Returns true if this object references the parameter, false otherwise.
     */
    public boolean pointsAt(IPlayingCard c){
        return this.referencedCard == c && this.isValid();
    }

    /**
     * Determines if this object still maintains a valid reference.
     * @return Returns true if the reference is still valid, and false if it references blank card.
     */
    public boolean isValid(){
        return this.referencedCard != BLANK_CARD;
    }


    @Override
    public int compareTo(final IPlayingCard o) {
        return this.referencedCard.compareTo(o);
    }
}
