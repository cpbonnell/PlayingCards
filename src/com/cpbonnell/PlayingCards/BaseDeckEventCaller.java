package com.cpbonnell.PlayingCards;

import com.cpbonnell.PlayingCards.DeckEvents.*;
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
 * <p>
 *     The event architecture is designed around a set of interfaces, each of
 *     which represents a single event. Any class wishing to respond to one
 *     of those events has two options. It may either implement the interface
 *     for the event is wishes to respond to, and then register itself with
 *     the corresponding addXxxxListener method, or it may supply a function
 *     reference to the addXxxxListener method. On a successful registration
 *     of a new listener, the addXxxxListener method returns a unique code
 *     which may be supplied to the removeXxxxListener method to unsubscribe.
 *     An object which registered by supplying a reference to itself (rather
 *     than a function reference) may also unsubscribe by simply passing a
 *     reference to itself to the removeXxxxListener method, without needing
 *     to keep a copy of the unique ID from addXxxxListener. Objects that
 *     register with a function reference must use the unique ID in order
 *     to successfully unsubscribe.
 * </p>
 * <p>
 *     I did decide to use function references only for the parent object. The 
 *     deck object needs to make sure that it locks itself as read-only before
 *     calling any events, and unlock after the event call. Rather than make
 *     the deck object do this every time it wishes to raise an event, it is
 *     more efficient to let the deck supply critical sections to the deck
 *     watcher object, and let the deck watcher invoke them when it invokes
 *     the event handlers. Doing this by supplying an interface for the deck
 *     to implement would require the deck to make those functions public...
 *     and risk some other class invoking them. However, passing the functions
 *     to the deck watcher by using function references allows the critical
 *     sections to remain private to the deck object itself.
 * </p>
 */
class BaseDeckEventCaller implements IDeckEventCaller {
    
    // Lists of objects listening for events...
    List<ICardDrawnListener> cardDrawnListeners = new ArrayList<>();
    List<IDiscardDrawnListener> discardDrawnListeners = new ArrayList<>();
    List<ICardDiscardedListener> cardDiscardedListeners = new ArrayList<>();
    List<IDeckShuffledListener> deckShuffledListeners = new ArrayList<>();
    List<IInvalidDiscardListener> invalidDiscardListeners = new ArrayList<>();
    
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
    public int addCardDrawnListener(ICardDrawnListener listener){
        
        if( ! cardDrawnListeners.contains(listener) ){
            cardDrawnListeners.add(listener);
            return listener.hashCode();
        }
        return -1;
    }
    
    @Override
    public boolean removeCardDrawnListener(ICardDrawnListener listener){
        return this.cardDrawnListeners.remove(listener);
    }

    @Override
    public boolean removeCardDrawnListener(int listenerHash) {
        // Find the specified object
        ICardDrawnListener obj = this.cardDrawnListeners.stream().
                filter(o -> o.hashCode() == listenerHash).findFirst().get();
        
        // Remove the object from the collection, if it was found
        return this.cardDrawnListeners.remove(obj);
    }

    @Override
    public void onCardDrawn(IPlayingDeck d){
        if( ! cardDrawnListeners.isEmpty() ){
            this.entryCriticalSection.criticalSection();
            cardDrawnListeners.stream().forEach(o -> o.cardDrawnEventHandler(d));
            this.exitCriticalSection.criticalSection();
        }
    }

    //==================== Functions for the DiscardDrawn Event ====================
    @Override
    public int addDiscardDrawnListener(IDiscardDrawnListener listener){
        if( ! discardDrawnListeners.contains(listener) ){
            discardDrawnListeners.add(listener);
            return listener.hashCode();
        }
        return -1;
    }

    @Override
    public boolean removeDiscardDrawnListener(IDiscardDrawnListener listener){
        return this.discardDrawnListeners.remove(listener);
    }

