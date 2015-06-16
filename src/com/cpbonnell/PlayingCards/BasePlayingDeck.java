package com.cpbonnell.PlayingCards;

import java.util.*;

/**
 * Created by Christian on 6/13/2015.
 */
public class BasePlayingDeck {
    
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
        
        // Make a facade object for each of the actual values, and place it in the 
        // discard pile.
        for(IPlayingCard c : this.cardValues){
            IPlayingCard f = new SecurePlayingCard(c);
            this.discardPile.push(f);
        }
        
        
        //TODO: Finish this constructor method.
    }
    
    
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
            
            //TODO (cpb): add a call to the deck-shuffled event here once it is implemented.
        }// END while
        
        
    }// END shuffle

    /**
     * Shows the rank of the top card on the discard pile.
     * @return The rank of the top card on the discard pile
     */
    public Ranks viewDiscardRank(){
        return this.discardPile.peek().rank();
    }

    /**
     * Shows the suit of the top card on the discard pile
     * @return The suit of the top card on the discard pile.
     */
    public Suits viewDiscardSuit(){
        return this.discardPile.peek().suit();
    }
    
    
    
    
}
