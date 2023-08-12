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
 * An event-driven functional interface which handles the call to {@link TransitionListener#onTransition(AutoState)},
 * the call is loaded into the stack by invoking {@link TransitionalManager#transit(Object, TransitionListener)}.
 * 
 * Applications should implement this interface to listen for the invocation of the system transition.
 * 
 * @author pavl_g
 */
public interface TransitionListener {
    
    /**
     * Dispatched as a result of commiting a call to {@link TransitionalManager#transit(Object, TransitionListener)}.
     * 
     * Applications should decide how they want to transit to other states from here by selectively assigning them 
     * based on some system conditions, the API provides a carrier for these system conditions on the tracer object
     * {@link AutoState#getStateTracer()} utilizing the abstract factory pattern.
     * 
     * @param <I> the type for the state input
     * @param <O> the type for the state tracer object
     * @param presentState the current running state
     */
    <I, O> void onTransition(final AutoState<I, O> presentState);
}