    @Override
    public boolean removeDiscardDrawnListener(int listenerHash) {
        // Find the specified object
        IDiscardDrawnListener obj = this.discardDrawnListeners.stream().
                filter(o -> o.hashCode() == listenerHash).findFirst().get();
        
        // Remove the object from the collection
        return this.discardDrawnListeners.remove(obj);
    }

    @Override
    public void onDiscardDrawn(IPlayingDeck d){
        if( ! discardDrawnListeners.isEmpty() ){
            this.entryCriticalSection.criticalSection();
            discardDrawnListeners.stream().forEach(o -> o.discardDrawnEventHandler(d));
            this.exitCriticalSection.criticalSection();
        }
    }
    
    
    //==================== Functions for the CardDiscarded Event ====================
    @Override
    public int addCardDiscardedListener(ICardDiscardedListener listener){
        if( ! cardDiscardedListeners.contains(listener) ){
            cardDiscardedListeners.add(listener);
            return listener.hashCode();
        }
        return -1;
    }

    @Override
    public boolean removeCardDiscardedListener(ICardDiscardedListener listener){
        return this.cardDiscardedListeners.remove(listener);
    }

    @Override
    public boolean removeCardDiscardedListener(int listenerHash) {
        // Find the specified object
        ICardDiscardedListener obj = this.cardDiscardedListeners.stream().
                filter(o -> o.hashCode() == listenerHash).findFirst().get();

        // Remove the object from the collection
        return this.cardDiscardedListeners.remove(obj);
    }

    @Override
    public void onCardDiscarded(IPlayingDeck d){
        if( ! cardDiscardedListeners.isEmpty() ){
            this.entryCriticalSection.criticalSection();
            cardDiscardedListeners.stream().forEach(o -> o.cardDiscardedEventHandler(d));
            this.exitCriticalSection.criticalSection();
        }
    }

    //==================== Functions for the Deck Shuffled Event ====================
    @Override
    public int addDeckShuffledListener(IDeckShuffledListener listener){
        if( ! deckShuffledListeners.contains(listener) ){
            deckShuffledListeners.add(listener);
            return listener.hashCode();
        }
        return -1;
    }

    @Override
    public boolean removeDeckShuffledListener(IDeckShuffledListener listener){
        return this.deckShuffledListeners.remove(listener);
    }

    @Override
    public boolean removeDeckShuffledListener(int listenerHash) {
        // Find the specified object
        IDeckShuffledListener obj = this.deckShuffledListeners.stream().
                filter(o -> o.hashCode() == listenerHash).findFirst().get();

        // Remove the object from the collection
        return this.deckShuffledListeners.remove(obj);
    }

    @Override
    public void onDeckShuffled(IPlayingDeck d){
        if( ! deckShuffledListeners.isEmpty() ){
            this.entryCriticalSection.criticalSection();
            deckShuffledListeners.stream().forEach(o -> o.deckShuffledEventHandler(d));
            this.exitCriticalSection.criticalSection();
        }
    }

    //==================== Functions for the Invalid Discard Event ====================
    @Override
    public int addInvalidDiscardListener(IInvalidDiscardListener listener){
        if( ! invalidDiscardListeners.contains(listener) ){
            invalidDiscardListeners.add(listener);
            return listener.hashCode();
        }
        return -1;
    }

    @Override
    public boolean removeInvalidDiscardListener(IInvalidDiscardListener listener){
        return this.invalidDiscardListeners.remove(listener);
    }

    @Override
    public boolean removeInvalidDiscardListener(int listenerHash) {
        // Find the specified object
        IInvalidDiscardListener obj = this.invalidDiscardListeners.stream().
                filter(o -> o.hashCode() == listenerHash).findFirst().get();

        // Remove the object from the collection
        return this.invalidDiscardListeners.remove(obj);
    }

    @Override
    public void onInvalidDiscard(IPlayingDeck d){
        if( ! invalidDiscardListeners.isEmpty() ){
            this.entryCriticalSection.criticalSection();
            invalidDiscardListeners.stream().forEach(o -> o.invalidDiscardEventHandler(d));
            this.exitCriticalSection.criticalSection();
        }
    }
    
}
