package com.cpbonnell.PlayingCards;

import com.sun.javaws.exceptions.InvalidArgumentException;

/**
 * Provides a representation of a basic playing card.
 * 
 * 
 * Created by christian_bonnell on 6/12/2015.
 */
class BasePlayingCard implements IPlayingCard {

    private Ranks rank;
    private Suits suit;


    public BasePlayingCard(Ranks r, Suits s){

        if(r != null && s != null){
            this.rank = r;
            this.suit = s;
        } else {
            this.rank = null;
            this.suit = null;
        }

        //throw new InvalidArgumentException();
    }

    @Override
    public Ranks rank() {
        return this.rank;
    }

    @Override
    public Suits suit() {
        return this.suit;
    }

    @Override
    public boolean matches(IPlayingCard c) {
        return this.rank == c.rank() && this.suit == c.suit();
    }

}
