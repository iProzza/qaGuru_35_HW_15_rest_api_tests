package demoqa.api.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.*;

public class CrudResponseSpecs {

    public static ResponseSpecification addBookToProfileResponseSpec201 = new ResponseSpecBuilder()
            .expectStatusCode(201)
            .log(ALL)
            .build();

    public static ResponseSpecification deleteAllBooksFromProfileByIdResponseSpec204 = new ResponseSpecBuilder()
            .expectStatusCode(204)
            .log(ALL)
            .build();


    public static ResponseSpecification getAccountUserBooksByIdResponseSpec200 = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(ALL)
            .build();

}
