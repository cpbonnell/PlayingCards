package com.cpbonnell.PlayingCards;

import com.cpbonnell.PlayingCards.DeckEvents.*;

import java.util.*;

/**
 * Class to manage the parent deck's relations with various event listeners.
 * 
 * <p>
 *     This is a helper class for the classes implementing the IPlayingDeck
 *     interface. It maintains a list of objects that wish to be notified of
 *     various events performed by or on the deck. The parent deck class
 *     only needs to contain an instance of this class, and expose it with
 *     a simple getter function. The parent deck can then raise the events
 *     in this class without worrying about maintaining the lists of listeners.
 * </p>
 */
public class BaseDeckWatcher {
    
    // Lists of objects listening for events...
    List<ICardDrawnListener> cardDrawnListeners = new ArrayList<>();
    List<ICardDiscardedListener> cardDiscardedListeners = new ArrayList<>();
    List<IDeckShuffledListener> deckShuffledListeners = new ArrayList<>();
    List<IInvalidDiscardListener> invalidDiscardListeners = new ArrayList<>();
    
    
    String preEvent;
    String postEvent;
    IEventCriticalSection entryCriticalSection;
    IEventCriticalSection exitCriticalSection;
    
    public BaseDeckWatcher(IEventCriticalSection entrySection, IEventCriticalSection exitSection){
        
        // If no callback is supplied for the critical sections, we supply a default
        // method in the form of a lambda that does nothing. This makes later code
        // much simpler, since it can call the entry section and exit section
        // wiothout having to first check for null values.
        if(entrySection == null){
            this.entryCriticalSection = () -> {return;};
        } else {
            this.entryCriticalSection = entrySection;
        }
        
        if(exitSection == null){
            this.exitCriticalSection = () ->{return;};
        } else {
            this.exitCriticalSection = exitSection;
        }
    }
    
    
    //==================== Functions for the CardDrawn Event ====================
    public void addCardDrawnListener(ICardDrawnListener listener){
        if( ! cardDrawnListeners.contains(listener) ){
            cardDrawnListeners.add(listener);
        }
    }
    
    public void removeCardDrawnListener(ICardDrawnListener listener){
        if(cardDrawnListeners.contains(listener)){
            cardDrawnListeners.remove(listener);
        }
    }
    
    public void onCardDrawn(IPlayingDeck d){
        if( ! cardDrawnListeners.isEmpty() ){
            this.entryCriticalSection.criticalSection();
            cardDrawnListeners.stream().forEach(o -> o.CardDrawnEventHandler(d));
            this.exitCriticalSection.criticalSection();
        }
    }
    
    
    //==================== Functions for the CardDiscarded Event ====================
    public void addCardDiscardedListener(ICardDiscardedListener listener){
        if( ! cardDiscardedListeners.contains(listener) ){
            cardDiscardedListeners.add(listener);
        }
    }

    public void removeCardDiscardedListener(ICardDiscardedListener listener){
        if(cardDiscardedListeners.contains(listener)){
            cardDiscardedListeners.remove(listener);
        }
    }

    public void onCardDiscarded(IPlayingDeck d){
        if( ! cardDiscardedListeners.isEmpty() ){
            this.entryCriticalSection.criticalSection();
            cardDiscardedListeners.stream().forEach(o -> o.CardDiscardedEventListener(d));
            this.exitCriticalSection.criticalSection();
        }
    }

    //==================== Functions for the Deck Shuffled Event ====================
    public void addDeckShuffledListener(IDeckShuffledListener listener){
        if( ! deckShuffledListeners.contains(listener) ){
            deckShuffledListeners.add(listener);
        }
    }

    public void removeDeckShuffledListener(IDeckShuffledListener listener){
        if(deckShuffledListeners.contains(listener)){
            deckShuffledListeners.remove(listener);
        }
    }

    public void onDeckShuffled(IPlayingDeck d){
        if( ! deckShuffledListeners.isEmpty() ){
            this.entryCriticalSection.criticalSection();
            deckShuffledListeners.stream().forEach(o -> o.DeckShuffledEventListener(d));
            this.exitCriticalSection.criticalSection();
        }
    }

    //==================== Functions for the Invalid Discard Event ====================
    public void addInvalidDiscardListener(IInvalidDiscardListener listener){
        if( ! invalidDiscardListeners.contains(listener) ){
            invalidDiscardListeners.add(listener);
        }
    }

    public void removeInvalidDiscardListener(IInvalidDiscardListener listener){
        if(invalidDiscardListeners.contains(listener)){
            invalidDiscardListeners.remove(listener);
        }
    }

    public void onInvalidDiscard(IPlayingDeck d){
        if( ! invalidDiscardListeners.isEmpty() ){
            this.entryCriticalSection.criticalSection();
            invalidDiscardListeners.stream().forEach(o -> o.InvalidDiscardEventHandler(d));
            this.exitCriticalSection.criticalSection();
        }
    }
    
}
