package com.cpbonnell.PlayingCards;

/**
 * Created by christian_bonnell on 6/11/2015.
 */
public enum Suits {
    CLUBS , DIAMONDS, HEARTS, SPADES;

    public boolean isRed(){
        switch (this){
            case HEARTS:
                return true;
            case DIAMONDS:
                return true;
            default:
                return false;
        }
    }

    public boolean isBlack(){
        switch (this){
            case CLUBS:
                return false;
            case SPADES:
                return true;
            default:
                return false;
        }
    }
}
