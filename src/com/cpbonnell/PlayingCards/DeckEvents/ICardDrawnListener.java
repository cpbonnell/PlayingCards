package com.cpbonnell.PlayingCards.DeckEvents;

import com.cpbonnell.PlayingCards.IPlayingDeck;

/**
 * Functional interface for classes that wish to be informed when a card is drawn.
 */
public interface ICardDrawnListener {
    public void CardDrawnEventHandler(IPlayingDeck d);
}
