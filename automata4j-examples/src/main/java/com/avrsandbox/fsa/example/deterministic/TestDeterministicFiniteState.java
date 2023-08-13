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

package com.avrsandbox.fsa.example.deterministic;

import com.avrsandbox.fsa.core.TransitionalManager;
import com.avrsandbox.fsa.core.deterministic.DeterministicManager;
import com.avrsandbox.fsa.core.state.AutoState;
import com.avrsandbox.fsa.core.state.TransitionListener;
import com.avrsandbox.fsa.util.AutomataLogger;
import com.avrsandbox.fsa.util.TransitionPath;

/**
 * Tests the Deterministic Finite-State-Automaton Pattern through using {@link DeterministicManager}
 * in which a-successor transition between 2 AutoStates must not be repeated.
 *
 * @author pavl_g
 */
public final class TestDeterministicFiniteState {
    public static void main(String[] args) {
        AutomataLogger.setEnabled(true);

        final ArmatureState idleState = new ArmatureState();
        idleState.setInput("Idle");
        final ArmatureState walkingState = new ArmatureState();
        walkingState.setInput("Walking");

        final TransitionPath<AutoState<String, String>> transitionPath =
                                            new TransitionPath<>("Armature-Mover-Map");
        transitionPath.assignPresentState(idleState);
        transitionPath.assignNextState(walkingState);

        final TransitionalManager transitionalManager = new DeterministicManager();
        /* repeat the transition path to assert the TransitionPathNotUniqueException */
        transitionalManager.transit(transitionPath, new TransitionListener() {
            @Override
            public <I, O> void onTransition(AutoState<I, O> presentState) {
                transitionalManager.transit(null);
                transitionalManager.transit(transitionPath, null);
            }
        });
    }

    protected static final class ArmatureState implements AutoState<String, String> {

        private String input;
        private String tracer;

        @Override
        public void onStart() {
            System.out.println("Started an Armature State!");
        }

        @Override
        public void invoke(String input) {
            tracer = "Armature is " + this.input;
            System.out.println(tracer);
        }

        @Override
        public void onFinish() {

        }

        @Override
        public String getInput() {
            return input;
        }

        @Override
        public void setInput(String input) {
            this.input = input;
        }

        @Override
        public String getStateTracer() {
            return tracer;
        }
    }
}
