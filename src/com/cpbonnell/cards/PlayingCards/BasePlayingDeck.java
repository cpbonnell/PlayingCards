package com.cpbonnell.cards.PlayingCards;

import java.util.*;

/**
 * Workhorse class, providing most of the functionality of the package.
 * <p>
 *     The BasePlayingDeck provides functionality for any application that
 *     needs to have basic playing card functionality. It maintains a set
 *     of distinct cards, with duplicate values allowed. The deck then
 *     allows cards to be drawn from a randomized, face-down draw pile,
 *     discarded to an ordered face-up discard pile, and drawn again
 *     from the discard pile in the order which they were discarded. Each
 *     of these actions raises an event, which allows objects to monitor
 *     not only the state of the deck, but also the actions of other objects
 *     on the deck.
 * </p>
 * <p>
 *     The BasePlayingDeck class simulates the behavior of a physical playing
 *     deck (namely, the uniqueness of cards) by keeping an internal set of
 *     PlayingCard objects that determine the values present in the deck.
 *     When a card is "drawn" a new object of type SecurePlayingCard is
 *     created, with an internal reference pointing to one of the
 *     PlayingCard objects internal to the deck. This SecurePlayingCard
 *     has no values stored internally, but implements the IPlayingCard
 *     interface by mirroring the values of the playing card it points to.
 * </p>
 */
public class BasePlayingDeck implements IPlayingDeck {
    
    // A private variable to allow the deck to be locked into a "read only"
    // mode during event calls, so that its state may be observed by event
    // handlers, but not modified. This both prevents cheating, and avoids
    // infinite loops where an event handler generates new events.
    private boolean isReadOnly;
    
    // Other variables that specify optional deck behaviors
    private boolean allowDrawFromDiscard;
    private boolean allowShuffle;
    
    // A DeckEventCaller object to handle the relations with various event listeners
    IDeckEventCaller eventCaller;
    
    // A random number generator for all the shuffling and other randomization that
    // is needed throughout the life of the deck.
    private Random rng;

    // This structure maintains the card objects that represent the actual values of the
    // deck. The other three collections maintain reverence to these objects.
    private List<IPlayingCard> cardValues;

    // The names of these collections should be fairly intuitive...
    private List<IPlayingCard> outstandingCards;
    private Queue<IPlayingCard> drawPile;
    private Deque<IPlayingCard> discardPile;
    
    //============================== Constructors ==============================
    public BasePlayingDeck(List<IPlayingCard> values){
        
        this.isReadOnly = false;
        this.allowDrawFromDiscard = true;
        this.allowShuffle = true;
        
        // Instantiate the random number generator, seeding it based on the
        // current time to keep things from progressing the same way every game.
        this.rng = new Random(System.currentTimeMillis());
        
        // Copy the items into the list of values
        this.cardValues = new ArrayList<>(values);
        
        // Instantiate the other collection objects
        this.drawPile = new ArrayDeque<>();
        this.discardPile = new ArrayDeque<>(this.cardValues);
        this.outstandingCards = new ArrayList<>();
        
        // Instantiate the helper classes, and pass it a reference to the the
        // lock and unlock functions.
        this.eventCaller = new BaseDeckEventCaller(this::lock, this::unlock);
    }

    /**
     * Static method to construct a standard 52 card French deck.
     * @return An object implementing IPlayingCard.
     */
    public static IPlayingDeck newStandardFrenchDeck(){
        
        // Make a list to hold the card values
        List<IPlayingCard> cards = new ArrayList<>();
        
        // Populate the list with the appropriate values
        for(Suits s: Suits.values()){
            for(Ranks r: Ranks.values()){
                cards.add(new PlayingCard(r, s));
            }
        }
        
        
        // Construct the deck object
        BasePlayingDeck deck = new BasePlayingDeck(cards);
        
        // Set the default values
        deck.allowShuffle = true;
        deck.allowDrawFromDiscard = true;
        
        // Return the deck object
        deck.shuffle();
        return deck;
    }

    /**
     * Static method to construct a standard 48 card Pinochle deck.
     * @return An object implementing IPlayingCard.
     */
    public static IPlayingDeck newPinochleDeck(){

        // Make a list to hold the card values
        List<IPlayingCard> cards = new ArrayList<>();

        // Populate the list with the appropriate values
        Ranks[] desiredRanks = {Ranks.ACE, Ranks.KING, Ranks.QUEEN, Ranks.JACK, Ranks.TEN, Ranks.NINE};
        
        for(Suits s: Suits.values()){
            for(Ranks r: desiredRanks){
                cards.add(new PlayingCard(r, s));
                cards.add(new PlayingCard(r, s));
            }
        }


        // Construct the deck object
        BasePlayingDeck deck = new BasePlayingDeck(cards);

        // Set the default values
        deck.allowShuffle = true;
        deck.allowDrawFromDiscard = true;

        // Return the deck object
        deck.shuffle();
        return deck;
    }
    
    
    
    //============================== Accessors ==============================
    // Make the event manager and the deck state public

