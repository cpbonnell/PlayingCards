package com.cpbonnell.PlayingCards;

import com.cpbonnell.PlayingCards.DeckEvents.*;

/**
 * Created by christian_bonnell on 6/19/2015.
 */
public interface IDeckEventManager {
    //==================== Functions for the CardDrawn Event ====================
    void addCardDrawnListener(ICardDrawnListener listener);

    void removeCardDrawnListener(ICardDrawnListener listener);

    //==================== Functions for the DiscardDrawn Event ====================
    void addDiscardDrawnListener(IDiscardDrawnListener listener);

    void removeDiscardDrawnListener(IDiscardDrawnListener listener);

    //==================== Functions for the CardDiscarded Event ====================
    void addCardDiscardedListener(ICardDiscardedListener listener);

    void removeCardDiscardedListener(ICardDiscardedListener listener);

    //==================== Functions for the Deck Shuffled Event ====================
    void addDeckShuffledListener(IDeckShuffledListener listener);

    void removeDeckShuffledListener(IDeckShuffledListener listener);

    //==================== Functions for the Invalid Discard Event ====================
    void addInvalidDiscardListener(IInvalidDiscardListener listener);

    void removeInvalidDiscardListener(IInvalidDiscardListener listener);
}
