package caju.transaction.component.extensions

import io.restassured.RestAssured.given
import io.restassured.http.ContentType.JSON
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSpecification
import org.springframework.http.HttpStatus

fun StartMocks(block: RequestSpecification.() -> Unit): Unit = given().run(block)

infix fun Unit.GivenByAsn(block: RequestSpecification.() -> RequestSpecification): RequestSpecification = given().run(block)

fun GivenByAsn(block: RequestSpecification.() -> RequestSpecification): RequestSpecification = given().run(block)

fun RequestSpecification.givenDefault(): RequestSpecification =
    this.with()
        .contentType(JSON)
        .log()
        .all()

fun ValidatableResponse.assertThatStatusCodeIsEqualTo(status: HttpStatus): ValidatableResponse =
    this.log()
        .all()
        .statusCode(status.value())

