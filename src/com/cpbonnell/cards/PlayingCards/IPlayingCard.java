package com.cpbonnell.cards.PlayingCards;

/**
 * Created by christian_bonnell on 6/11/2015.
 */
public interface IPlayingCard extends Comparable<IPlayingCard> {
    public Ranks rank();
    public Suits suit();
    public boolean matches(IPlayingCard c);
    public String toChars();
}
