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

package com.avrsandbox.fsa.core;

import com.avrsandbox.fsa.core.state.AutoState;
import com.avrsandbox.fsa.core.state.NextStateNotFoundException;
import com.avrsandbox.fsa.core.state.TransitionListener;

/**
 * Represents a machine system transition with one next-state {@link Transition#nextState}.
 * 
 * @author pavl_g
 */
public final class Transition<S extends AutoState<?, ?>> {
    
    private S nextState;

    /**
     * Instantiates a transition with an empty next-state.
     */
    public Transition() {
    }

    /**
     * Instantiates a transition with a next-state.
     * 
     * @param nextState the next-state object to assign
     */
    public Transition(S nextState) {
        this.nextState = nextState;
    }

    /**
     * Assigns a next-state into your heap memory.
     * Default value is (null).
     * 
     * @param nextState the next-state object to assign
     */
    public void setNextState(S nextState) {
        this.nextState = nextState;
    }

    /**
     * Loads the state from your heap into your stack memory.
     * 
     * @return the next state of the transition system
     * @throws NextStateNotFoundException thrown if the {@link TransitionalManager#transit(Object, TransitionListener)}
     * is called without assigning a next state
     */
    public S getNextState() throws NextStateNotFoundException {
        /* a business exception if there is no next-state assigned */
        if (nextState == null) {
            throw new NextStateNotFoundException();
        }
        return nextState;
    }

    /**
     * Removes the state from the heap memory.
     * 
     * Warning, this removes the deep copy aka. the real pointer
     * from your heap memory, so you might expect uneventful results.
     */
    public void remove() {
        nextState = null;
    }
}