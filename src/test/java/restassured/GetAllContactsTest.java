package restassured;

import helpers.PropertiesReader;
import helpers.TestHelper;
import models.ContactListModel;
import models.ContactModel;
import org.testng.annotations.Test;

import java.io.*;

import static io.restassured.RestAssured.given;

public class GetAllContactsTest implements TestHelper {
    @Test
    public void getAllContactsPositive() throws IOException {

        File logFile = new File("src/logs/testresult.log");
        if(!logFile.exists()){
            logFile.getParentFile().mkdirs();
            logFile.createNewFile();
        }
        PrintStream printStream = new PrintStream(new FileOutputStream(logFile));
        System.setOut(printStream);
        System.setErr(printStream);


        ContactListModel contactList = given()
                .header(AuthorizationHeader, PropertiesReader.getProperty("token"))
                .when()
                .get(PropertiesReader.getProperty("getAllContacts"))
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(ContactListModel.class);

        for (ContactModel contact : contactList.getContacts()){
            System.out.println(contact.getEmail());
            System.out.println(contact.getId());
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
        }
        printStream.close();

    }
}