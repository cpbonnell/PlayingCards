package com.cpbonnell.PlayingCards;

import java.util.*;

/**
 * Workhorse class, providing most of the functionality of the package.
 */
class BasePlayingDeck implements IPlayingDeck {
    
    // A private variable to allow the deck to be locked into a "read only"
    // mode during event calls, so that its state may be observed by event
    // handlers, but not modified. This both prevents cheating, and avoids
    // infinite loops where an event handler generates new events.
    private boolean isReadOnly = false;
    
    // A deckWatcher object to handle the relations with various event listeners
    BaseDeckWatcher eventCaller;
    
    // A random number generator for all the shuffling and other randomization that
    // is needed throughout the life of the deck.
    private Random rng;

    // This structure maintains the card objects that represent the actual values of the
    // deck. The other three collections maintain reverence to these objects.
    private List<IPlayingCard> cardValues;

    // The names of these collections should be fairly intuitive...
    private List<IPlayingCard> outstandingCards;
    private Queue<IPlayingCard> faceDownPile;
    private Stack<IPlayingCard> discardPile;
    
    //==================== Constructors ====================
    public BasePlayingDeck(List<IPlayingCard> values){
        
        // Instantiate the random number generator, seeding it based on the
        // current time to keep things from progressing the same way every game.
        this.rng = new Random(System.currentTimeMillis());
        
        // Copy the items into the list of values
        this.cardValues = new ArrayList<>(values);
        
        // Instantiate the other collection objects
        this.faceDownPile = new ArrayDeque<>();
        this.discardPile = new Stack<>();
        this.outstandingCards = new ArrayList<>();
        
        // Instantiate the helper classes
        this.eventCaller = new BaseDeckWatcher();
        this.eventCaller.setEntryCriticalSection("lock");
        
        
        
        
        //TODO: Finish this constructor method.
    }

    /**
     * Shuffles the whole discard pile back into the deck.
     */
    @Override
    public void shuffle(){
        this.shuffle(0);
    }

    /**
     * Shuffles the discard pile back into the deck, leaving some.
     * <p>
     *     This function randomizes the cards in the discard pile, and places
     *     them at the bottom of the deck. A specified number are left at the
     *     top of the discard pile, and not randomized or moved back into the
     *     deck. If the number of cards specified is greater than the total
     *     number of cards in the discard pile, then no cards are moved to
     *     the deck.
     * </p>
     * @param leaveTopDiscards The number of cards to leave on top of the discard pile.
     */
    @Override
    public void shuffle(int leaveTopDiscards){
        
        // Pull remaining cards from the discard pile at random and place them in the
        // facedown pile.
        while(this.discardPile.size() > leaveTopDiscards){
            
            // NOTE: nextInt returns a number between 0 (inclusive) and i (exclusive), so no -1 is needed
            // at the end to keep the index in balance
            
            // Pick an index at random from the range to be sorted
            int i = this.rng.nextInt(this.discardPile.size() - leaveTopDiscards);
            i += leaveTopDiscards;
            
            // Switch the card corresponding to  that index to the bottom of the face down pile,
            // and remove it from the discard pile
            IPlayingCard c = this.discardPile.elementAt(i);
            this.faceDownPile.add(c);
            this.discardPile.removeElementAt(i);
        }// END while
        
        
        //DONE (cpb): add a call to the deck-shuffled event here once it is implemented.
        // Lock the deck, raise the appropriate event, unlock, and return
        this.lock();
        this.eventCaller.onDeckShuffled(this);
        this.unlock();
    }
    // END shuffle

    /**
     * Shows the rank of the top card on the discard pile.
     * @return The rank of the top card on the discard pile
     */
    @Override
    public Ranks viewDiscardRank(){
        return this.discardPile.peek().rank();
    }

    /**
     * Shows the suit of the top card on the discard pile
     * @return The suit of the top card on the discard pile.
     */
    @Override
    public Suits viewDiscardSuit(){
        return this.discardPile.peek().suit();
    }

    /**
     * Gets the total number of distinct card objects belonging to the object.
     * @return The number of card objects belonging to the deck.
     */
    @Override
    public int deckSize(){
        return this.cardValues.size();
    }
    
    
    
    
    //==================== Private Helper Functions ====================
    protected void lock(){
        this.isReadOnly = true;
    }
    
    protected void unlock(){
        this.isReadOnly = false;
    }
    
    
}
