package user_test;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import diplom2.user.User;
import diplom2.user.UserSteps;

public class SetUserInfoTest {
    private String token;
    private User user;
    private UserSteps userSteps;
    @Before
    @Step("Тестовые данные")
    public void setUp() {
        userSteps = new UserSteps();
        user = new User("0mb565db8fu@yandex.ru", "19877834", "Lola");
        ValidatableResponse response = userSteps.newUser(user);
        token = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Изменение данных авторизированного пользователя")
    @Description("Проверяем, что можно изменить данные авторизированного пользователя")
    public void setDataAuthTest() {
        ValidatableResponse response = userSteps.loginRealUser(user);
        token = response.extract().path("accessToken");
        user = new User("hx6zgnhg92v5u@yandex.ru", "1087533", "Pheoby");
        ValidatableResponse responseSet = userSteps.setUserData(token, user);
        responseSet.assertThat()
                .body("success", Matchers.equalTo(true))
                .and()
                .body("user.email", Matchers.equalTo(user.getEmail()))
                .and()
                .body("user.name", Matchers.equalTo(user.getName()))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Проверяем, что можно изменить данные пользователя без авторизации")
    public void setDataNotAuthTest() {
        user = new User("3eb60nc4d3aszfu@yandex.ru", "1f29834", "Lola");
        ValidatableResponse response = userSteps.newUser(user);
        token = response.extract().path("accessToken");
        user = new User("bxnnz7y699ku@yandex.ru", "98765", "Leonardo");
        ValidatableResponse responseSet = userSteps.setNotAuthUserData(user);
        responseSet.assertThat()
                .body("success", Matchers.equalTo(false))
                .and()
                .body("message", Matchers.equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }

    @After
    @Step("Удаление тестовых данных")
    public void deleteUser() {
        if (!(token == null || token.isEmpty())) {
            userSteps.userDelete(token);
            System.out.println("DELETED");
        } else {
            System.out.println("NOT DELETED");
        }
    }
}
