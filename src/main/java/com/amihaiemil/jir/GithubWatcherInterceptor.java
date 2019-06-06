/**
 * Copyright (c) 2019, Mihai Emil Andronache
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1)Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 2)Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 3)Neither the name of jakartaee-issue-reporter nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.amihaiemil.jir;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Interceptor for the {@link GithubWatcher} annotation.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @checkstyle DesignForExtension (200 lines)
 * @checkstyle ReturnCount (200 lines)
 * @checkstyle IllegalCatch (200 lines)
 * @todo #6:30min Continue implementing a service which reads the
 *  Github coordinates (token + repo) from the system properties or
 *  from the environment and opens a Github ticket.
 * @todo #6:30min Implement logging sing slf4J. If the annotation's
 *  "log" flag is set to true, the exception should be logged. Also,
 *   it shouldn't be possible to have both "rethrow" and "log" set to
 *   false (the exception would completely be swalloed.
 */
@GithubWatcher
@Interceptor
public class GithubWatcherInterceptor {

    /**
     * Intercept it.
     * @param context Interceptor invocation context.
     * @return The result of the method's call.
     * @throws Exception If something goes wrong.
     */
    @AroundInvoke
    public Object intercept(final InvocationContext context) throws Exception {
        final GithubWatcher annotation = this.fetchBinding(context);
        Object result = null;
        try {
            result = context.proceed();
        } catch (final Exception ex) {
            if(annotation.rethrow()) {
                throw ex;
            }
        }
        return result;
    }
    
    /**
     * Get the binding annotation.
     * @param context The interceptor's invocation context.
     * @return The GithubWatcher binding annotation.
     */
    private GithubWatcher fetchBinding(final InvocationContext context) {
        GithubWatcher annotation = context.getMethod().getAnnotation(
            GithubWatcher.class
        );
        if (annotation != null) {
            return annotation;
        }
        return context.getMethod().getDeclaringClass().getAnnotation(
            GithubWatcher.class
        );
    }

}
