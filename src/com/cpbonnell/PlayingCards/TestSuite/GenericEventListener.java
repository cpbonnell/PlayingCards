package com.cpbonnell.PlayingCards.TestSuite;

import com.cpbonnell.PlayingCards.DeckEvents.*;
import com.cpbonnell.PlayingCards.IPlayingDeck;

/**
 * This class listens for the various deck events, and registers any time they are called.
 */
public class GenericEventListener implements ICardDiscardedListener, ICardDrawnListener, IDeckShuffledListener,
    IDiscardDrawnListener, IInvalidDiscardListener{
    
    
    
    
    
    
    @Override
    public void cardDiscardedEventHandler(IPlayingDeck d) {
        
    }

    @Override
    public void cardDrawnEventHandler(IPlayingDeck d) {

    }

    @Override
    public void deckShuffledEventHandler(IPlayingDeck d) {

    }

    @Override
    public void discardDrawnEventHandler(IPlayingDeck d) {

    }

    @Override
    public void invalidDiscardEventHandler(IPlayingDeck d) {

    }
}
