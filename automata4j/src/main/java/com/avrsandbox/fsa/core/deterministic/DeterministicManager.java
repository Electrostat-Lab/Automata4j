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

package com.avrsandbox.fsa.core.deterministic;

import com.avrsandbox.fsa.core.TransitionalManager;
import com.avrsandbox.fsa.core.state.AutoState;
import com.avrsandbox.fsa.core.state.TransitionListener;
import com.avrsandbox.fsa.util.TransitionPath;
import java.util.HashMap;
import java.util.Map;

/**
 * A deterministic version of the {@link TransitionalManager} to enable implementing the
 * Deterministic Finite-State-Automaton pattern (DFSA).
 *
 * <p>
 * The only way to leak through this pattern and convert it to a non-deterministic form, though not
 * recommended, is through changing the name of the transition path before reusing it.
 * </p>
 *
 * @author pavl_g
 */
public class DeterministicManager extends TransitionalManager {

    /**
     * Keeps track of the previous assigned paths to ensure new transition
     * paths are unique.
     */
    protected Map<String, TransitionPath> paths = new HashMap<>();

    @Override
    public <I, O> void transit(TransitionPath<AutoState<I, O>> transitionPath, TransitionListener transitionListener) {
        if (hasTransitionPath(transitionPath)) {
            throw new TransitionPathNotUniqueException(transitionPath.getName());
        }
        paths.put(transitionPath.getName(), transitionPath);
        super.transit(transitionPath, transitionListener);
    }

    /**
     * Tests whether the system has used this transition path before.
     *
     * <p>
     * This validation can be bypassed by changing the name of the transition
     * path using {@link TransitionPath#setName(String)} before reusing it.
     * </p>
     *
     * @param transitionPath the transition path object to test against
     * @param <I> the autoStates input type
     * @param <O> the autoStates output type
     * @return true if this path is not unique, false otherwise
     */
    protected <I, O> boolean hasTransitionPath(TransitionPath<AutoState<I, O>> transitionPath) {
        if (transitionPath == null) {
            throw new IllegalArgumentException("Cannot accept null transition paths!");
        }
        return paths.get(transitionPath.getName()) != null &&
                (paths.get(transitionPath.getName()) == transitionPath ||
                (paths.get(transitionPath.getName()).getPresentState().hashCode() == transitionPath.getPresentState().hashCode() &&
                paths.get(transitionPath.getName()).getNextState().hashCode() == transitionPath.getNextState().hashCode() &&
                paths.get(transitionPath.getName()).getNextState().getInput().hashCode() == transitionPath.getNextState().getInput().hashCode()));
    }
}
