/*
 * Copyright (c) 2011-2018, simple-http committers
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

package simplehttp.matchers;

import org.hamcrest.StringDescription;
import org.junit.Test;
import simplehttp.Header;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static simplehttp.HeaderPair.header;
import static simplehttp.matchers.Matchers.equalTo;

public class HeaderEqualityMatcherTest {

    private final Header header = header("Accept", "application/json, q=1; text/plain, q=0.5");

    @Test
    public void exampleUsage() {
        assertThat(header, is(equalTo(header("Accept", "application/json, q=1; text/plain, q=0.5"))));
    }

    @Test
    public void matches() {
        assertThat(equalTo(header("Accept", "application/json, q=1; text/plain, q=0.5")).matches(header), is(true));
    }

    @Test
    public void matchesAgainstHeaderImplementationWithoutEqualityImplemented() {
        assertThat(equalTo(header("Accept", "application/json, q=1; text/plain, q=0.5")).matches(new HeaderWithoutEqualityImplemented()), is(true));
    }

    @Test
    public void doesNotMatch() {
        assertThat(equalTo(header("Accept", "application/json, q=1")).matches(header), is(false));
        assertThat(equalTo(header("accept", "application/json, q=1; text/plain, q=0.5")).matches(header), is(false));
    }

    @Test
    public void description() {
        StringDescription description = new StringDescription();
        equalTo(header("Accept", "application/json, q=1; text/plain, q=0.5")).describeTo(description);
        assertThat(description.toString(), containsString("application/json, q=1; text/plain, q=0.5"));
    }

    private static class HeaderWithoutEqualityImplemented implements Header {
        @Override
        public String name() {
            return "Accept";
        }

        @Override
        public String value() {
            return "application/json, q=1; text/plain, q=0.5";
        }
    }
}
