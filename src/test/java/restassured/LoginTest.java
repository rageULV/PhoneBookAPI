package restassured;

import helpers.EmailGenerator;
import helpers.PasswordStringGenerator;
import helpers.PropertiesWriter;
import helpers.TestHelper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.AuthenticationRequestModel;
import models.AuthenticationResponseModel;
import models.ErrorModel;
import models.NewUserModel;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class LoginTest implements TestHelper{


    @Test
    public void registrationTest(){

        NewUserModel newUserModel = new NewUserModel(EmailGenerator.generateEmail(7,7,3)
                , PasswordStringGenerator.generateString());

        String token = given().body(newUserModel).contentType(ContentType.JSON)
                .when()
                .post(REGISTRATION_PATH)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().path("token");
        System.out.println("Token: "+token);
    }

    @Test
    public void loginTestRestAssured(){
        RestAssured.baseURI = LOGIN_PATH;

        AuthenticationRequestModel authenticationRequestModel =
                AuthenticationRequestModel.username("hurigosutalo@gmail.com").password("!Hurigosutalo2552");

        AuthenticationResponseModel response = given()
                .body(authenticationRequestModel)
                .contentType(ContentType.JSON)
                .when()
                .post().then().log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(AuthenticationResponseModel.class);
        PropertiesWriter.writeProperties("token",response.getToken());

        System.out.println("Token : "+response.getToken());
    }

    @Test
    public void loginNegative(){
        AuthenticationRequestModel authenticationRequestModel =
                AuthenticationRequestModel.username("kjdgfluyouy").password("iytfidSHFDS354!");
        ErrorModel errorModel= given()
                .body(authenticationRequestModel)
                .contentType(ContentType.JSON)
                .when()
                .post(LOGIN_PATH).then().log().all()
                .assertThat()
                .statusCode(401).extract().as(ErrorModel.class);
        System.out.println(errorModel.getError()+" : " +errorModel.getMessage());

    }

}