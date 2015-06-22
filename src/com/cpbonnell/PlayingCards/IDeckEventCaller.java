package com.cpbonnell.PlayingCards;

/**
 * Created by christian_bonnell on 6/19/2015.
 */
interface IDeckEventCaller extends IDeckEventRegistrar {


    void onCardDrawn(IPlayingDeck d);

    void onDiscardDrawn(IPlayingDeck d);

    void onCardDiscarded(IPlayingDeck d);

    void onDeckShuffled(IPlayingDeck d);

    void onInvalidDiscard(IPlayingDeck d);
}
