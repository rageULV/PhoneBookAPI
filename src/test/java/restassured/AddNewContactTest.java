package restassured;

import helpers.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.ContactModel;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;


public class AddNewContactTest implements TestHelper {

    @Test
    public void addContactPosi () {
        RestAssured.baseURI = ADD_CONTACT_PATH;
        ContactModel contactModel = new ContactModel(
                NameAndLastNameGenerator.generateName(),
                NameAndLastNameGenerator.generateLastName(),
                EmailGenerator.generateEmail(7,5,3),
                PhoneNumberGenerator.generatePhoneNumber(),
                AddressGenerator.generateAddress(),"mydesc");

        String message = given().header(AuthorizationHeader, PropertiesReader.getProperty("token")).
                body(contactModel).
                contentType(ContentType.JSON).
                when().post().then().assertThat().statusCode(200).
                extract().path("message");
        IdExtractor.extractId(message);
        System.out.println("ID :"+message);

    }
}
