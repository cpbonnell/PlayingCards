package com.cpbonnell.PlayingCards;


/**
 * Created by christian_bonnell on 6/11/2015.
 */
public enum Ranks {
    TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;

    private static boolean acesHigh = false;
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
        if(!this.acesHigh){
            return -this.compareTo(r);
        } else {
            return this.compareTo(r);
        }

        // The above code should handle ALL possibilities, so if execution reaches
        // this point, we have a problem.
    }

}
