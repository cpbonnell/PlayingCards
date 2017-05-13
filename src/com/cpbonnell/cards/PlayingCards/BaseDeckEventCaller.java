package com.cpbonnell.cards.PlayingCards;

import java.util.function.IntSupplier;
import java.util.function.Consumer;

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
    List< Consumer<IPlayingDeck> > cardDrawnListeners = new ArrayList<>();
    List< Consumer<IPlayingDeck> > discardDrawnListeners = new ArrayList<>();
    List< Consumer<IPlayingDeck> > cardDiscardedListeners = new ArrayList<>();
    List< Consumer<IPlayingDeck> > deckShuffledListeners = new ArrayList<>();
    List< Consumer<IPlayingDeck> > invalidDiscardListeners = new ArrayList<>();
    
    IntSupplier entryCriticalSection;
    IntSupplier exitCriticalSection;
    
    public BaseDeckEventCaller(IntSupplier entrySection, IntSupplier exitSection){
        
        // If no callback is supplied for the critical sections, we supply a default
        // method in the form of a lambda that does nothing. This makes later code
        // much simpler, since it can call the entry section and exit section
        // without having to first check for null values.
        if(entrySection == null){
            this.entryCriticalSection = () -> 1;
        } else {
            this.entryCriticalSection = entrySection;
        }
        
        if(exitSection == null){
            this.exitCriticalSection = () -> 1;
        } else {
            this.exitCriticalSection = exitSection;
        }
    }
    
    
    //==================== Functions for the CardDrawn Event ====================
    @Override
    public int addCardDrawnListener(Consumer<IPlayingDeck> listener){
        
        if( ! cardDrawnListeners.contains(listener) ){
            cardDrawnListeners.add(listener);
            return listener.hashCode();
        }
        return -1;
    }

    @Override
    public boolean removeCardDrawnListener(int listenerHash) {
        // Find the specified object
        Consumer<IPlayingDeck> obj = this.cardDrawnListeners.stream().
                filter(o -> o.hashCode() == listenerHash).findFirst().get();
        
        // Remove the object from the collection, if it was found
        return this.cardDrawnListeners.remove(obj);
    }

    @Override
    public void onCardDrawn(IPlayingDeck d){
        if( ! cardDrawnListeners.isEmpty() ){
            this.entryCriticalSection.getAsInt();
            cardDrawnListeners.stream().forEach(o -> o.accept(d));
            this.exitCriticalSection.getAsInt();
        }
    }

    
    //==================== Functions for the DiscardDrawn Event ====================
    @Override
    public int addDiscardDrawnListener(Consumer<IPlayingDeck> listener){
        if( ! discardDrawnListeners.contains(listener) ){
            discardDrawnListeners.add(listener);
            return listener.hashCode();
        }
        return -1;
    }
    
    @Override
    public boolean removeDiscardDrawnListener(int listenerHash) {
        // Find the specified object
        Consumer<IPlayingDeck> obj = this.discardDrawnListeners.stream().
                filter(o -> o.hashCode() == listenerHash).findFirst().get();
        
        // Remove the object from the collection
        return this.discardDrawnListeners.remove(obj);
    }

    @Override
    public void onDiscardDrawn(IPlayingDeck d){
        if( ! discardDrawnListeners.isEmpty() ){
            this.entryCriticalSection.getAsInt();
            discardDrawnListeners.stream().forEach(o -> o.accept(d));
            this.exitCriticalSection.getAsInt();
        }
    }
    
    
    //==================== Functions for the CardDiscarded Event ====================
    @Override
    public int addCardDiscardedListener(Consumer<IPlayingDeck> listener){
        if( ! cardDiscardedListeners.contains(listener) ){
            cardDiscardedListeners.add(listener);
            return listener.hashCode();
        }
        return -1;
    }
    

    @Override
    public boolean removeCardDiscardedListener(int listenerHash) {
        // Find the specified object
        Consumer<IPlayingDeck> obj = this.cardDiscardedListeners.stream().
                filter(o -> o.hashCode() == listenerHash).findFirst().get();

        // Remove the object from the collection
        return this.cardDiscardedListeners.remove(obj);
    }

    @Override
    public void onCardDiscarded(IPlayingDeck d){
        if( ! cardDiscardedListeners.isEmpty() ){
            this.entryCriticalSection.getAsInt();
            cardDiscardedListeners.stream().forEach(o -> o.accept(d));
            this.exitCriticalSection.getAsInt();
        }
    }

    //==================== Functions for the Deck Shuffled Event ====================
    @Override
    public int addDeckShuffledListener(Consumer<IPlayingDeck> listener){
        if( ! deckShuffledListeners.contains(listener) ){
            deckShuffledListeners.add(listener);
            return listener.hashCode();
        }
        return -1;
    }
    

    @Override
    public boolean removeDeckShuffledListener(int listenerHash) {
        // Find the specified object
        Consumer<IPlayingDeck> obj = this.deckShuffledListeners.stream().
                filter(o -> o.hashCode() == listenerHash).findFirst().get();

        // Remove the object from the collection
        return this.deckShuffledListeners.remove(obj);
    }

    @Override
    public void onDeckShuffled(IPlayingDeck d){
        if( ! deckShuffledListeners.isEmpty() ){
            this.entryCriticalSection.getAsInt();
            deckShuffledListeners.stream().forEach(o -> o.accept(d));
            this.exitCriticalSection.getAsInt();
        }
    }

    //==================== Functions for the Invalid Discard Event ====================
    @Override
    public int addInvalidDiscardListener(Consumer<IPlayingDeck> listener){
        if( ! invalidDiscardListeners.contains(listener) ){
            invalidDiscardListeners.add(listener);
            return listener.hashCode();
        }
        return -1;
    }
    

    @Override
    public boolean removeInvalidDiscardListener(int listenerHash) {
        // Find the specified object
        Consumer<IPlayingDeck> obj = this.invalidDiscardListeners.stream().
                filter(o -> o.hashCode() == listenerHash).findFirst().get();

        // Remove the object from the collection
        return this.invalidDiscardListeners.remove(obj);
    }

    @Override
    public void onInvalidDiscard(IPlayingDeck d){
        if( ! invalidDiscardListeners.isEmpty() ){
            this.entryCriticalSection.getAsInt();
            invalidDiscardListeners.stream().forEach(o -> o.accept(d));
            this.exitCriticalSection.getAsInt();
        }
    }
    
}
