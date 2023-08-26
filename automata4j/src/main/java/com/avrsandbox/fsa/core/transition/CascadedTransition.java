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

import com.avrsandbox.fsa.core.state.AutoState;
import com.avrsandbox.fsa.core.state.NextStateNotFoundException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Provides an implementation of the {@link Queue} ADT for automata4j transitions.
 *
 * <p>
 * This implementation adapts a {@link TransitionPath} to accommodate a present-state and
 * next-states by cascading them using a Queue object.
 * </p>
 *
 * <p>
 * A Queue is an abstract data structure that operates primarily in a first-in-first-out (FIFO) principle,
 * it could be used to enqueue {@link AutoState}s to the {@link com.avrsandbox.fsa.core.TransitionalManager}.
 * </p>
 *
 * <p>
 * The Queue data structure algorithms are implementation-wise, a deque is a double-ended queue that
 * accepts both logics (FIFO and LIFO), however the implementation on the {@link com.avrsandbox.fsa.core.TransitionalManager}
 * side uses ONLY the FIFO principle.
 * </p>
 *
 * @param <I> the autoStates input type
 * @param <O> the autoStates output type
 * @author pavl_g
 * @see ConcurrentCascadedTransition for a thread-safe implementation
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class CascadedTransition<I, O> extends TransitionPath<I, O> {

    /**
     * An abstract Queue object.
     */
    protected Queue<AutoState> autoStatesCascade;

    /**
     * Instantiates a cascaded transition with the default queue implementation:
     * {@link ArrayDeque}.
     *
     * @param name the name of this transition path cascade
     */
    public CascadedTransition(String name) {
        this(name, QueueImplementation.ArrayDeque.getQueueObject());
    }

    /**
     * Instantiates a cascaded transition with the a specified queue implementation
     * from {@link CascadedTransition.QueueImplementation}.
     *
     * @param name the name of this transition path cascade
     * @param queueImplementation the implementation object of type {@link Queue} from the popular
     *                            enum {@link CascadedTransition.QueueImplementation}
     */
    public CascadedTransition(String name, CascadedTransition.QueueImplementation queueImplementation) {
        this(name, queueImplementation.getQueueObject());
    }

    /**
     * Represents the base constructor to instantiate a cascaded transition path
     * with the a user queue implementation.
     *
     * @param name the name of this transition path cascade
     * @param queue a user implementation of the type {@link Queue}
     */
    public CascadedTransition(String name, Queue<AutoState> queue) {
        super(name);
        autoStatesCascade = queue;
    }

    /**
     * Assigns an {@link AutoState} at the end of the cascade.
     *
     * @param nextState the next-state object to assign
     * @throws NextStateNotFoundException if the input is null or the offer operation has failed
     */
    @Override
    public void assignNextState(AutoState<I, O> nextState) throws NextStateNotFoundException {
        if (!autoStatesCascade.offer(nextState)) {
            throw new NextStateNotFoundException();
        }
        super.assignNextState(nextState);
    }

    /**
     * Assigns an {@link AutoState} at the start of the cascade, only if the cascade is of type
     * {@link Deque}.
     *
     * @param presentState the state object to assign at the beginning of the cascade
     * @throws UnsupportedOperationException if the {@link CascadedTransition#getAutoStatesCascade()} is not
     *                                       of type {@link Deque}
     * @throws RuntimeException if the operation has failed
     */
    @Override
    public void assignPresentState(AutoState<I, O> presentState) throws UnsupportedOperationException, RuntimeException {
        assertCascadeOfTypeDeque();
        if (!((Deque<AutoState>) autoStatesCascade).offerFirst(presentState)) {
            throw new RuntimeException("Assigning present state has failed!");
        }
        super.assignPresentState(presentState);
    }

    /**
     * Polls the first state in a First-in-First-out (FIFO) order returning a reference
     * to the polled state.
     *
     * <p>
     * This method and the method {@link CascadedTransition#getNextState()}
     * are fairly equivalent in this implementation, however the abstraction of the {@link com.avrsandbox.fsa.core.TransitionalManager}
     * requires both logic to be present, thus they are not duplicates.
     * </p>
     *
     * @return a reference to the polled auto state.
     */
    @Override
    public AutoState<I, O> getPresentState() {
        return this.presentState = autoStatesCascade.poll();
    }

    /**
     * Polls the first state in a First-in-First-out (FIFO) order returning a reference
     * to the polled state.
     *
     * <p>
     * This method and the method {@link CascadedTransition#getPresentState()}
     * are fairly equivalent in this implementation, however the abstraction of the {@link com.avrsandbox.fsa.core.TransitionalManager}
     * requires both logic to be present, thus they are not duplicates.
     * </p>
     *
     * @return a reference to the polled auto state.
     */
    @Override
    public AutoState<I, O> getNextState() {
        return this.nextState = autoStatesCascade.poll();
    }

    /**
     * Polls the last state in the cascade, and returns it.
     *
     * @return a reference to the last state in the cascade
     */
    public AutoState<I, O> getLastState() throws UnsupportedOperationException {
        assertCascadeOfTypeDeque();
        final Deque<AutoState> deque = (Deque<AutoState>) autoStatesCascade;
        final AutoState autoState = deque.pollLast();
        if (autoState == null) {
            throw new RuntimeException("Failed to poll and retrieve the last state!");
        }
        return autoState;
    }

    /**
     * Retrieves the queue of the {@link AutoState}s.
     *
     * @return the concrete queue object
     */
    public Queue<AutoState> getAutoStatesCascade() {
        return autoStatesCascade;
    }

    /**
     * Asserts the {@link CascadedTransition#getAutoStatesCascade()} is of type {@link Deque}
     * throwing an {@link UnsupportedOperationException} if it doesn't match a Deque.
     *
     * @throws UnsupportedOperationException if the queue object isn't a deque
     */
    protected void assertCascadeOfTypeDeque() {
        if (!(autoStatesCascade instanceof Deque)) {
            throw new UnsupportedOperationException("The cascade isn't of a Deque type!");
        }
    }

    /**
     * Houses the most popular {@link Queue} implementation.
     */
    public enum QueueImplementation {

        /**
         * Defines a new {@link ArrayDeque} implementation in which
         * the {@link AutoState}s are stored in a circular array data structure.
         *
         * <p>
         * In an array deque, {@link AutoState}s can be inserted from either sides
         * of the {@link Queue} in an array data structure.
         * </p>
         */
        ArrayDeque(new ArrayDeque<>()),

        /**
         * Defines a new {@link LinkedList} implementation in which
         * the {@link AutoState}s are stored in double-ended queue.
         *
         * <p>
         * In a linked list, {@link AutoState}s can be inserted from either sides
         * of the {@link Queue} in a LinkedList data structure (Nodal System).
         * </p>
         */
        LinkedDeque(new LinkedList<>());

        private final Queue<AutoState> queue;

        /**
         * Instantiates a queue data structure implementation for this cascade.
         *
         * @param queue the queue concrete object
         */
        QueueImplementation(final Queue<AutoState> queue) {
            this.queue = queue;
        }

        /**
         * Retrieves the queue concrete object.
         *
         * @return the queue implementation object
         */
        public Queue<AutoState> getQueueObject() {
            return queue;
        }
    }
}