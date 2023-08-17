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

public class CreateUserTest {
    private String token;
    private User user;
    private UserSteps userSteps;

    @Before
    @Step("Тестовые данные")
    public void setUp() {
        userSteps = new UserSteps();
        user = new User("nm0xk9878660gfu@yandex.ru", "1879834", "Lola");
    }
    @Test
    @DisplayName("Регистрация нового пользователя")
    @Description("Проверяем, что можно создать нового пользователя, если все поля заполнены корректно")
    public void createNewUserTest() {
        ValidatableResponse response = userSteps.newUser(user);
        token = response.extract().path("accessToken");
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
    @DisplayName("Регистрация дублирующего пользователя")
    @Description("Проверяем, что нельзя создать дублирующего пользователя")
    public void createTwoIdenticalUserTest() {
        user = new User("test-data@yandex.ru", "password", "Username");
        ValidatableResponse responseCreateIdentical = userSteps.newUser(user);
        responseCreateIdentical.assertThat()
                .body("success", Matchers.equalTo(false))
                .and()
                .body("message", Matchers.equalTo("User already exists"))
                .and()
                .statusCode(403);
    }


    @Test
    @DisplayName("Регистрация пользователя, не заполнив все обязательные поля")
    @Description("Проверяем, что нельзя создать нового пользователя не заполнив поле Почта")
    public void createUserWithoutMail() {
        user = new User("", "1k29834", "Lola");
        ValidatableResponse validatableResponse = userSteps.createUserWithoutFullInfo(user);
        validatableResponse.assertThat()
                .body("success", Matchers.equalTo(false))
                .and()
                .body("message", Matchers.equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Регистрация пользователя, не заполнив все обязательные поля")
    @Description("Проверяем, что нельзя создать нового пользователя не заполнив поле Пароль")
    public void createUserWithoutPassword() {
        user = new User("0vdt6bh7gfu@yandex.ru", "", "Lola");
        ValidatableResponse validatableResponse = userSteps.createUserWithoutFullInfo(user);
        validatableResponse.assertThat()
                .body("success", Matchers.equalTo(false))
                .and()
                .body("message", Matchers.equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Регистрация пользователя, не заполнив все обязательные поля")
    @Description("Проверяем, что нельзя создать нового пользователя не заполнив поле Имя")
    public void createUserWithoutName() {
        user = new User("0vhxdobh7g11u@yandex.ru", "766nhdd", "");
        ValidatableResponse validatableResponse = userSteps.createUserWithoutFullInfo(user);
        validatableResponse.assertThat()
                .body("success", Matchers.equalTo(false))
                .and()
                .body("message", Matchers.equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
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
