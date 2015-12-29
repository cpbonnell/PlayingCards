package com.cpbonnell.PokerHands;

import com.cpbonnell.PlayingCards.IPlayingCard;
import com.cpbonnell.PlayingCards.Ranks;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by christian_bonnell on 12/24/2015.
 */
public class PokerHandMetadata implements IPokerHand, Comparable<PokerHandMetadata> {
    
    private List<IPlayingCard> cards;
    private List<IPlayingCard> sortedCards;
    
    // Metadata about the five card hand that defines what hand class it
    // belongs to.
    List<Ranks> pairs;
    List<Ranks> triples;
    List<Ranks> quads;
    boolean suitsSame;
    boolean runOfFive;
    
    PokerHandClass hclass = null;
    
    
    public PokerHandMetadata(List<IPlayingCard> cards){
        this.cards = cards;
        
        
        // Sort the cards by rank, and calculate the metadata that defines what
        // hand class it belongs to.
        this.refreshMetadata();
        
        // Using the metadata, tag the hand.
        this.hclass = PokerHandClass.classifyHand(this);
    }
    
    
    @Override
    public List<Ranks> getPairs() {
        return null;
    }

    @Override
    public List<Ranks> getTriples() {
        return this.pairs;
    }

    @Override
    public List<Ranks> getQuads() {
        return this.triples;
    }

    @Override
    public Ranks getHighestCard() {
        return this.getHighestCard(1);
    }

    @Override
    public Ranks getHighestCard(int i) {
        return null;
    }

    @Override
    public boolean hasAllCardsSameSuit() {
        return this.suitsSame;
    }

    @Override
    public boolean hasRunOfFive() {
        return runOfFive;
    }

    @Override
    public boolean hasCardOfRank(Ranks r) {
        return this.cards.stream().filter(c -> c.rank() == r).count() > 0;
    }

    @Override
    public String toChars() {
        
        String result = "";
        
        for(IPlayingCard c : this.sortedCards){
            result += c.toChars();
        }
        
        return result;
    }

    @Override
    public PokerHandClass getHandClassification() {
        if(this.hclass == null) this.hclass = PokerHandClass.classifyHand(this);
        return this.hclass;
    }


    //========== Private helper methods ==========
    
    private void refreshMetadata(){
        
        if(this.cards.size() < 5){
            // handle the error
            //String[] msg = {"The hand size is not 5 cards."};
            //throw new InvalidArgumentException(msg);
        } else {
            // Start by refreshing the sorted list from the master cards list
            this.sortedCards = new ArrayList<>();
            this.sortedCards.addAll(this.cards);
            this.sortedCards.sort((IPlayingCard o1, IPlayingCard o2) -> o1.compareTo(o2));
        }
        
        
        
        //  Find sets of cards with the same rank in the hand
        this.pairs = new ArrayList<Ranks>();
        this.triples = new ArrayList<Ranks>();
        this.quads = new ArrayList<Ranks>();
        this.suitsSame = true;
        this.runOfFive = true;
        
        int repeatedRank = 1;
        
        for(int i = 1; i < this.sortedCards.size(); i++){
            
            IPlayingCard current = this.sortedCards.get(i);
            IPlayingCard previous = this.sortedCards.get(i-1);
            
            // Check to see if all the suits march
            if(current.suit() != previous.suit()) this.suitsSame = false;
            
            // Check to see if the ranks are sequential, handling the special case where
            // we have two and Ace present
            if(current.rank() == previous.rank().next() || 
                    (current.rank() == Ranks.ACE && this.sortedCards.get(1).rank() == Ranks.TWO)){
                // Do nothing
            } else {
                this.runOfFive = false;
            }
            
            // Check for sets of cards with the same suit
            if(current.rank() == previous.rank()){
                repeatedRank++;
            } 
            
            if(current.rank() != previous.rank() || i == 5){
                
                switch(repeatedRank){
                    case 1: break;
                    case 2: this.pairs.add(current.rank());
                        break;
                    case 3: this.triples.add(current.rank());
                        break;
                    case 4: this.quads.add(current.rank());
                        break;
                }
                
                repeatedRank = 1;
            }
            
            
        }
        
        
    }// END refreshMetadata

    @Override
    public int compareTo(PokerHandMetadata o) {
        // If the hands are not of the same hand class, just use the default comparator...
        int result = this.getHandClassification().compareTo(o.getHandClassification());
        if(result != 0) return result;

        List<Ranks> set1;
        List<Ranks> set2;

        // Compare by four-sets first
        set1 = this.getQuads();
        set2 = o.getQuads();
        for(int i = 0; result == 0 && i <= set1.size(); i++){
            result = set1.get(i).compareTo(set2.get(i));
        }

        // Compare by three-sets next
        set1 = this.getTriples();
        set2 = o.getTriples();
        for(int i = 0; result == 0 && i <= set1.size(); i++){
            result = set1.get(i).compareTo(set2.get(i));
        }

        // Compare by two-sets next
        set1 = this.getPairs();
        set2 = o.getPairs();
        for(int i = 0; result == 0 && i <= set1.size(); i++){
            result = set1.get(i).compareTo(set2.get(i));
        }


        // Compare the "kickers" or by single cards last...
        for(int i = 1; result == 0 && i <= 5; i++){
            Ranks c1 = this.getHighestCard(i);
            Ranks c2 = o.getHighestCard(i);

            result = c1.compareTo(c2);
        }
        
        return result;
    }
    
    public static Comparator<PokerHandMetadata> defaultComparator = new Comparator<PokerHandMetadata>() {
        @Override
        public int compare(PokerHandMetadata o1, PokerHandMetadata o2) {
            return o1.compareTo(o2);
        }
    };
}
