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

import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.Thread;
import com.avrsandbox.fsa.core.state.AutoState;
import com.avrsandbox.fsa.core.state.TransitionListener;
import com.avrsandbox.fsa.util.TransitionPath;

/**
 * Represents the core component that drives and controls the flow of the Finite-State pattern by selectively assigning a new state,
 * transiting into the newly assigned state and executing the state actions.
 *
 * <p>
 * Each transitional-manager object owns a transition object, the transition object describes a state in terms of memory.
 * </p>
 *
 * @author pavl_g
 */
public class TransitionalManager {
    
    /**
     * The system transition. 
     */
    protected final Transition<AutoState<?, ?>> transition = new Transition<>();

    /**
     * A general purpose logger for this manager object.
     */
    protected static final Logger logger = Logger.getLogger(TransitionalManager.class.getName());
    
    /**
     * Assigns a new next state.
     * Warning: calling this multiple times on different objects
     * will override the previous one !
     * 
     * @param <I> the state input type
     * @param <O> the tracer object type
     * @param autostate the target state
     */
    public <I, O> void assignNextState(AutoState<I, O> autostate) { 
        transition.setNextState(autostate);
        logger.log(Level.INFO, "Assigned a new state " + autostate);
    }

    /**
     * Assigns a new next state from a system transition.
     * Warning: calling this multiple times on different objects
     * will override the previous one !
     * 
     * @param <I> the state input type
     * @param <O> the tracer object type
     * @param transition the target transition
     */
    public <I, O> void assignNextState(final Transition<AutoState<I, O>> transition) { 
        assignNextState(transition.getNextState());
    }
    
    /**
     * Transits to the next-state from a state-transitionPath.
     * 
     * @param <I> the state input type
     * @param <O> the state tracer object type
     * @param transitionPath the system state-transitionPath holding a presentstate and a nextstate
     * @param transitionListener an event driven interface object that fires {@link TransitionListener#onTransition(AutoState)} 
     *                           after the {@link AutoState#invoke(Object)} is invoked when the transition completes
     */
    public <I, O> void transit(final TransitionPath<AutoState<I, O>> transitionPath, final TransitionListener transitionListener) {
        assignNextState(transitionPath.getNextState());
        transit(transitionPath.getNextState().getInput(), transitionListener);
    }

    /**
     * Transits to the next-state from a state-transitionPath with a latency period.
     * 
     * @param <I> the state input type
     * @param <O> the state tracer object type
     * @param time the latency after which the transition starts
     * @param transitionPath the system state-transitionPath holding a presentstate and a nextstate
     * @param transitionListener an event driven interface object that fires {@link TransitionListener#onTransition(AutoState)} 
     *                           after the {@link AutoState#invoke(Object)} is invoked when the transition completes
     * @throws InterruptedException thrown if the application has interrupted the system during the latency period
     */
    public <I, O> void transit(final long time, final TransitionPath<AutoState<I, O>> transitionPath, final TransitionListener transitionListener) throws InterruptedException {
        transit(time, transitionPath.getPresentState().getInput(), transitionListener);
        assignNextState(transitionPath.getNextState());
        transitionPath.removePresentState();
    }

    /**
     * Transits to the next-state after a latency time in milliseconds.
     * 
     * @param <I> the state input type
     * @param time the latency after which the transition starts
     * @param input the state input argument
     * @param transitionListener an event driven interface object that fires {@link TransitionListener#onTransition(AutoState)} 
     *                           after the {@link AutoState#invoke(Object)} is invoked when the transition completes
     * @throws InterruptedException thrown if the application has interrupted the system during the latency period
     */
    public <I> void transit(final long time, final I input, final TransitionListener transitionListener) throws InterruptedException {
        Thread.sleep(time);
        transit(input, transitionListener);
    }
    
    /**
     * Transits to the next assigned state
     * 
     * @param <I> the state input type
     * @param <O> the tracer object type
     * @param input the state input
     * @param transitionListener an event driven interface object that fires {@link TransitionListener#onTransition(AutoState)} 
     *                           after the {@link AutoState#invoke(Object)} is invoked when the transition completes
     * @throws NullPointerException thrown if a pointer to the next state is not found
     */
    @SuppressWarnings("unchecked")
    public <I, O> void transit(final I input, final TransitionListener transitionListener) throws NullPointerException {
        final AutoState<I, O> autoState = (AutoState<I, O>) transition.getNextState();
        logger.log(Level.INFO, "Transiting into a new state " + autoState);
        autoState.onStart();
        autoState.invoke(input);
        if (transitionListener != null) {
            transitionListener.onTransition(autoState);
        }
        autoState.onFinish();
    }

    /**
     * Retrieves the system transition for debugging purposes only.
     * 
     * @return the transition object holding the next-state (if assigned)
     */
    public Transition<AutoState<?, ?>> getTransition() {
        return transition;
    }
}