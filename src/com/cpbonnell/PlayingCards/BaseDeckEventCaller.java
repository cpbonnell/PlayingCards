package com.cpbonnell.PlayingCards;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Class to manage the parent deck's relations with various event listeners.
 * 
 * <p>
 *     This is a helper class for the classes implementing the IPlayingDeck
 *     interface. It maintains a list of objects that wish to be notified of
 *     various events performed by or on the deck. The parent deck class
 *     only needs to contain an instance of this class, and expose its add 
 *     and remove functionality via a simple getter function. The parent deck
 *     can then raise the events in this class without worrying about 
 *     maintaining the lists of listeners.
 * </p>
 */
class BaseDeckEventCaller implements IDeckEventCaller {
    
    // Lists of objects listening for events...
    List<IDeckEventListener> cardDrawnListeners = new ArrayList<>();
    List<IDeckEventListener> discardDrawnListeners = new ArrayList<>();
    List<IDeckEventListener> cardDiscardedListeners = new ArrayList<>();
    List<IDeckEventListener> deckShuffledListeners = new ArrayList<>();
    List<IDeckEventListener> invalidDiscardListeners = new ArrayList<>();
    
    IEventCriticalSection entryCriticalSection;
    IEventCriticalSection exitCriticalSection;
    
    public BaseDeckEventCaller(IEventCriticalSection entrySection, IEventCriticalSection exitSection){
        
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
    @Override
    public int addCardDrawnListener(IDeckEventListener listener){
        
        if( ! cardDrawnListeners.contains(listener) ){
            cardDrawnListeners.add(listener);
            return listener.hashCode();
        }
        return -1;
    }

    @Override
    public boolean removeCardDrawnListener(int listenerHash) {
        // Find the specified object
        IDeckEventListener obj = this.cardDrawnListeners.stream().
                filter(o -> o.hashCode() == listenerHash).findFirst().get();
        
        // Remove the object from the collection, if it was found
        return this.cardDrawnListeners.remove(obj);
    }

    @Override
    public void onCardDrawn(IPlayingDeck d){
        if( ! cardDrawnListeners.isEmpty() ){
            this.entryCriticalSection.criticalSection();
            cardDrawnListeners.stream().forEach(o -> o.deckEventHandler(d));
            this.exitCriticalSection.criticalSection();
        }
    }

    
    //==================== Functions for the DiscardDrawn Event ====================
    @Override
    public int addDiscardDrawnListener(IDeckEventListener listener){
        if( ! discardDrawnListeners.contains(listener) ){
            discardDrawnListeners.add(listener);
            return listener.hashCode();
        }
        return -1;
    }
    
    @Override
    public boolean removeDiscardDrawnListener(int listenerHash) {
        // Find the specified object
        IDeckEventListener obj = this.discardDrawnListeners.stream().
                filter(o -> o.hashCode() == listenerHash).findFirst().get();
        
        // Remove the object from the collection
        return this.discardDrawnListeners.remove(obj);
    }

    @Override
    public void onDiscardDrawn(IPlayingDeck d){
        if( ! discardDrawnListeners.isEmpty() ){
            this.entryCriticalSection.criticalSection();
            discardDrawnListeners.stream().forEach(o -> o.deckEventHandler(d));
            this.exitCriticalSection.criticalSection();
        }
    }
    
    
    //==================== Functions for the CardDiscarded Event ====================
    @Override
    public int addCardDiscardedListener(IDeckEventListener listener){
        if( ! cardDiscardedListeners.contains(listener) ){
            cardDiscardedListeners.add(listener);
            return listener.hashCode();
        }
        return -1;
    }
    

    @Override
    public boolean removeCardDiscardedListener(int listenerHash) {
        // Find the specified object
        IDeckEventListener obj = this.cardDiscardedListeners.stream().
                filter(o -> o.hashCode() == listenerHash).findFirst().get();

        // Remove the object from the collection
        return this.cardDiscardedListeners.remove(obj);
    }

    @Override
    public void onCardDiscarded(IPlayingDeck d){
        if( ! cardDiscardedListeners.isEmpty() ){
            this.entryCriticalSection.criticalSection();
            cardDiscardedListeners.stream().forEach(o -> o.deckEventHandler(d));
            this.exitCriticalSection.criticalSection();
        }
    }

    //==================== Functions for the Deck Shuffled Event ====================
    @Override
    public int addDeckShuffledListener(IDeckEventListener listener){
        if( ! deckShuffledListeners.contains(listener) ){
            deckShuffledListeners.add(listener);
            return listener.hashCode();
        }
        return -1;
    }
    

    @Override
    public boolean removeDeckShuffledListener(int listenerHash) {
        // Find the specified object
        IDeckEventListener obj = this.deckShuffledListeners.stream().
                filter(o -> o.hashCode() == listenerHash).findFirst().get();

        // Remove the object from the collection
        return this.deckShuffledListeners.remove(obj);
    }

    @Override
    public void onDeckShuffled(IPlayingDeck d){
        if( ! deckShuffledListeners.isEmpty() ){
            this.entryCriticalSection.criticalSection();
            deckShuffledListeners.stream().forEach(o -> o.deckEventHandler(d));
            this.exitCriticalSection.criticalSection();
        }
    }

    //==================== Functions for the Invalid Discard Event ====================
    @Override
    public int addInvalidDiscardListener(IDeckEventListener listener){
        if( ! invalidDiscardListeners.contains(listener) ){
            invalidDiscardListeners.add(listener);
            return listener.hashCode();
        }
        return -1;
    }
    

    @Override
    public boolean removeInvalidDiscardListener(int listenerHash) {
        // Find the specified object
        IDeckEventListener obj = this.invalidDiscardListeners.stream().
                filter(o -> o.hashCode() == listenerHash).findFirst().get();

        // Remove the object from the collection
        return this.invalidDiscardListeners.remove(obj);
    }

    @Override
    public void onInvalidDiscard(IPlayingDeck d){
        if( ! invalidDiscardListeners.isEmpty() ){
            this.entryCriticalSection.criticalSection();
            invalidDiscardListeners.stream().forEach(o -> o.deckEventHandler(d));
            this.exitCriticalSection.criticalSection();
        }
    }
    
}
