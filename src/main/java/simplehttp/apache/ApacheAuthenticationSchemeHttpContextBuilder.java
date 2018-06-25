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

package simplehttp.apache;

import org.apache.http.client.AuthCache;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import simplehttp.Builder;
import simplehttp.configuration.ConfigurableHttpClient;

import java.net.URL;

import static org.apache.http.client.protocol.ClientContext.AUTH_CACHE;
import static simplehttp.apache.Coercions.asHttpHost;

/**
 * Build a "local context" to use with individual HTTP verbs. The {@link HttpContext} is used to share information, in
 * this case, which authentication scheme the client should be using (based on the target URL).
 */
public class ApacheAuthenticationSchemeHttpContextBuilder implements Builder<HttpContext>, ConfigurableHttpClient {

    private final BasicHttpContext localContext = new BasicHttpContext();
    private final AuthCache authenticationSchemes = new BasicAuthCache();

    private ApacheAuthenticationSchemeHttpContextBuilder() {
    }

    public static ApacheAuthenticationSchemeHttpContextBuilder anApacheBasicAuthScheme() {
        return new ApacheAuthenticationSchemeHttpContextBuilder();
    }

    @Override
    public ApacheAuthenticationSchemeHttpContextBuilder withBasicAuthCredentials(String username, String password, URL url) {
        authenticationSchemes.put(asHttpHost(url), new BasicScheme());
        return this;
    }

    @Override
    public ApacheAuthenticationSchemeHttpContextBuilder withOAuthCredentials(String accessToken, URL url) {
        authenticationSchemes.put(asHttpHost(url), new BearerScheme());
        return this;
    }

    @Override
    public HttpContext build() {
        localContext.setAttribute(AUTH_CACHE, authenticationSchemes);
        return localContext;
    }

}
