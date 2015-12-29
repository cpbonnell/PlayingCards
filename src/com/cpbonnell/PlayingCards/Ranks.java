package com.cpbonnell.PlayingCards;


import java.util.Comparator;

/**
 * Created by christian_bonnell on 6/11/2015.
 */
public enum Ranks {
    TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;

    private static boolean acesHigh = true;
    private static Ranks[] vals = Ranks.values();
    public static void setAcesHigh(boolean setting){
        Ranks.acesHigh = setting;
    }
    public static boolean getAcesHigh() {return Ranks.acesHigh;}

    public boolean isFace(){
        if(this == JACK || this == QUEEN || this == KING || this == ACE){
            return true;
        } else{
            return false;
        }
    }
    
    public static Comparator<Ranks> acesHighComparator = new Comparator<Ranks>() {
        @Override
        public int compare(Ranks o1, Ranks o2) {
            return o1.compareTo(o2);
        }
    };
    
    public static Comparator<Ranks> acesLowComparator = new Comparator<Ranks>(){
        
        public int compare(Ranks o1, Ranks o2){
            if((o1 == Ranks.ACE || o2 == Ranks.ACE) && Ranks.getAcesHigh()){
                return -o1.compareTo(o2);
            } else {
                return o1.compareTo(o2);
            }
        }
    };
    
    // functions to provide "wrap around" ability with Aces.
    public Ranks next(){
        if(this == ACE) return TWO;
        else return vals[this.ordinal() + 1];
    }
    
    public Ranks previous(){
        if(this == TWO) return ACE;
        else return vals[this.ordinal() - 1];
        
    }
    
    public String toChars(){
        String ret = "-";
        
        switch (this){
            case TWO: ret = "2";
                break;
            case THREE: ret = "3";
                break;
            case FOUR: ret = "4";
                break;
            case FIVE: ret = "5";
                break;
            case SIX: ret = "6";
                break;
            case SEVEN: ret = "7";
                break;
            case EIGHT: ret = "8";
                break;
            case NINE: ret = "9";
                break;
            case TEN: ret = "0";
                break;
            case JACK: ret = "J";
                break;
            case QUEEN: ret = "Q";
                break;
            case KING: ret = "K";
                break;
            case ACE: ret = "A";
                break;
        }
        return ret;
    }

}
