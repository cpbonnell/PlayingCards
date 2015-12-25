package com.cpbonnell.PlayingCards;


/**
 * Created by christian_bonnell on 6/11/2015.
 */
public enum Ranks {
    TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;

    private static boolean acesHigh = true;
    public static void setAcesHigh(boolean setting){
        Ranks.acesHigh = setting;
    }

    public boolean isFace(){
        if(this == JACK || this == QUEEN || this == KING || this == ACE){
            return true;
        } else{
            return false;
        }
    }

    
    public int compare(Ranks r){
        if((this == ACE || r == ACE) && !this.acesHigh){
            return -this.compareTo(r);
        } else {
            return this.compareTo(r);
        }

        // The above code should handle ALL possibilities, so if execution reaches
        // this point, we have a problem.
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
