package com.cpbonnell.PlayingCards;


import java.util.function.Consumer;

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
    int addCardDrawnListener(Consumer<IPlayingDeck> listener);
    
    boolean removeCardDrawnListener(int listenerHash);

    //==================== Functions for the DiscardDrawn Event ====================
    int addDiscardDrawnListener(Consumer<IPlayingDeck> listener);
    
    boolean removeDiscardDrawnListener(int listenerHash);

    //==================== Functions for the CardDiscarded Event ====================
    int addCardDiscardedListener(Consumer<IPlayingDeck> listener);
    
    boolean removeCardDiscardedListener(int listenerHash);

    //==================== Functions for the Deck Shuffled Event ====================
    int addDeckShuffledListener(Consumer<IPlayingDeck> listener);
    
    boolean removeDeckShuffledListener(int listenerHash);

    //==================== Functions for the Invalid Discard Event ====================
    int addInvalidDiscardListener(Consumer<IPlayingDeck> listener);
    
    boolean removeInvalidDiscardListener(int listenerHash);
}
