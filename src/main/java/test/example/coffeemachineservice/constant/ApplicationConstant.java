package test.example.coffeemachineservice.constant;

public class ApplicationConstant {

    public static final String ERROR_LOG_TEMPLATE = "{}: {} {}";

    public static final String RECIPE_SUCCESS_ADD_MESSAGE = "Рецепт добавлен";

    public static final String RECIPES_NOT_FOUND_MESSAGE = "Рецепты не найдены";

    public static final String RECIPE_NOT_FOUND_MESSAGE = "Рецепт с указанным названием не найден";

    public static final String RECIPE_ALREADY_EXISTS_MESSAGE = "Рецепт с указанным названием уже существует";

    public static final String RECIPE_ID_NOT_FOUND_MESSAGE = "Рецепт с указанным ID не найден";

    public static final String RECIPE_DELETED_MESSAGE = "Рецепт удален";

    public static final String INGREDIENT_SUCCESS_ADD_MESSAGE = "Ингредиент добавлен";

    public static final String INGREDIENTS_NOT_FOUND_MESSAGE = "Ингредиенты не найдены";

    public static final String INGREDIENT_NOT_FOUND_MESSAGE = "Ингредиент с указанным названием не найден";

    public static final String INGREDIENT_ALREADY_EXISTS_MESSAGE = "Ингредиент с указанным названием уже существует";

    public static final String INGREDIENT_ID_NOT_FOUND_MESSAGE = "Ингредиент с указанным ID не найден";

    public static final String INGREDIENT_DELETED_MESSAGE = "Ингредиент удален";

    public static final String DRINK_SUCCESS_ADD_MESSAGE = "Напиток добавлен";

    public static final String DRINKS_NOT_FOUND_MESSAGE = "Напитки не найдены";

    public static final String DRINK_NOT_FOUND_MESSAGE = "Напиток с указанным названием не найден";

    public static final String DRINK_ID_NOT_FOUND_MESSAGE = "Напиток с указанным ID не найден";

    public static final String DRINK_ALREADY_EXISTS_MESSAGE = "Напиток с указанным названием уже существует";

    public static final String DRINK_DELETED_MESSAGE = "Напиток удален";

    public static final String POPULAR_DRINK_NOT_FOUND_MESSAGE = "Самый популярный напиток не найден";

    public static final String NOT_ENOUGH_INGREDIENTS_MESSAGE = "Недостаточно ингредиентов для приготовления напитка";

    public static final String WAIT_UNTIL_READY_MESSAGE = "Напиток готовится, подождите 2 минуты...";

    public static final String ORDERS_NOT_FOUND_MESSAGE = "Заказы не найдены";

    public static final String ORDER_NOT_FOUND_MESSAGE = "Заказ с указанным ID не найден";

    public static final String ORDER_DELETED_MESSAGE = "Заказ удален";

    public static final String CAN_ONLY_ONE_ACTIVE_ORDER_MESSAGE = "Может быть только один активный заказ";

    public static final int MINUTES_MAKE_DRINK = 2;

    public static final String REGEXP_UUID = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

    public static final String INCOMING_PARAMETER_MISSING_MESSAGE = "Обязательный входной параметр отсутствует";

    public static final String MUST_BE_GREATER_THAN_OR_EQUAL_ZERO_MESSAGE = "Число должно быть больше или равно 0";

    public static final String MUST_BE_GREATER_THAN_ZERO_MESSAGE = "Число должно быть больше 0";

    public static final String INCORRECT_UUID_FORMAT_MESSAGE = "Неверный формат UUID";

    public static final String NOT_POSSIBLE_DELETE_RECIPE_MESSAGE = "Невозможно удалить рецепт, перед этим необходимо удалить напитки, в которых он используется";

    public static final String NOT_POSSIBLE_DELETE_INGREDIENT_MESSAGE = "Невозможно удалить ингредиент, перед этим необходимо удалить рецепты, в которых он используется";

    public static final String NOT_POSSIBLE_DELETE_DRINKS_MESSAGE = "Невозможно удалить напиток, перед этим необходимо удалить заказы, в которых он присутствует";
}