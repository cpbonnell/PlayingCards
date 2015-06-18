package com.cpbonnell.PlayingCards;

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
    
    private static final IPlayingCard BLANK_CARD = new BasePlayingCard(null, null);
    private IPlayingCard referencedCard;
    
    public SecurePlayingCard(IPlayingCard c){
        this.referencedCard = c;
    }


    @Override
    public Ranks rank() {
        return referencedCard.rank();
    }

    @Override
    public Suits suit() {
        return referencedCard.suit();
    }

    @Override
    public boolean matches(IPlayingCard c) {
        return this.referencedCard.matches(c);
    }

    public void invalidate(){
        this.referencedCard = SecurePlayingCard.BLANK_CARD;
    }
    
    public boolean pointsAt(IPlayingCard c){
        return this.referencedCard == c && this.isValid();
    }
    
    public boolean isValid(){
        return this.referencedCard != BLANK_CARD;
    }
    
}
