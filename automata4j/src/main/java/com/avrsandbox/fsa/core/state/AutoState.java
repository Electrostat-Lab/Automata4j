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

package com.avrsandbox.fsa.core.state;

import com.avrsandbox.fsa.core.TransitionalManager;

/**
 * Represents an automaton system state.
 * 
 * @param <I> type-generic for the state-input
 * @param <O> type-generic for the state-tracer object
 * @author pavl_g
 */
public interface AutoState<I, O> {
   
    /**
     * Dispatched as a start point at {@link TransitionalManager#transit(Object, TransitionListener)}.
     */
    void onStart();

    /**
     * Dispatched as a main invocation point at {@link TransitionalManager#transit(Object, TransitionListener)}.
     * 
     * @param input the input to the state
     */
    void invoke(I input);

    /**
     * Dispatched after {@link AutoState#invoke(Object)}.
     */
    void onFinish();

    /**
     * Loads the given state input to the current stack.
     * 
     * @return the state initial input
     */
    I getInput();

    /**
     * Sets the input of the given state.
     *
     * @param input the new input value
     */
    void setInput(I input);

    /**
     * Loads the state tracer object to the current stack.
     * 
     * @return the state tracer object
     */
    O getStateTracer();

    /**
     * Clones object either deeply or superficially.
     * 
     * @param cloneType either superficial or deep
     * @return a clone of this object
     */
    default AutoState<I, O> clone(final CloneType cloneType) {
        /* sanity perform a superficial copy */
        if (cloneType.getCode() == CloneType.SUPERFICIAL.getCode()) {
            return this;
        }

        /* perform a deep clone */
        final AutoState<I, O> clone = new AutoState<>() {
            
            public Object substrate = new Object();

            @Override
            @SuppressWarnings("unchecked")
            public void onStart() {
                ((AutoState<I, O>) substrate).onStart();
            }

            @Override
            @SuppressWarnings("unchecked")
            public void invoke(I input) {
                ((AutoState<I, O>) substrate).invoke(input);
            }

            @Override
            @SuppressWarnings("unchecked")
            public void onFinish() {
                ((AutoState<I, O>) substrate).onFinish();
            }

            @Override
            @SuppressWarnings("unchecked")
            public I getInput() {
                return ((AutoState<I, O>) substrate).getInput();
            }

            @Override
            @SuppressWarnings("unchecked")
            public void setInput(I input) {
                ((AutoState<I, O>) substrate).setInput(input);
            }

            @Override
            @SuppressWarnings("unchecked")
            public O getStateTracer() {
                return ((AutoState<I, O>) substrate).getStateTracer();
            }

            @Override
            public void setCloneSubstrate(AutoState<I, O> substrate) {
                this.substrate = substrate;
            }
        };
        clone.setCloneSubstrate(this);

        return clone;
    }
    
    /**
     * Internal use-only !
     * 
     * @param substrate the object to clone
     */
    default void setCloneSubstrate(final AutoState<I, O> substrate) {
    }
}
