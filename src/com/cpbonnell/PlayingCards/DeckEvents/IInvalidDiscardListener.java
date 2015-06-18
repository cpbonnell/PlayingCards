package com.cpbonnell.PlayingCards.DeckEvents;

import com.cpbonnell.PlayingCards.IPlayingDeck;

/**
 * Created by christian_bonnell on 6/18/2015.
 */
public interface IInvalidDiscardListener {
    public void InvalidDiscardEventHandler(IPlayingDeck d);
}
