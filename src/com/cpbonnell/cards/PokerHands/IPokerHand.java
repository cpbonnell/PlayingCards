package com.cpbonnell.cards.PokerHands;


import com.cpbonnell.cards.PlayingCards.Ranks;

import java.util.List;

/**
 * Created by christian_bonnell on 12/24/2015.
 */
public interface IPokerHand {
    
    
    // These functions return a list containing the ranks for which there is a set
    // of cards of the given size. Sets are exclusive (a hand with 3 Jacks will return
    // Jack in the list for getTriples(), but not in getPairs()). If there are no sets
    // of the given size, the function returns an empty list.
    List<Ranks> getPairs();
    List<Ranks> getTriples();
    List<Ranks> getQuads();
    
    // Returns the i_th highest card. Calling the function without an argument, or with
    // "0" or "1" will return the rank of the highest card. Calling it with "2" will
    // return the second highest card, and so on. Calling the function with an integer
    // greater than the the number of cards in the hand will result in an "index out of 
    // bounds" exception.
    Ranks getHighestCard();
    Ranks getHighestCard(int i);
    
    
    boolean hasAllCardsSameSuit();
    
    boolean hasRunOfFive();
    
    boolean hasCardOfRank(Ranks r);
    
    // Return a 10-character string representation of the 5-card hand
    String toChars();
    
    PokerHandClass getHandClassification();
     
    
    
}
