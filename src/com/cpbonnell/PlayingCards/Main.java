package com.cpbonnell.PlayingCards;

import java.util.*;

public class Main {

    public static void main(String[] args) {
	// write your code here
        
        // Instantiate some mock objects to monitor the deck
        VocalMockDeckListener classref = new VocalMockDeckListener("classref Listener");
        VocalMockDeckListener funref = new VocalMockDeckListener("funref Listener");
        
        // Instantiate a new deck
        IPlayingDeck deck = BasePlayingDeck.newStandardFrenchDeck();
        
        // Register with the deck via class reference
        deck.getEventManager().addCardDiscardedListener(classref);
        deck.getEventManager().addCardDrawnListener(classref);
        deck.getEventManager().addDeckShuffledListener(classref);
        deck.getEventManager().addDiscardDrawnListener(classref);
        deck.getEventManager().addInvalidDiscardListener(classref);
        
        // Register with the deck via function reference
        int id1 = deck.getEventManager().addCardDiscardedListener(funref::cardDiscardedEventHandler);
        int id2 = deck.getEventManager().addCardDrawnListener(funref::cardDrawnEventHandler);
        int id3 = deck.getEventManager().addDeckShuffledListener(funref::deckShuffledEventHandler);
        int id4 = deck.getEventManager().addDiscardDrawnListener(funref::discardDrawnEventHandler);
        int id5 = deck.getEventManager().addInvalidDiscardListener(funref::invalidDiscardEventHandler);
        
        // Unsubscribe each object from a couple of events to make sure that functionality works
        deck.getEventManager().removeCardDrawnListener(id2);
        deck.getEventManager().removeDeckShuffledListener(id3);
        deck.getEventManager().removeCardDiscardedListener(classref);
        deck.getEventManager().removeInvalidDiscardListener(classref);
        
        // Now test the deck by raising events one-by-one
        IPlayingCard goodCard = deck.drawCard();
        IPlayingCard anotherCard = deck.drawCard();
        deck.discardCard(goodCard);
        deck.shuffle();
        deck.discardCard(anotherCard);
        goodCard = deck.drawDiscard();
        
        
        
        System.out.println("Execution complete.");
        
        
    }
}
