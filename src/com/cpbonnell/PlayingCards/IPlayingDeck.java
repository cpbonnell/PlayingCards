package com.cpbonnell.PlayingCards;

/**
 * Created by christian_bonnell on 6/16/2015.
 */
public interface IPlayingDeck {


    // Methods for shuffling and looking at the top of the discard pile.
    
    void shuffle();

    void shuffle(int leaveTopDiscards);

    Ranks viewDiscardRank();

    Suits viewDiscardSuit();

    
    // Methods to look at the size of the various parts of the deck.
    
    int drawPileSize();
    
    int discardPileSize();
    
    int outstandingSize();
    
    int totalSize();
    
    
    // Methods for drawing and discarding
    
    IPlayingCard drawCard();
    
    IPlayingCard drawDiscard();
    
    boolean discardCard(IPlayingCard c);
}
