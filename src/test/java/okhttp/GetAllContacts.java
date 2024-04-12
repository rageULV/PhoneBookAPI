package okhttp;

import helpers.PropertiesReader;
import helpers.TestConfig;
import models.ContactListModel;
import models.ContactModel;
import okhttp3.Request;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class GetAllContacts {

    @Test
    public void getAllContactsPositive() throws IOException {

        Request request = new Request.Builder()
            .url(PropertiesReader.getProperty("getAllContacts"))
            .addHeader("Authorization", PropertiesReader.getProperty("token"))
            .build();

        Response response = TestConfig.client.newCall(request).execute();

        String responsBody = response.body().string();

        Assert.assertTrue(response.isSuccessful());

        ContactListModel contacts =
                TestConfig.gson.fromJson(responsBody, ContactListModel.class);

        for(ContactModel contact: contacts.getContacts())
        {
            System.out.println(contact.getName());
            System.out.println(contact.getPhone());
            System.out.println("-------------------------");
        }
    }
}
