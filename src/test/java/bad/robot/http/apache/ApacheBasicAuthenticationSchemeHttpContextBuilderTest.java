/*
 * Copyright (c) 2011-2012, bad robot (london) ltd
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

package bad.robot.http.apache;

import org.apache.http.auth.AuthScheme;
import org.apache.http.client.AuthCache;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.protocol.HttpContext;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static bad.robot.http.apache.ApacheBasicAuthenticationSchemeHttpContextBuilder.anApacheBasicAuthScheme;
import static bad.robot.http.apache.Coercions.asHttpHost;
import static org.apache.http.client.protocol.ClientContext.AUTH_CACHE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * The {@link AuthCache} just gives an {@link AuthScheme} back for a given URL. This means it gives back, say, a scheme
 * representing "basic auth" for URL http://localhost. The actual credentials are setup in the {@link org.apache.http.client.CredentialsProvider}
 * elsewhere.
 */
public class ApacheBasicAuthenticationSchemeHttpContextBuilderTest {

    private final String username = "username";
    private final String password = "password";

    @Test
    public void addsAnAuthenticationSchemeForCredentials() throws MalformedURLException {
        URL google = new URL("http://www.google.com");
        URL github = new URL("http://www.github.com");
        HttpContext context = anApacheBasicAuthScheme()
            .withCredentials(username, password, google)
            .build();
        assertThat(schemeFromCacheByUrl(google, context), is(instanceOf(BasicScheme.class)));
        assertThat(schemeFromCacheByUrl(github, context), is(nullValue()));
    }

    @Test
    public void addsAuthenticationSchemesForCredentialsDifferingInPortNumbers() throws MalformedURLException {
        URL google1 = new URL("http://www.google.com:80");
        URL google2 = new URL("http://www.google.com:8081");
        HttpContext context = anApacheBasicAuthScheme()
            .withCredentials(username, password, google1)
            .withCredentials(username, password, google2)
            .build();
        assertThat(schemeFromCacheByUrl(google1, context), is(instanceOf(BasicScheme.class)));
        assertThat(schemeFromCacheByUrl(google2, context), is(instanceOf(BasicScheme.class)));
    }

    @Test
    public void addsAuthenticationSchemesForCredentialsDifferingInProtocol() throws MalformedURLException {
        URL google1 = new URL("http://www.google.com");
        URL google2 = new URL("https://www.google.com");
        HttpContext context = anApacheBasicAuthScheme()
            .withCredentials(username, password, google1)
            .withCredentials(username, password, google2)
            .build();
        assertThat(schemeFromCacheByUrl(google1, context), is(instanceOf(BasicScheme.class)));
        assertThat(schemeFromCacheByUrl(google2, context), is(instanceOf(BasicScheme.class)));
    }

    private static AuthScheme schemeFromCacheByUrl(URL url, HttpContext context) {
        AuthCache cache = (AuthCache) context.getAttribute(AUTH_CACHE);
        return cache.get(asHttpHost(url));
    }
}