    /**
     * Get access to the deck manager to register or unregister event listeners.
     * @return A reference to the Deck's event caller object.
     */
    public IDeckEventRegistrar getEventManager(){
        return this.eventCaller;
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
     * Determines if the specified IPlayingCard object is a valid outstanding card from this deck.
     * @param c An objectimplementing IPlayingCard whose authenticity is in question.
     * @return true if the card is a valid outstanding card, false if it is not.
     */
    @Override
    public boolean validateOutstandingCard(IPlayingCard c){
        
        // Make sure that the parameter is of the proper class
        if(c.getClass() != SecurePlayingCard.class){
            return false;
        }
        
        //Cast the parameter to a concrete type to get the additional functionality
        SecurePlayingCard s = (SecurePlayingCard) c;
        
        // Scan through the BasePlayingCards in the list of outstanding cards, and
        // if the item is found, return true. Otherwise, return false.
         return this.outstandingCards.stream().anyMatch( s::pointsAt );
    }

    //============================== Action Methods ==============================
    // Methods that change the state of the deck. All of these raise events,
    // and are available only when the deck is NOT locked. 
    
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
        
        // Exit immediately if the deck is in read-only mode, or if shuffling is not allowed
        if(this.isReadOnly || ! this.allowShuffle ){
            return;
        }
        
        // The deque collection does not allow random access, so we need to put the cards from
        // the discard pile into a collection that will allow us to split them how we like.
        List<IPlayingCard> replacementDiscardPile = new ArrayList<>(this.discardPile);

        // Pull remaining cards from the discard pile at random and place them in the
        // facedown pile.
        while(replacementDiscardPile.size() > leaveTopDiscards){
            
            // NOTE: nextInt returns a number between 0 (inclusive) and i (exclusive), so no -1 is needed
            // at the end to keep the index in balance
            
            // Pick an index at random from the range to be sorted
            int i = this.rng.nextInt(replacementDiscardPile.size() - leaveTopDiscards);
            i += leaveTopDiscards;
            
            // Place the card corresponding to that index on the bottom of the draw pile,
            // and remove it from the discard pile
            IPlayingCard c = replacementDiscardPile.remove(i);
            this.drawPile.add(c);
        }// END while
        
        // We are done shuffling the discard pile, and need only replace the cards that
        // we left at the top of the discard pile.
        this.discardPile = new ArrayDeque<>(replacementDiscardPile);
        
        // Raise the appropriate event...
        this.eventCaller.onDeckShuffled(this);
    }

    /**
     * Shuffles the whole discard pile back into the deck.
     */
    @Override
    public void shuffle(){
        this.shuffle(0);
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

        // Exit immediately if the deck is in read-only mode.
        if(this.isReadOnly){
            return null;
        }
        
        // Handle cases where there are no cards left in the draw pile
        if(this.drawPile.isEmpty()){
            
            if( ! this.discardPile.isEmpty() ){
                // Maybe the discard pile just needs to be shuffled...
                
                this.shuffle();
                
            } else if(this.outstandingCards.size() == this.cardValues.size()){
                // Maybe all the available cards are still in the hands of various users, and
                // we can't do anything about it...
                return null;
            }
        }
        
        // Get the top card off the draw pile
        IPlayingCard c = this.drawPile.remove();
        
        // Create a card facade to represent the value outside the deck
        IPlayingCard f = new SecurePlayingCard(c);
        
        // Move the actual card into the list of outstanding cards
        this.outstandingCards.add(c);
        
        // Raise the card drawn event, and return the facade to the caller
        this.eventCaller.onCardDrawn(this);
        return f;
    }

    @Override
    public IPlayingCard drawDiscard() {

        // Exit immediately if the deck is in read-only mode, or if drawing from the
        // discard pile is not enabled, or if there are no cards to be drawn.
        if(this.isReadOnly || ! this.allowDrawFromDiscard || this.discardPile.isEmpty()){
            return null;
        }
        
        // Pull the top card off the discard pile, and create a facade wrapper around it
        IPlayingCard actual = this.discardPile.pop();
        IPlayingCard facade = new SecurePlayingCard(actual);
        
        // Put the actual card into the list of outstanding cards
        this.outstandingCards.add(actual);
        
        // Raise the appropriate event, and return the facade
        this.eventCaller.onDiscardDrawn(this);
        return facade;
    }

    @Override
    public boolean discardCard(IPlayingCard c) {

        // Exit immediately if the deck is in read-only mode.
        if(this.isReadOnly){
            return false;
        }
        
        // First make sure that the parameter is a valid card facade object...
        if(c.getClass() != SecurePlayingCard.class){
            //DONE(cpb): Raise an invalid discard event here
            this.eventCaller.onInvalidDiscard(this);
            return false;
        }
        
        // We have verified that the parameter c is indeed a SecurePlayingCard, so
        // it is safe to cast it and gain access to the object's additional functionality.
        SecurePlayingCard facade = (SecurePlayingCard) c;
        
        
        // Find the corresponding valued card in the outstanding cards list...
        //NOTE: The method reference may be replaced with the lambda "o -> facade.pointsAt(o)" ...
        // not sure which is clearer, and may swap them out later.
        IPlayingCard target = this.outstandingCards.stream().filter( facade::pointsAt ).findFirst().get();
        
        // We now want to remove the target card from the list of outstanding cards,
        // and put it on top of the discard pile...
        this.outstandingCards.remove(target);
        this.discardPile.push(target);
        
        // Lastly, we want to invalidate the card facade that was passed as a parameter,
        // raise a card discarded event, and return true to indicate that the
        // operation was successful.
        facade.invalidate();
        this.eventCaller.onCardDiscarded(this);
        return true;
    }


    //==================== Private Helper Functions ====================

    
    private int lock() {
        this.isReadOnly = true;
        return 1;
    }
    
    private int unlock() {
        this.isReadOnly = false;
        return 1;
    }
    
}
