package okhttp;

import helpers.PropertiesReader;
import helpers.PropertiesWriter;
import helpers.PropertiesWriterXML;
import helpers.TestConfig;
import models.*;
import okhttp3.Request; // класс Request из библиотеки okhttp3 используется для создания HTTP-запросов.
import okhttp3.RequestBody; //класс RequestBody из библиотеки okhttp3 представляет тело HTTP-запроса.
import okhttp3.Response; // класс Response из библиотеки okhttp3 представляет ответ на HTTP-запрос.
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import static helpers.TestConfig.gson;
import static helpers.TestHelper.FILE_PATH;

public class LoginTest {

    @Test
    public void loginPositive() throws IOException {
        // AuthenticationRequestModel класс представляет модель запроса аутентификации.
        AuthenticationRequestModel requestModel = AuthenticationRequestModel
                .username(PropertiesReader.getProperty("existingUserEmail"))
                .password(PropertiesReader.getProperty("existingUserPassword")); // Создается экземпляр класса AuthenticationRequestModel с заданными данными пользователя для аутентификации (email и пароль).
        System.out.println("REQUEST: "+requestModel.getUsername()+" : "+requestModel.getPassword());

        RequestBody requestBody = RequestBody
                .create(gson.toJson(requestModel),
                        TestConfig.JSON); // Создается объект RequestBody, содержащий данные запроса в формате JSON.

        Request request = new Request.Builder()
                .url(PropertiesReader.getProperty("urlPassAndLogin"))
                .post(requestBody)
                .build();
        System.out.println("REQ "+request.toString());
        // Создается объект Request, представляющий HTTP POST-запрос к указанному URL с телом,
        // содержащим данные аутентификации. Класс Request является частью библиотеки OkHttp,
        // которая предоставляет удобный способ для работы с сетевыми запросами. в итоге у вас получается объект Request,
        // который готов к отправке на сервер с заданными параметрами: методом POST, URL-адресом и телом запроса.
        Response response = TestConfig.client.newCall(request).execute(); // Выполняется HTTP-запрос с помощью объекта client из класса TestConfig, и возвращается объект Response т.е. какой-то ответ.
        System.out.println("Response code: "+response.code());
// если ответ содержит код 2** , то все прошло успешно.
        if(response.isSuccessful()){
            // И теперь можно десериализовать в модель объекта с которым можно работать дальше. т.е. из объекта JSON получили объект класса AuthenticationRequestModel

            AuthenticationResponseModel responseModel =
                    gson.fromJson(response.body().string(),
                            AuthenticationResponseModel.class);

            PropertiesWriterXML propertiesWriterXML = new PropertiesWriterXML(FILE_PATH);
            propertiesWriterXML.setProperties("token",responseModel.getToken(),false);

            Assert.assertTrue(response.isSuccessful());
        }
        else {
            System.out.println("Status code : "+response.code());
            ErrorModel errorModel = gson.fromJson(response.body().string(), ErrorModel.class);
            System.out.println("\u001B[32mError status: "+errorModel.getStatus());
            Assert.assertTrue(response.isSuccessful());
        }


    }
}