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

package com.avrsandbox.fsa.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Defines a general-purpose utility for logging events, use
 * {@link AutomataLogger#setEnabled(boolean)} to control whether to
 * enable or disable this utility.
 *
 * @author pavl_g
 */
public final class AutomataLogger {

    private static final Logger logger = Logger.getLogger("Automata4j-core");
    private static boolean enabled;

    private AutomataLogger() {
    }

    /**
     * Logs an event with a logging level and a message.
     *
     * @param level the logging level
     * @param msg a message to log
     */
    public static void log(Level level, String msg) {
        if (!enabled) {
            return;
        }
        logger.log(level, msg);
    }

    /**
     * Logs a throwable event with a logging level and a message.
     *
     * @param level the logging level
     * @param msg a message to log
     * @param throwable a throwable event
     */
    public static void log(Level level, String msg, Throwable throwable) {
        if (!enabled) {
            return;
        }
        logger.log(level, msg, throwable);
    }

    /**
     * Enables/Disables the framework event logger.
     *
     * @param enabled true to enable, false otherwise
     */
    public static void setEnabled(boolean enabled) {
        AutomataLogger.enabled = enabled;
    }

    /**
     * Tests whether the framework event logger is enabled.
     *
     * @return true if the event logger is enabled, false otherwise
     */
    public static boolean isEnabled() {
        return enabled;
    }
}
