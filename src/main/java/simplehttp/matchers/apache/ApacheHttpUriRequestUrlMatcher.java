/*
 * Copyright (c) 2011-2019, simple-http committers
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package simplehttp.matchers.apache;

import org.apache.http.client.methods.HttpUriRequest;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.net.URL;
import java.util.concurrent.Callable;

public class ApacheHttpUriRequestUrlMatcher extends TypeSafeMatcher<HttpUriRequest> {

    private final URL url;

    @Factory
    public static Matcher<HttpUriRequest> requestWith(URL url) {
        return new ApacheHttpUriRequestUrlMatcher(url);
    }

    public ApacheHttpUriRequestUrlMatcher(URL url) {
        this.url = url;
    }

    @Override
    public boolean matchesSafely(HttpUriRequest request) {
        return wrapAsRuntimeException(() -> url.toExternalForm().equals(request.getURI().toURL().toExternalForm()));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Url contains ").appendValue(url);
    }

    private static <V> V wrapAsRuntimeException(Callable<V> callable) throws RuntimeException {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
