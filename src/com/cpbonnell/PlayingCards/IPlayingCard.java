package com.cpbonnell.PlayingCards;

/**
 * Created by christian_bonnell on 6/11/2015.
 */
public interface IPlayingCard {
    public Ranks rank();
    public Suits suit();
    public boolean matches(IPlayingCard c);
    public String toChars();
}
