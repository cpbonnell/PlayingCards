package com.cpbonnell.PlayingCards;

import com.cpbonnell.PlayingCards.DeckEvents.*;

/**
 * Created by christian_bonnell on 6/19/2015.
 */
public interface IDeckEventRegistrar {
    //==================== Functions for the CardDrawn Event ====================
    int addCardDrawnListener(ICardDrawnListener listener);

    boolean removeCardDrawnListener(ICardDrawnListener listener);
    boolean removeCardDrawnListener(int listenerHash);

    //==================== Functions for the DiscardDrawn Event ====================
    int addDiscardDrawnListener(IDiscardDrawnListener listener);

    boolean removeDiscardDrawnListener(IDiscardDrawnListener listener);
    boolean removeDiscardDrawnListener(int listenerHash);

    //==================== Functions for the CardDiscarded Event ====================
    int addCardDiscardedListener(ICardDiscardedListener listener);

    boolean removeCardDiscardedListener(ICardDiscardedListener listener);
    boolean removeCardDiscardedListener(int listenerHash);

    //==================== Functions for the Deck Shuffled Event ====================
    int addDeckShuffledListener(IDeckShuffledListener listener);

    boolean removeDeckShuffledListener(IDeckShuffledListener listener);
    boolean removeDeckShuffledListener(int listenerHash);

    //==================== Functions for the Invalid Discard Event ====================
    int addInvalidDiscardListener(IInvalidDiscardListener listener);

    boolean removeInvalidDiscardListener(IInvalidDiscardListener listener);
    boolean removeInvalidDiscardListener(int listenerHash);
}
