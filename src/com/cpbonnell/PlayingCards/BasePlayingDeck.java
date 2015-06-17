package com.cpbonnell.PlayingCards;

import com.cpbonnell.PlayingCards.DeckEvents.IEventCriticalSections;

import java.util.*;

/**
 * Workhorse class, providing most of the functionality of the package.
 */
class BasePlayingDeck implements IPlayingDeck, IEventCriticalSections {
    
    // A private variable to allow the deck to be locked into a "read only"
    // mode during event calls, so that its state may be observed by event
    // handlers, but not modified. This both prevents cheating, and avoids
    // infinite loops where an event handler generates new events.
    private boolean isReadOnly;
    
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
    private Queue<IPlayingCard> drawPile;
    private Stack<IPlayingCard> discardPile;
    
    //==================== Constructors ====================
    public BasePlayingDeck(List<IPlayingCard> values){
        
        this.isReadOnly = false;
        
        // Instantiate the random number generator, seeding it based on the
        // current time to keep things from progressing the same way every game.
        this.rng = new Random(System.currentTimeMillis());
        
        // Copy the items into the list of values
        this.cardValues = new ArrayList<>(values);
        
        // Instantiate the other collection objects
        this.drawPile = new ArrayDeque<>();
        this.discardPile = new Stack<>();
        this.outstandingCards = new ArrayList<>();
        
        // Instantiate the helper classes, and pass it a reference to the this object
        // through the IEventCriticalSections interface, allowing the deck watcher
        // to access the critical sections of the deck to lock it before calling events.
        this.eventCaller = new BaseDeckWatcher(this);
        
        
        
        
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
            
            // Place the card corresponding to that index on the bottom of the draw pile,
            // and remove it from the discard pile
            IPlayingCard c = this.discardPile.elementAt(i);
            this.drawPile.add(c);
            this.discardPile.removeElementAt(i);
        }// END while
        
        // Raise the appropriate event...
        this.eventCaller.onDeckShuffled(this);
    }

    /**
     * Shows the rank of the top card on the discard pile.
     * @return The rank of the top card on the discard pile
     */
    @Override
    public Ranks viewDiscardRank(){
        if(this.discardPile.size() > 0){
            return this.discardPile.peek().rank();
        } else {
            return null;
        }
    }

    /**
     * Shows the suit of the top card on the discard pile
     * @return The suit of the top card on the discard pile.
     */
    @Override
    public Suits viewDiscardSuit(){
        if( ! this.discardPile.isEmpty() ){
            return this.discardPile.peek().suit();
        } else {
            return null;
        }
    }

    /**
     * Gets the number of cards available for the drawCard() method.
     * @return The number of cards available in the draw pile.
     */
    @Override
    public int drawPileSize() {
        return this.drawPile.size();
    }

    /**
     * Gets the number of cards available for the drawDiscard() method.
     * @return The number of cards in the discard pile.
     */
    @Override
    public int discardPileSize() {
        return this.discardPile.size();
    }

    /**
     * Gets the number of cards that have been issued via the two draw* methods.
     * @return The number of cards outstanding.
     */
    @Override
    public int outstandingSize() {
        return this.outstandingCards.size();
    }

    /**
     * Gets the total number of distinct card objects belonging to the object.
     * @return The number of card objects belonging to the deck.
     */
    @Override
    public int totalSize(){
        return this.cardValues.size();
    }


    /**
     * Issues a card from the dop of the draw pile.
     * <p>
     * Issues an object implementing IPlayingCard, but with no face value of his own. The
     * card instead references a face value stored in the deck. When the object is
     * returned to the deck via discardCard, the object is invalidated. The object
     * is not destroyed by the method, but the link to its value is severed, and it will
     * appear blank. The value of discarded card is then added to the discard pile.
     * </p>
     * @return a facade object implementing IPlayingCard.
     */
    @Override
    public IPlayingCard drawCard() {
        
        // Handle cases where there are no cards left in the draw pile
        if(this.drawPile.isEmpty()){
            
            if( ! this.discardPile.isEmpty() ){
                // Maybe the discard pile just needs to be shuffled...
                
                this.shuffle();
                
            } else if(this.outstandingCards.size() == this.cardValues.size()){
                // Maybe all the available cards are still in the hands of various users, and
                // we can't do anything about it...
                
                // TODO(cpb): Add an event here to signal that no more cards can be drawn
                return null;
            }
        }
        
        // Get the top card off the draw pile
        IPlayingCard c = this.drawPile.remove();
        
        // Create a card facade to represent the value outside the deck
        IPlayingCard f = new SecurePlayingCard(c);
        
        // Move the actual card into the list of outstanding cards
        this.outstandingCards.add(c);
        
        // Return the facade to the caller
        return f;
    }

    @Override
    public IPlayingCard drawDiscard() {
        return null;
    }

    @Override
    public boolean discardCard(IPlayingCard c) {
        
        // First make sure that the parameter is a valid card facade object...
        SecurePlayingCard s = null;
        if(c.getClass() != SecurePlayingCard.class){
            s = (SecurePlayingCard)c;
        } else {
            return false;
        }
        
        // Find the corresponding valued card in the outstanding cards list...
        //TODO(cpb): I'm in the middle of implementing this method...
        
        
        return false;
    }


    //==================== Private Helper Functions ====================


    @Override
    public void entryCriticalSection() {
        this.isReadOnly = true;
    }

    @Override
    public void exitCriticalSection() {
        this.isReadOnly = false;
    }
}
