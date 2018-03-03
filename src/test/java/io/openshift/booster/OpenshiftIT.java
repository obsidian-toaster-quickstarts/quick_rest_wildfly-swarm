/*
 *
 *  Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.openshift.booster;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.specification.RequestSpecification;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import com.jayway.restassured.RestAssured;
import org.arquillian.cube.openshift.impl.enricher.AwaitRoute;
import org.arquillian.cube.openshift.impl.enricher.RouteURL;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

/**
 * @author Heiko Braun
 */
@RunWith(Arquillian.class)
public class OpenshiftIT {

    @RouteURL("${app.name}")
    @AwaitRoute
    private URL url;

    private RequestSpecification requestSpecification;

    @Before
    public void setup() {
        String uri = url.toString() + "api/greeting";
        requestSpecification = new RequestSpecBuilder()
            .setBaseUri(uri).build();
    }

    @Test
    public void testServiceInvocation() {
        given()
            .spec(requestSpecification)
        .when()
            .get()
        .then()
            .statusCode(200)
            .body(containsString("Hello, World!"));
    }

    @Test
    public void testServiceInvocationWithParam() {
        given()
            .spec(requestSpecification)
            .queryParam("name", "Peter")
        .when()
            .get()
        .then()
            .statusCode(200)
            .body(containsString("Hello, Peter!"));
    }
}
