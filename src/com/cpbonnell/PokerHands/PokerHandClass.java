package com.cpbonnell.PokerHands;

import com.cpbonnell.PlayingCards.IPlayingCard;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

/**
 * Created by christian_bonnell on 12/24/2015.
 */
public enum PokerHandClass {
    HIGH_CARD (1302540), 
    ONE_PAIR (1098240), 
    TWO_PAIR (123552), 
    THREE_KIND (54912), 
    STRAIGHT (10240),
    FLUSH (5148), 
    FULL_HOUSE (3744), 
    FOUR_KIND (624), 
    STRAIGHT_FLUSH (40);


    private double n;
    public static final double N = 2598960;
    
    PokerHandClass(int n){
        this.n = n;
    }
    
    public double probability(){
        return n/N;
    }
    
    // Main version of the function that contains the logic
    public static PokerHandClass classifyHand(PokerHandMetadata meta){
        
        // Start be extracting the relevant metadata
        int npair = meta.getPairs().size();
        int ntrip = meta.getTriples().size();
        int nquad = meta.getQuads().size();
        boolean flush = meta.hasAllCardsSameSuit();
        boolean straight = meta.hasRunOfFive();
        
        
        // By far the most common hand we will run into is the "one pair" class, followed by the "two pair"
        // and the nthree of a kind
        if(npair == 1 && ntrip == 0) return ONE_PAIR;
        if(npair == 2) return TWO_PAIR;
        if(npair == 0 && ntrip == 1) return THREE_KIND;
        
        //The work has already been done to see if the hand has is a "straight" or "flush"
        // variety, so we can basically check those for free.
        if(straight && !flush) return STRAIGHT;
        if( flush && !straight) return FLUSH;
        
        
        // Lastly, look for the less common "full house", "four of a kind", and "straight flush" classes
        if(ntrip == 1 && npair == 1) return FULL_HOUSE;
        if(nquad == 1) return FOUR_KIND;
        if(straight && flush) return STRAIGHT_FLUSH;
        
        // Since the hand cannot be classified as any of the other categories, we will treat it as
        // a case of the "high card" category
        return HIGH_CARD;
    }
    
    
    // Alternate version of the hand for cases where the metadata has not yet been calculated
    public static PokerHandClass classifyHand(List<IPlayingCard> cards){
        PokerHandMetadata meta = new PokerHandMetadata(cards);
        return meta.getHandClassification();
    }
}
