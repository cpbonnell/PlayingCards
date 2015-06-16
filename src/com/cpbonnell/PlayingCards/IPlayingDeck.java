package com.cpbonnell.PlayingCards;

/**
 * Created by christian_bonnell on 6/16/2015.
 */
public interface IPlayingDeck {


    void shuffle();

    void shuffle(int leaveTopDiscards);

    Ranks viewDiscardRank();

    Suits viewDiscardSuit();

    int deckSize();
    
}
