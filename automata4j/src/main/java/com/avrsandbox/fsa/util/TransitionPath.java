package com.avrsandbox.fsa.util;

import com.avrsandbox.fsa.core.TransitionalManager;
import com.avrsandbox.fsa.core.state.AutoState;

/**
 * Provides a state-map composed of two states, a present-state and a next-state.
 * 
 * @author pavl_g
 * @see TransitionalManager#transit(StateMap, com.avrsandbox.fsa.core.state.TransitionListener)
 */
public final class StateMap<S extends AutoState> {

    private String name;
    private S presentState;
    private S nextState;

    /**
     * Instantiates a new state map with empty states
     *
     * @param name the map name (not null)
     */
    public StateMap(String name) {
        this(name, null, null);
    }

    /**
     * Instantiates a new state map with a present-state and a next-state.
     * 
     * @param presentState a present-state to transit to
     * @param nextState a next-state to assign for the next transition
     */
    public StateMap(String name, S presentState, S nextState) {
        this.name = name;
        this.presentState = presentState;
        this.nextState = nextState;
    }
    
    /**
     * Assigns the present state to this map object.
     * 
     * @param presentState the present state object
     */
    public void assignPresentState(S presentState) {
        this.presentState = presentState;
    }
    
    /**
     * Assigns the next state to this map object.
     * 
     * @param nextState the next state object
     */
    public void assignNextState(S nextState) {
        this.nextState = nextState;
    }
    
    /**
     * Retains the next-state object from the heap memory.
     * 
     * @return the next-state object
     */
    public S getNextState() {
        return nextState;
    }

    /**
     * Retains the present-state object from the heap memory.
     * 
     * @return the present-state object
     */
    public S getPresentState() {
        return presentState;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Nullifies the present state reference.
     */
    public void removePresentState() {
        presentState = null;
    }

    /**
     * Nullifies the next state reference.
     */
    public void removeNextState() {
        nextState = null;
    }

    /**
     * Nullifies all object references for the GC.
     */
    public void removeAll() {
        removePresentState();
        removeNextState();
    }
}
