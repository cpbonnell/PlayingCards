package com.cpbonnell.PlayingCards;

import com.cpbonnell.PlayingCards.DeckEvents.*;

/**
 * This mock object is used for testing the playing deck and event registrar classes.
 */
class VocalMockDeckListener implements ICardDiscardedListener, ICardDrawnListener, IDeckShuffledListener, 
        IDiscardDrawnListener, IInvalidDiscardListener{
    
    String objName;
    
    public VocalMockDeckListener(String name){
        this.objName = name;
    }


    @Override
    public void cardDiscardedEventHandler(IPlayingDeck d) {
        System.out.println("-Card Discarded- event handled by " + this.objName);
    }

    @Override
    public void cardDrawnEventHandler(IPlayingDeck d) {
        System.out.println("-Card Drawn- event handled by " + this.objName);
    }

    @Override
    public void deckShuffledEventHandler(IPlayingDeck d) {
        System.out.println("-Deck Shuffled- event handled by " + this.objName);
    }

    @Override
    public void discardDrawnEventHandler(IPlayingDeck d) {
        System.out.println("-Discard Drawn- event handled by " + this.objName);
    }

    @Override
    public void invalidDiscardEventHandler(IPlayingDeck d) {
        System.out.println("-Invalid Discard- event handled by " + this.objName);
    }
}
