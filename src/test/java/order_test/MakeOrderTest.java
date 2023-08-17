package order_test;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import diplom2.order.Order;
import diplom2.order.OrderSteps;
import diplom2.user.User;
import diplom2.user.UserSteps;

public class MakeOrderTest {
    private String token;
    private User user;
    private UserSteps userSteps;
    private Order order;
    private OrderSteps orderSteps;

    @Before
    @Step("Тестовые данные")
    public void setUp() {
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        order = new Order(new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa70"});
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами авторизированным пользователем")
    @Description("Проверяем, что можно создать заказ, если пользователь авторизирован")
    public void makeOrderAuthPassed() {
        user = new User("90g86sh6dn8fu@yandex.ru", "19877834", "Lola");
        ValidatableResponse response = userSteps.newUser(user);
        token = response.extract().path("accessToken");
        ValidatableResponse responseCreateOrder = orderSteps.newOrderCreate(token, order);
        responseCreateOrder.assertThat()
                .statusCode(200)
                .body("name", Matchers.notNullValue())
                .and()
                .body("order.number", Matchers.notNullValue())
                .and()
                .body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами неавторизированным пользователем")
    @Description("Проверяем, что можно создать заказ, если пользователь не авторизирован")
    public void makeOrderWithoutAuth() {
        user = new User("7h6543fs5566u@yandex.ru", "1f29834", "Lola");
        ValidatableResponse response = userSteps.newUser(user);
        token = response.extract().path("accessToken");
        ValidatableResponse responseGetOrder = orderSteps.newOrderWithoutAuth(order);
        responseGetOrder.assertThat()
                .statusCode(200)
                .body("name", Matchers.notNullValue())
                .and()
                .body("order.number", Matchers.notNullValue())
                .and()
                .body("success", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверяем, что нельзя создать заказ без ингредиентов")
    public void makeOrderWithoutIngredients() {
        user = new User("9ksbdgg5n8fu@yandex.ru", "19877834", "Lola");
        ValidatableResponse response = userSteps.newUser(user);
        token = response.extract().path("accessToken");
        ValidatableResponse responseNoIngredients = orderSteps.orderWithoutBody(token);
        responseNoIngredients.assertThat()
                .statusCode(400)
                .body("success", Matchers.equalTo(false))
                .and()
                .body("message", Matchers.equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с опечатками в хэшах")
    @Description("Проверяем, что нельзя создать заказ с опечатками в хэшах")
    public void makeOrderWithWrongHash() {
        user = new User("xbxdb68dn8fu@yandex.ru", "19984834", "Lola");
        ValidatableResponse response = userSteps.newUser(user);
        token = response.extract().path("accessToken");
        order = new Order(new String[]{"6asdc5a7111f82001bdaaa6d", "61c005a71d1f82001bdaaa6f", "61c6g6a71d1f82001bdaaa70"});
        ValidatableResponse responseNoHash = orderSteps.newOrderCreate(token, order);
        responseNoHash.assertThat()
                .statusCode(500);
    }

    @After
    @Step("Удаление тестовых данных")
    public void deleteUser() {
        if (!(token == null || token.isEmpty())) {
            orderSteps.userDelete(token);
            System.out.println("DELETED");
        } else {
            System.out.println("NOT DELETED");
        }
    }
}
