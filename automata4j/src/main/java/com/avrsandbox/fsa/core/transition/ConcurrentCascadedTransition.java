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
import java.util.Queue;

/**
 * A thread-safe implementation of the {@link CascadedTransition} queue adapter pattern.
 *
 * @param <I> the autoStates input type
 * @param <O> the autoStates output type
 * @author pavl_g
 * @see CascadedTransition for non-concurrent operations
 */
@SuppressWarnings("all")
public class ConcurrentCascadedTransition<I, O> extends CascadedTransition<I, O> {

    /**
     * Instantiates a thread-safe cascaded transition with the default queue implementation:
     * {@link ArrayDeque}.
     *
     * @param name the name of this transition path cascade
     */
    public ConcurrentCascadedTransition(String name) {
        super(name);
    }

    /**
     * Instantiates a thread-safe cascaded transition with the a specified queue implementation
     * from {@link CascadedTransition.QueueImplementation}.
     *
     * @param name the name of this transition path cascade
     * @param queueImplementation the implementation object of type {@link Queue} from the popular
     *                            enum {@link CascadedTransition.QueueImplementation}
     */
    public ConcurrentCascadedTransition(String name, CascadedTransition.QueueImplementation queueImplementation) {
        super(name, queueImplementation);
    }

    /**
     * Represents the base constructor to instantiate a thread-safe cascaded transition path
     * with the a user queue implementation.
     *
     * @param name the name of this transition path cascade
     * @param queue a user implementation of the type {@link Queue}
     */
    public ConcurrentCascadedTransition(String name, Queue<AutoState> queue) {
        super(name, queue);
    }

    @Override
    public synchronized void assignNextState(AutoState<I, O> nextState) throws NextStateNotFoundException {
        super.assignNextState(nextState);
    }

    @Override
    public synchronized AutoState<I, O> getNextState() throws NextStateNotFoundException {
        return super.getNextState();
    }
}
