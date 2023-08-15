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
import java.lang.Thread;
import com.avrsandbox.fsa.core.state.AutoState;
import com.avrsandbox.fsa.core.state.NextStateNotFoundException;
import com.avrsandbox.fsa.core.state.TransitionListener;
import com.avrsandbox.fsa.util.AutomataLogger;
import com.avrsandbox.fsa.util.TransitionPath;

/**
 * Represents the core component that drives and controls the flow of the Finite-State pattern by selectively assigning a new state,
 * transiting into the newly assigned state and executing the state actions.
 *
 * <p>
 * Each transitional-manager object owns a transition object, the transition object describes a state in terms of memory.
 * </p>
 *
 * @param <I> the state input type
 * @param <O> the tracer object type
 * @author pavl_g
 */
public class TransitionalManager<I, O> {
    
    /**
     * The system transition. 
     */
    protected final Transition<I, O> transition = new Transition<>();

    /**
     * Instantiates a transitional manager object.
     */
    public TransitionalManager() {
    }

    /**
     * Assigns a new next state.
     *
     * <p>
     * Warning: calling this multiple times on different objects
     * will override the previous one!
     * </p>
     *
     * @param autoState the target state
     */
    public void assignNextState(AutoState<I, O> autoState) {
        transition.setNextState(autoState);
        AutomataLogger.log(Level.INFO, TransitionalManager.class.getName(), "assignNextState(AutoState)",
                        "Assigned a new state " + autoState);
    }

    /**
     * Assigns a new next state from a system transition.
     *
     * <p>
     * Warning: calling this multiple times on different objects
     * will override the previous one!
     * </p>
     *
     * @param transition the target transition
     */
    public void assignNextState(Transition<I, O> transition) {
        assignNextState(transition.getNextState());
    }
    
    /**
     * Traverses through a transition path starting from the present-state
     * assigning the next-state without transiting to it.
     *
     * <p>
     * Tips: A call to {@link TransitionalManager#transit(TransitionListener)} can be submitted from the
     * {@link TransitionListener#onTransition(AutoState)} to transit to the assigned next-state.
     * </p>
     *
     * @param transitionPath the system state-transitionPath holding a present state and a next state
     * @param transitionListener an event driven interface object that fires {@link TransitionListener#onTransition(AutoState)} 
     *                           after the {@link AutoState#invoke(Object)} is invoked when the transition to the present-state completes
     */
    public void transit(TransitionPath<I, O> transitionPath, TransitionListener<I, O> transitionListener) {
        assignNextState(transitionPath.getPresentState());
        transit(transitionPath.getPresentState().getInput(),
                            new NextStateAssigner<>(this, transitionPath, transitionListener));
    }

    /**
     * Transits to the next-state from a state-transitionPath with a latency period.
     *
     * @param time the latency after which the transition starts
     * @param transitionPath the system state-transitionPath holding a present state and a next state
     * @param transitionListener an event driven interface object that fires {@link TransitionListener#onTransition(AutoState)} 
     *                           after the {@link AutoState#invoke(Object)} is invoked when the transition completes
     * @throws InterruptedException thrown if the application has interrupted the system during the latency period
     */
    public void transit(long time, TransitionPath<I, O> transitionPath, TransitionListener<I, O> transitionListener) throws InterruptedException {
        transit(time, transitionPath.getPresentState().getInput(), transitionListener);
        assignNextState(transitionPath.getNextState());
        transitionPath.removePresentState();
    }

    /**
     * Transits to the next-state after a latency time in milliseconds.
     *
     * @param time the latency after which the transition starts
     * @param input the state input argument
     * @param transitionListener an event driven interface object that fires {@link TransitionListener#onTransition(AutoState)} 
     *                           after the {@link AutoState#invoke(Object)} is invoked when the transition completes
     * @throws InterruptedException thrown if the application has interrupted the system during the latency period
     */
    public void transit(long time, I input, TransitionListener<I, O> transitionListener) throws InterruptedException {
        Thread.sleep(time);
        transit(input, transitionListener);
    }
    
    /**
     * Transits to the next assigned state
     *
     * @param input the state input
     * @param transitionListener an event driven interface object that fires {@link TransitionListener#onTransition(AutoState)} 
     *                           after the {@link AutoState#invoke(Object)} is invoked when the transition completes
     * @throws NextStateNotFoundException thrown if a pointer to the next state is not found
     */
    public void transit(I input, TransitionListener<I, O> transitionListener) throws NextStateNotFoundException {
        final AutoState<I, O> autoState = transition.getNextState();
        AutomataLogger.log(Level.INFO, TransitionalManager.class.getName(),
                    "transit(Input, TransitionalListener)", "Transiting into a new state " + autoState);
        autoState.setInput(input);
        autoState.onStart();
        autoState.invoke(input);
        if (transitionListener != null) {
            transitionListener.onTransition(autoState);
        }
        autoState.onFinish();
    }

    /**
     * Transits to the next assigned state with the predefined input value.
     *
     * @param transitionListener an event driven interface object that fires {@link TransitionListener#onTransition(AutoState)}
     *                           after the {@link AutoState#invoke(Object)} is invoked when the transition completes
     * @throws NextStateNotFoundException thrown if a pointer to the next state is not found
     */
    public void transit(TransitionListener<I, O> transitionListener) {
        transit(transition.getNextState().getInput(), transitionListener);
    }

    /**
     * Retrieves the system transition for debugging purposes only.
     * 
     * @return the transition object holding the next-state (if assigned)
     */
    public Transition<I, O> getTransition() {
        return transition;
    }


}