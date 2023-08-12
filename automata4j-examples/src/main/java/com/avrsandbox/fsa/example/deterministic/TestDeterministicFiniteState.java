package com.avrsandbox.fsa.example.deterministic;

import com.avrsandbox.fsa.core.TransitionalManager;
import com.avrsandbox.fsa.core.deterministic.DeterministicManager;
import com.avrsandbox.fsa.core.state.AutoState;
import com.avrsandbox.fsa.util.TransitionPath;

/**
 * Tests the Deterministic Finite-State-Automaton Pattern through using {@link DeterministicManager}
 * in which a-successor transition between 2 AutoStates must not be repeated.
 *
 * @author pavl_g
 */
public final class TestDeterministicFiniteState {
    public static void main(String[] args) {

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
        transitionalManager.transit(transitionPath, null);

        transitionPath.setName(transitionPath.getName() + "2");
        transitionalManager.transit(transitionPath, null);
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
            tracer = "Armature is " + input;
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
