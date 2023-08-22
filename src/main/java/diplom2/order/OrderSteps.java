package diplom2.order;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static diplom2.constants.ApiPath.*;


public class OrderSteps {
    public static RequestSpecification getRequest() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri("https://stellarburgers.nomoreparties.site");
    }
    @Step("Успешное создание заказа авторизированного пользователя")
    public ValidatableResponse newOrderCreate(String accessToken, Order order) {
        return getRequest()
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(MAKE_ORDER)
                .then();
    }

    @Step("создание заказа неавторизированного пользователя")
    public ValidatableResponse newOrderWithoutAuth(Order order) {
        return getRequest()
                .body(order)
                .when()
                .post(MAKE_ORDER)
                .then();
    }
    @Step("Создание заказа без ингридиентов")
    public ValidatableResponse orderWithoutBody(String accessToken) {
        return getRequest()
                .header("Authorization", accessToken)
                .body("")
                .when()
                .post(MAKE_ORDER)
                .then();
    }

    @Step("Получение заказов авторизированного пользователя")
    public ValidatableResponse getAuthUserOrders(String accessToken) {
        return  getRequest()
                .header("Authorization", accessToken)
                .when()
                .get(GET_USER_ORDERS)
                .then();
    }

    @Step("Получение заказов неавторизированного пользователя")
    public ValidatableResponse getNotAuthUserOrders() {
        return  getRequest()
                .when()
                .get(GET_USER_ORDERS)
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
