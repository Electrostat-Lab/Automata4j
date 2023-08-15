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

package com.avrsandbox.fsa.example.simple.ndfsa;

import java.util.logging.Logger;
import java.util.logging.Level;
import com.avrsandbox.fsa.core.state.AutoState;

/**
 * A non-carry adder state.
 *
 * @author pavl_g
 */
public final class NonCarryState implements AutoState<BitsAdder, Integer> {
    
    private BitsAdder adder;
    private Integer carry = 0;
    private final Logger LOGGER = Logger.getLogger(CarryState.class.getName());
    
    @Override
    public void invoke(BitsAdder adder) {
        this.adder = adder;
        adder.output = adder.add();

        if (adder.output == 2) {
            // output = 0 and carry = 1
            adder.output = 0;
            carry = 1;
        }/* else, carry = 0 and output = 1 :-) */
        
        LOGGER.log(Level.INFO, "Present-State = NonCarryState ; " + "X1/X2 = Z"  + " ; " + adder.input0 + "/" + adder.input1 + " = " + adder.output);
    }

    @Override
    public BitsAdder getInput() {
        return this.adder;
    }

    @Override
    public void setInput(BitsAdder input) {
        this.adder = input;
    }

    @Override
    public Integer getStateTracer() {
        return carry;
    }
    
    @Override
    public void onStart() {
        
    }

    @Override
    public void onFinish() {
        // reset values and/or release resources
        carry = 0;
        this.adder = null;
    }
}
