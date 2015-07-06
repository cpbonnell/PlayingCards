package com.cpbonnell.PlayingCards;

import com.cpbonnell.PlayingCards.DeckEvents.*;

/**
 * Public interface for subscribing and unsubscribing to deck events.
 * <p>
 *     This interface exposes one addXxxListener method for each of the five
 *     deck events, as well as two removeXxxxListener methods. These methods
 *     provide for two methods of subscribing / unsubscribing for methods. For
 *     more information, see the documentation for the BaseDeckEventCaller
 *     class.
 * </p>
 */
public interface IDeckEventRegistrar {
    //==================== Functions for the CardDrawn Event ====================
    int addCardDrawnListener(IDeckEventListener listener);

    //boolean removeCardDrawnListener(ICardDrawnListener listener);
    boolean removeCardDrawnListener(int listenerHash);

    //==================== Functions for the DiscardDrawn Event ====================
    int addDiscardDrawnListener(IDeckEventListener listener);

    //boolean removeDiscardDrawnListener(IDiscardDrawnListener listener);
    boolean removeDiscardDrawnListener(int listenerHash);

    //==================== Functions for the CardDiscarded Event ====================
    int addCardDiscardedListener(IDeckEventListener listener);

    //boolean removeCardDiscardedListener(ICardDiscardedListener listener);
    boolean removeCardDiscardedListener(int listenerHash);

    //==================== Functions for the Deck Shuffled Event ====================
    int addDeckShuffledListener(IDeckEventListener listener);

    //boolean removeDeckShuffledListener(IDeckShuffledListener listener);
    boolean removeDeckShuffledListener(int listenerHash);

    //==================== Functions for the Invalid Discard Event ====================
    int addInvalidDiscardListener(IDeckEventListener listener);

    //boolean removeInvalidDiscardListener(IInvalidDiscardListener listener);
    boolean removeInvalidDiscardListener(int listenerHash);
}
