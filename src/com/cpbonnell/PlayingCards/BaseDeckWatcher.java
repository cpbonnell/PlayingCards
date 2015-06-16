package com.cpbonnell.PlayingCards;

import com.cpbonnell.PlayingCards.DeckEvents.ICardDiscardedListener;
import com.cpbonnell.PlayingCards.DeckEvents.ICardDrawnListener;
import com.cpbonnell.PlayingCards.DeckEvents.IDeckShuffledListener;
import com.cpbonnell.PlayingCards.DeckEvents.IEventCriticalSection;
import com.cpbonnell.PlayingCards.IPlayingDeck;

import java.lang.reflect.InvocationTargetException;
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
    
    String preEvent;
    String postEvent;
    
    public void setEntryCriticalSection(String e){
        this.preEvent = e;
    }
    
    public void setExitCriticalSection(String e){
        this.postEvent = e;
    }
    
    private void performEntryCriticalSection(Object caller){

        try {
            caller.getClass().getMethod(this.preEvent).invoke(caller);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void performExitCriticalSection(Object caller){

        try {
            caller.getClass().getMethod(this.postEvent).invoke(caller);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
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
            
            cardDrawnListeners.stream().forEach(o -> o.CardDrawnEventHandler(d));
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
            cardDiscardedListeners.stream().forEach(o -> o.CardDiscardedEventListener(d));
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
            deckShuffledListeners.stream().forEach(o -> o.DeckShuffledEventListener(d));
        }
    }
    
}
