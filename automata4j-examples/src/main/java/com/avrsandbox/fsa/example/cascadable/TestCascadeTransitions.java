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

package com.avrsandbox.fsa.example.cascadable;

import com.avrsandbox.fsa.core.state.NextStateAssigner;
import com.avrsandbox.fsa.core.TransitionalManager;
import com.avrsandbox.fsa.core.state.AutoState;
import com.avrsandbox.fsa.core.state.NextStateNotFoundException;
import com.avrsandbox.fsa.core.transition.CascadedTransition;
import com.avrsandbox.fsa.core.transition.TransitionPath;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Examines and Tests the cascading pattern on top of a finite-state {@link TransitionalManager}.
 *
 * @author pavl_g
 */
public final class TestCascadeTransitions {

    public static void main(String[] args) {
        final AutoState<String, String> autoState = new CascadableState();
        final AutoState<String, String> autoState1 = new CascadableState();
        final AutoState<String, String> autoState2 = new CascadableState();
        final AutoState<String, String> autoState3 = new CascadableState();

        autoState.setInput("First State");
        autoState1.setInput("Second State");
        autoState2.setInput("Third State");
        autoState3.setInput("Forth State");

        final TransitionalManager<String, String> transitionalManager = new TransitionalManager<>();
        final TransitionPath<String, String> transitionPath = new CascadedTransition<>("Cascade",
                                                                    CascadedTransition.QueueImplementation.ArrayDeque);
        transitionPath.assignPresentState(autoState);
        transitionPath.assignNextState(autoState1);
        transitionPath.assignNextState(autoState2);
        transitionPath.assignNextState(autoState3);

        /* incrementally assigns and transits to next states */
        transitionalManager.transit(transitionPath, presentState ->
                transit(transitionalManager, transitionPath));
    }

    public static <I extends String, O extends String> void transit(
            TransitionalManager<I, O> transitionalManager,
            TransitionPath<I, O> transitionPath) {
       try {
           transitionalManager.transit(new NextStateAssigner<>(transitionalManager, transitionPath,
                   presentState -> transit(transitionalManager, transitionPath)));
       } catch (NextStateNotFoundException exception) {
           Logger.getLogger(TestCascadeTransitions.class.getName()).
                   log(Level.WARNING, "Dead-end of the finite-states!", exception);
       }
    }

    private static final class CascadableState implements AutoState<String, String> {

        private String input;

        @Override
        public void onStart() {

        }

        @Override
        public void invoke(String input) {
            System.out.println(input);
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
            return null;
        }
    }
}
