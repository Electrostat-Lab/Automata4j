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

package com.avrsandbox.fsa.core.transition;

import com.avrsandbox.fsa.core.deterministic.DeterministicManager;
import com.avrsandbox.fsa.core.state.AutoState;

/**
 * Provides a transition path composed of two states, a present-state and a next-state.
 *
 * @param <I> a class-generic representing the input value type
 * @param <O> a class-generic representing the tracer object (output) value type
 * @author pavl_g
 * @see DeterministicManager#transit(TransitionPath, com.avrsandbox.fsa.core.state.TransitionListener)
 * @see CascadedTransition for defining a cascade of transitions
 */
public class TransitionPath<I, O> extends Transition<I, O> {

    /**
     * The name of this path.
     */
    protected String name;

    /**
     * The starting auto state.
     */
    protected AutoState<I, O> presentState;

    /**
     * The next state (accepting/terminating) in this path.
     */
    protected AutoState<I, O> nextState;

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
    public TransitionPath(String name, AutoState<I, O> presentState, AutoState<I, O> nextState) {
        super(nextState);
        this.name = name;
        this.presentState = presentState;
    }
    
    /**
     * Assigns the present state to this transition path object.
     * 
     * @param presentState the present state object
     */
    public void assignPresentState(AutoState<I, O> presentState) {
        this.presentState = presentState;
    }

    /**
     * Tests whether this transition path object has an assigned present state.
     *
     * @return true if there is an assigned present state, false otherwise (if null)
     */
    public boolean hasPresentState() {
        return presentState != null;
    }

    /**
     * Retains the present-state object from the heap memory.
     * 
     * @return the present-state object
     */
    public AutoState<I, O> getPresentState() {
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

    /**
     * @deprecated use {@link TransitionPath#removeNextState()}.
     */
    @Override
    @Deprecated
    public void remove() {
    }
}
