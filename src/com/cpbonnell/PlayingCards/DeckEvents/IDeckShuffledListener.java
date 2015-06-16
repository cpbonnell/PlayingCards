package com.cpbonnell.PlayingCards.DeckEvents;

import com.cpbonnell.PlayingCards.IPlayingDeck;

/**
 * Functional interface for classes that wish to be informed when the deck is shuffled.
 */
public interface IDeckShuffledListener {
    public void DeckShuffledEventListener(IPlayingDeck d);
}
