package com.cpbonnell.PlayingCards;

import java.util.*;

public class Main {

    public static void main(String[] args) {
	// write your code here
        
        
        Stack<String> s = new Stack();
        
        List<IPlayingCard> cards = new ArrayList<>();
        
        for(Ranks r : Ranks.values()){
            cards.add(new BasePlayingCard(r, Suits.CLUBS));
        }
        
        
        BasePlayingDeck d = new BasePlayingDeck(cards);
        d.shuffle();
        
        
        System.out.println("Execution complete.");
        
        
    }
}
