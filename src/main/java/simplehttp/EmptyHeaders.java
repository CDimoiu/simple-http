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

package simplehttp;

import java.util.Iterator;

public class EmptyHeaders implements Headers {

    public static EmptyHeaders emptyHeaders() {
        return new EmptyHeaders();
    }

    private EmptyHeaders() {
    }

    @Override
    public Iterator<Header> iterator() {
        return new EmptyIterator<>();
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public boolean has(String header) {
        return false;
    }

    @Override
    public Header get(String header) {
        return new NoHeader();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        return other != null && getClass() == other.getClass();
    }
}
