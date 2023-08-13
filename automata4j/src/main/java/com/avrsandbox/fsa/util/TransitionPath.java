/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2023, The AvrSandbox Project, Automata4j
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.avrsandbox.fsa.util;

import com.avrsandbox.fsa.core.deterministic.DeterministicManager;
import com.avrsandbox.fsa.core.state.AutoState;

/**
 * Provides a transition path composed of two states, a present-state and a next-state.
 *
 * @param <S> a type of {@link AutoState}
 * @author pavl_g
 * @see DeterministicManager#transit(TransitionPath, com.avrsandbox.fsa.core.state.TransitionListener)
 */
@SuppressWarnings("rawtypes")
public final class TransitionPath<S extends AutoState> {

    private String name;
    private S presentState;
    private S nextState;

    /**
     * Instantiates a new transition path with empty states.
     *
     * @param name the transition path name (not null)
     */
    public TransitionPath(String name) {
        this(name, null, null);
    }

    /**
     * Instantiates a new transition path with a present-state and a next-state.
     *
     * @param name the transition path name (not null)
     * @param presentState a present-state to transit to
     * @param nextState a next-state to assign for the next transition
     */
    public TransitionPath(String name, S presentState, S nextState) {
        this.name = name;
        this.presentState = presentState;
        this.nextState = nextState;
    }
    
    /**
     * Assigns the present state to this transition path object.
     * 
     * @param presentState the present state object
     */
    public void assignPresentState(S presentState) {
        this.presentState = presentState;
    }
    
    /**
     * Assigns the next state to this transition path object.
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

    /**
     * Sets the name of this transition path.
     *
     * <p>
     * This attribute ensures uniqueness of the transition path in
     * case of using the {@link com.avrsandbox.fsa.core.deterministic.DeterministicManager}.
     * </p>
     *
     * @param name the new name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the name of this transition path.
     *
     * @return the name of this transition path
     */
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
