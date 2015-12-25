package com.cpbonnell.PokerHands;

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
    
    
}
