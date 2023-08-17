package diplom2.user;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static diplom2.constants.ApiPath.*;

public class UserSteps {
    public static RequestSpecification getRequest() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri("https://stellarburgers.nomoreparties.site");
    }
    @Step("Создание пользователя")
    public ValidatableResponse newUser(User user) {
        return getRequest()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(REGISTER_NEW_USER)
                .then();
    }
    @Step("Создание пользователя с неполными данными")
    public ValidatableResponse createUserWithoutFullInfo(User user){
        return getRequest()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(REGISTER_NEW_USER)
                .then();
    }

    @Step("Логин существующего пользователя")
    public ValidatableResponse loginRealUser(User user){
        return getRequest()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(LOGIN_USER)
                .then();
    }
    @Step("Изменение данных авторизованного пользователя")
    public ValidatableResponse setUserData(String accessToken, User user) {
        return getRequest()
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(SET_USER_DATA)
                .then();
    }

    @Step("Изменение данных неавторизованного пользователя")
    public ValidatableResponse setNotAuthUserData(User user) {
        return getRequest()
                .body(user)
                .when()
                .patch(SET_USER_DATA)
                .then();
    }
    @Step("Выход из аккаунта")
    public ValidatableResponse logoutUser(String refreshToken, User user) {
        return getRequest()
                .header("application/json", refreshToken)
                .body(user)
                .when()
                .post(LOGOUT)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse userDelete(String accessToken){
        return getRequest()
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth" + "/" + accessToken)
                .then();
    }
}
