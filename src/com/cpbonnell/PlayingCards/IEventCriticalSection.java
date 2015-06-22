package com.cpbonnell.PlayingCards;

/**
 * Functional interface to allow a deck object to supply critical sections for events.
 * <p>
 *     To maintain safety, a deck object needs to be able to lock itself before
 *     raising an event, both for concurrency reasons and to prevent infinite
 *     recursive raising of more events if a client object is badly written. This
 *     interface lets the deck supply critical sections via function reference
 *     to be called before an event is raised, and another after the event is raised.
 * </p>
 */
interface IEventCriticalSection {
    public void criticalSection();
}
