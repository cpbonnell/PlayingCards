package com.cpbonnell.cards.PlayingCards;

/**
 * Created by christian_bonnell on 6/16/2015.
 */
public interface IPlayingDeck {

    IDeckEventRegistrar getEventManager();


    // Methods for shuffling and looking at the top of the discard pile.
    
    void shuffle();

    void shuffle(int leaveTopDiscards);

    Ranks viewDiscardRank();

    Suits viewDiscardSuit();

    
    // Methods to look at the size of the various parts of the deck,
    // and validate outstanding card objects.
    
    int drawPileSize();
    
    int discardPileSize();
    
    int outstandingSize();
    
    int totalSize();
    
    boolean validateOutstandingCard(IPlayingCard c);
    
    
    // Methods for drawing and discarding
    
    IPlayingCard drawCard();
    
    IPlayingCard drawDiscard();
    
    boolean discardCard(IPlayingCard c);
}
