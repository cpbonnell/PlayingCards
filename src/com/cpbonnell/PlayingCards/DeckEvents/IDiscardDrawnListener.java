package com.cpbonnell.PlayingCards.DeckEvents;

import com.cpbonnell.PlayingCards.IPlayingDeck;

/**
 * Functional interface for classes wishing to respond to a Discard Drawn event.
 * <p>
 *     The Discard Drawn event occurs when the top card on the discard pile is
 *     removed. 
 * </p>
 */
public interface IDiscardDrawnListener {
    public void discardDrawnEventHandler(IPlayingDeck d);
}
