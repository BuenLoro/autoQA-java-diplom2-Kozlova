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

public class LoginUserTest {
    private String token;
    private User user;
    private UserSteps userSteps;
    @Before
    @Step("Тестовые данные")
    public void setUp() {
        userSteps = new UserSteps();
        user = new User("3i7v303aszfu@yandex.ru", "1k29834", "Lola");
        ValidatableResponse response = userSteps.newUser(user);
        token = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Логин существующего пользователя")
    @Description("Проверяем, что можно залогиниться под существующим пользователем, если все поля заполнены верно")
    public void loginCorrectTest() {
        ValidatableResponse response = userSteps.loginRealUser(user);
        response.assertThat()
                .body("success", Matchers.equalTo(true))
                .and()
                .body("user.email", Matchers.equalTo(user.getEmail()))
                .and()
                .body("user.name", Matchers.equalTo(user.getName()))
                .and()
                .statusCode(200);
        System.out.println(token);
        }

    @Test
    @DisplayName("Логин пользователя c неверной почтой")
    @Description("Проверяем, что нельзя залогиниться, если заполнить поле Почта с ошибкой")
    public void loginUserWithWrongEmailTest() {
        user = new User("errrormxk8zfu@yandex.ru", "1k29834", "Lola");
        ValidatableResponse response = userSteps.loginRealUser(user);
        response.assertThat()
                .body("success", Matchers.equalTo(false))
                .and()
                .body("message", Matchers.equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Логин пользователя c неверным паролем")
    @Description("Проверяем, что нельзя залогиниться, если заполнить поле Пароль с ошибкой")
    public void loginUserWithWrongPasswordTest() {
        user = new User("3i7v303aszfu@yandex.ru", "108g88ju34", "Lola");
        ValidatableResponse response = userSteps.loginRealUser(user);
        response.assertThat()
                .body("success", Matchers.equalTo(false))
                .and()
                .body("message", Matchers.equalTo("email or password are incorrect"))
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
