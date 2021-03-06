/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.glassfish.jersey.tests.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Pavel Bucek (pavel.bucek at oracle.com)
 */
public class OptionsTest extends JerseyTest {

    @Override
    protected ResourceConfig configure() {
        return new ResourceConfig(HttpOptionsTest.class);
    }

    @Path("/OptionsTest")
    public static class HttpOptionsTest {

        static String html_content =
                "<html><head><title>get text/html</title></head>"
                        + "<body>get text/html</body></html>";

        @GET
        public Response getPlain() {
            return Response.ok("CTS-get text/plain").header("TEST-HEAD", "text-plain")
                    .build();
        }

        @GET
        @Produces("text/html")
        public Response getHtml() {
            return Response.ok(html_content).header("TEST-HEAD", "text-html")
                    .build();
        }

        @GET
        @Path("/sub")
        public Response getSub() {
            return Response.ok("TEST-get text/plain").header("TEST-HEAD",
                    "sub-text-plain")
                    .build();
        }

        @GET
        @Path("/sub")
        @Produces(value = "text/html")
        public Response headSub() {
            return Response.ok(html_content).header("TEST-HEAD", "sub-text-html")
                    .build();
        }
    }

    /*
     * Client invokes OPTIONS on a sub resource at /OptionsTest/sub;
     * which no request method designated for OPTIONS.
     * Verify that an automatic response is generated.
     */
    @Test
    public void OptionSubTest() {
        final Response response = target().path("/OptionsTest/sub").request(MediaType.TEXT_HTML_TYPE).options();

        assertTrue(response.getAllowedMethods().contains("GET"));
        assertTrue(response.getAllowedMethods().contains("HEAD"));
        assertTrue(response.getAllowedMethods().contains("OPTIONS"));

        assertEquals(response.getMediaType(), MediaType.TEXT_HTML_TYPE);
    }
}
