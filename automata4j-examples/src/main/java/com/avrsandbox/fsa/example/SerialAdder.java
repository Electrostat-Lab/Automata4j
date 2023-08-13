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

package com.avrsandbox.fsa.example;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.avrsandbox.fsa.core.TransitionalManager;
import com.avrsandbox.fsa.core.state.AutoState;
import com.avrsandbox.fsa.core.state.CloneType;
import com.avrsandbox.fsa.core.state.TransitionListener;
import com.avrsandbox.fsa.util.AutomataLogger;

/**
 * A tech-demo demonstrating a basic example of an antegrade finite-state-automaton design pattern.
 * 
 * Input bits at t0 = (x1 = 0, x2 = 0) ; At Present-State(PS) = (0) = NonCarryState(default-state) ; Output(Z0) = 0 ; Next-State(NS) = (0) = NonCarryState
 * Input bits at t1 = (x1 = 0, x2 = 1) ; At Present-State(PS) = (0) = NonCarryState ; Output(Z1) = 1 ; Next-State(NS) = (0) = NonCarryState
 * ..... and so on
 * 
 * The execution states flow is determined by those 3 factors: 
 * # The Present-State(PS)
 * # The Inputs 
 * # The Next-State (which is determined by the inputs and the present state)
 * 
 * @author pavl_g.
 */
public final class SerialAdder extends Thread implements TransitionListener {

    private final TransitionalManager transitionalManager = new TransitionalManager();
    private final List<BitsAdder> adders = new ArrayList<>();
    private final List<AutoState<BitsAdder, Integer>> states = new ArrayList<>();

    /**
     * Creates a serial adder state-machine example
     */
    public SerialAdder() {
        super("SerialAdder-Finite-State-Automaton");
    }
    
    /**
     * Initializes the AutoStates and assigns the entry state.
     */
    public void init() {
        /* enables automata logger */
        AutomataLogger.setEnabled(true);

        /* create bits to add */
        adders.add(new BitsAdder(0, 0));
        adders.add(new BitsAdder(0, 1));
        adders.add(new BitsAdder(1, 0));
        adders.add(new BitsAdder(1, 1));
        adders.add(new BitsAdder(0, 1));
        adders.add(new BitsAdder(1, 0));
        adders.add(new BitsAdder(1, 1));
        adders.add(new BitsAdder(0, 0));
        /* state is transited to stateA again -- NonCarryState default state */
        adders.add(new BitsAdder(0, 0));
        
        /* define states */
        AutoState<BitsAdder, Integer> stateA = new NonCarryState();
        final AutoState<BitsAdder, Integer> stateB = new CarryState();
        /* Test deep copy */
        final AutoState<BitsAdder, Integer> copyA = stateA.clone(CloneType.DEEP);
        System.out.println(stateA);
        System.out.println(copyA);

        states.add(copyA);
        states.add(stateB);

        stateA = null;
        System.out.println(stateA);
        System.out.println(copyA);
        
        /* assign the entry state to this machine */
        transitionalManager.assignNextState(copyA);
    }
    
    @Override
    public void run() {
        adders.forEach( adder -> {
            try {
                transitionalManager.transit(500, adder, this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
        });  
        Logger.getLogger(getName()).info("Finite state finishes !");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I, O> void onTransition(AutoState<I, O> presentState) {
        /* assigns the state on transition */
        final AutoState<BitsAdder, Integer> autoState = (AutoState<BitsAdder, Integer>) presentState;
        if (!hasNextCarryState(autoState)) {
            transitionalManager.assignNextState(states.get(0));
        } else {
            transitionalManager.assignNextState(states.get(1));
        }
    }

    /**
     * Tests whether the current state has a next {@link CarryState}.
     * 
     * @param state the state to test against its tracer
     * @return true if the next state is a carry state (i.e: when the tracer is 1), false otherwise.
     */
    private boolean hasNextCarryState(AutoState<BitsAdder, Integer> state) {
        return state.getStateTracer() == 1;
    }
}
