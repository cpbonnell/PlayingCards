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
 *     only needs to contain an instance of this class, and expose its add 
 *     and remove functionality via a simple getter function. The parent deck
 *     can then raise the events in this class without worrying about 
 *     maintaining the lists of listeners.
 * </p>
 * <p>
 *     The event architecture is designed around a set of interfaces, each of
 *     which represents a single event. Any class wishing to respond to one
 *     of those events needs only to implement the interface and register
 *     itself through the appropriate addXxxxListener method, and unregister
 *     with the corresponding removeXxxxListener method. I considered an
 *     alternate implementation which would make use of a single functional
 *     interface for all events, specifying a single function: handler().
 *     Classes wishing to subscribe would then pass a function reference
 *     to the appropriate method with a syntax like
 *     " watcher.addXxxxListener(obj1::xxxxHandler) "
 *     I decided against this implementation for two reasons. The first is
 *     that if a subscribing class has to implement one interface for each
 *     event, then it is easy to look at a given class and see what events
 *     it relies on... and it is easy to make sure that each class supplies
 *     no more than one handler. The second reason is that it makes
 *     un-subscribing more tedious.
 * </p>
 * <p>
 *     I decided against this implementation for two reasons. The first is
 *     that if a subscribing class has to implement one interface for each
 *     event, then it is easy to look at a given class and see what events
 *     it relies on... and it is easy to make sure that each class supplies
 *     no more than one handler. The second reason is that the way Java
 *     uses function references makes unsubscribing rather difficult. 
 *     What the addXxxxListener method sees internally with a function
 *     reference call is a newly generated lambda object that contains
 *     a reference to the specified function. Passing the same function
 *     to the same method a second time results in a seperate object, with
 *     a seperate hash code... so the parent function cannot unsubscribe
 *     unless the addXxxxHandler method returns the hash code of the
 *     newly generated lambda object to the calling parent object, which
 *     the parent object must save and pass in turn to the removeXxxxHandler
 *     method in order to unsubscribe.
 * </p>
 * <p>
 *     I did decide to use function references for another purpose. The 
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
public class BaseDeckWatcher implements IDeckEventManager {
    
    // Lists of objects listening for events...
    List<ICardDrawnListener> cardDrawnListeners = new ArrayList<>();
    List<IDiscardDrawnListener> discardDrawnListeners = new ArrayList<>();
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
    @Override
    public void addCardDrawnListener(ICardDrawnListener listener){
        if( ! cardDrawnListeners.contains(listener) ){
            cardDrawnListeners.add(listener);
        }
    }
    
    @Override
    public void removeCardDrawnListener(ICardDrawnListener listener){
        if(cardDrawnListeners.contains(listener)){
            cardDrawnListeners.remove(listener);
        }
    }
    
    public void onCardDrawn(IPlayingDeck d){
        if( ! cardDrawnListeners.isEmpty() ){
            this.entryCriticalSection.criticalSection();
            cardDrawnListeners.stream().forEach(o -> o.cardDrawnEventHandler(d));
            this.exitCriticalSection.criticalSection();
        }
    }

    //==================== Functions for the DiscardDrawn Event ====================
    @Override
    public void addDiscardDrawnListener(IDiscardDrawnListener listener){
        if( ! discardDrawnListeners.contains(listener) ){
            discardDrawnListeners.add(listener);
        }
    }

    @Override
    public void removeDiscardDrawnListener(IDiscardDrawnListener listener){
        if(discardDrawnListeners.contains(listener)){
            discardDrawnListeners.remove(listener);
        }
    }

    public void onDiscardDrawn(IPlayingDeck d){
        if( ! discardDrawnListeners.isEmpty() ){
            this.entryCriticalSection.criticalSection();
            discardDrawnListeners.stream().forEach(o -> o.discardDrawnEventHandler(d));
            this.exitCriticalSection.criticalSection();
        }
    }
    
    
    //==================== Functions for the CardDiscarded Event ====================
    @Override
    public void addCardDiscardedListener(ICardDiscardedListener listener){
        if( ! cardDiscardedListeners.contains(listener) ){
            cardDiscardedListeners.add(listener);
        }
    }

    @Override
    public void removeCardDiscardedListener(ICardDiscardedListener listener){
        if(cardDiscardedListeners.contains(listener)){
            cardDiscardedListeners.remove(listener);
        }
    }

    public void onCardDiscarded(IPlayingDeck d){
        if( ! cardDiscardedListeners.isEmpty() ){
            this.entryCriticalSection.criticalSection();
            cardDiscardedListeners.stream().forEach(o -> o.cardDiscardedEventHandler(d));
            this.exitCriticalSection.criticalSection();
        }
    }

    //==================== Functions for the Deck Shuffled Event ====================
    @Override
    public void addDeckShuffledListener(IDeckShuffledListener listener){
        if( ! deckShuffledListeners.contains(listener) ){
            deckShuffledListeners.add(listener);
        }
    }

    @Override
    public void removeDeckShuffledListener(IDeckShuffledListener listener){
        if(deckShuffledListeners.contains(listener)){
            deckShuffledListeners.remove(listener);
        }
    }

    public void onDeckShuffled(IPlayingDeck d){
        if( ! deckShuffledListeners.isEmpty() ){
            this.entryCriticalSection.criticalSection();
            deckShuffledListeners.stream().forEach(o -> o.deckShuffledEventHandler(d));
            this.exitCriticalSection.criticalSection();
        }
    }

    //==================== Functions for the Invalid Discard Event ====================
    @Override
    public void addInvalidDiscardListener(IInvalidDiscardListener listener){
        if( ! invalidDiscardListeners.contains(listener) ){
            invalidDiscardListeners.add(listener);
        }
    }

    @Override
    public void removeInvalidDiscardListener(IInvalidDiscardListener listener){
        if(invalidDiscardListeners.contains(listener)){
            invalidDiscardListeners.remove(listener);
        }
    }

    public void onInvalidDiscard(IPlayingDeck d){
        if( ! invalidDiscardListeners.isEmpty() ){
            this.entryCriticalSection.criticalSection();
            invalidDiscardListeners.stream().forEach(o -> o.invalidDiscardEventHandler(d));
            this.exitCriticalSection.criticalSection();
        }
    }
    
}
