package uz.com.markethub.core.record;

public record ApiStatus(String key, int code, String defaultMessage) {

    //----------- Success ---------------

    public static final ApiStatus CREATED = new ApiStatus(
            "CREATED",
            0,
            "Successfully created");
    public static final ApiStatus UPDATED = new ApiStatus(
            "UPDATED",
            0,
            "Successfully updated");
    public static final ApiStatus DELETED = new ApiStatus(
            "DELETED",
            0,
            "Successfully deleted");
    public static final ApiStatus OK = new ApiStatus(
            "OK",
            0,
            "Ok");

    public static final ApiStatus SUCCESSFULLY_AUTHORIZED = new ApiStatus(
            "SUCCESSFULLY_AUTHORIZED",
            0,
            "User successfully authorized");

    //------- ERROR -----------------

    public static final ApiStatus ERR_NOT_FOUND = new ApiStatus(
            "ERR_NOT_FOUND",
            -1,
            "Item not found %s");

    public static final ApiStatus ERR_ID_NOT_FOUND = new ApiStatus(
            "ERR_ID_NOT_FOUND",
            -2,
            "Item not found by id");
    public static final ApiStatus ERR_ID_WITH_VERIFICATION_CODE_NOT_FOUND = new ApiStatus(
            "ERR_ID_WITH_VERIFICATION_CODE_NOT_FOUND",
            -3,
            "Item not found by id, verificationCode");

    public static final ApiStatus ERR_ID_IS_NOT_NULL = new ApiStatus(
            "ERR_ID_IS_NOT_NULL",
            -4,
            "id must be null");
    public static final ApiStatus ERR_VALIDATION = new ApiStatus(
            "ERR_VALIDATION",
            -5,
            "Validation invalid !");

    public static final ApiStatus ERR_UNAUTHORIZED = new ApiStatus(
            "ERR_UNAUTHORIZED",
            -6,
            "User unauthorized");
    public static final ApiStatus ERR_FORBIDDEN = new ApiStatus(
            "ERR_FORBIDDEN",
            -7, "Permission denied ");
    public static final ApiStatus ERR_DUPLICATE_VALUE = new ApiStatus(
            "ERR_DUPLICATE_VALUE",
            -8,
            "Items duplicated");
    public static final ApiStatus ERR_UNEXPECTED = new ApiStatus(
            "ERR_UNEXPECTED",
            -9,
            "Unexpected error");
    public static final ApiStatus ERR_API_PARTNER_VALIDATION = new ApiStatus(
            "ERR_API_PARTNER_VALIDATION",
            -12,
            "Api partner validation invalid");
    public static final ApiStatus ERR_API_PARTNER_NOT_ENABLED = new ApiStatus(
            "ERR_API_PARTNER_NOT_ENABLED",
            -13,
            "Api partner not enabled");
    public static final ApiStatus ERR_API_PARTNER_VERSION_EXPIRED = new ApiStatus(
            "ERR_API_PARTNER_VERSION_EXPIRED",
            -14,
            "Api partner validation invalid"
    );
    public static final ApiStatus ERR_USER_OLD_PASSWORD_INCORRECT = new ApiStatus(
            "ERR_USER_OLD_PASSWORD_INCORRECT",
            -15,
            "User old password incorrect"
    );
    public static final ApiStatus ERR_TOO_MANY_REQUESTS = new ApiStatus(
            "ERR_TOO_MANY_REQUESTS",
            -16,
            "The number of attempts has overflowed"
    );

    public static final ApiStatus ERR_INSUFFICIENT_STOCK = new ApiStatus(
            "ERR_INSUFFICIENT_STOCK",
            -17,
            "Insufficient stock for the requested product"
    );

    public static final ApiStatus ERR_INVALID_ORDER_TRANSITION = new ApiStatus(
            "ERR_INVALID_ORDER_TRANSITION",
            -18,
            "Invalid order status transition"
    );

    public static final ApiStatus ERR_CART_EMPTY = new ApiStatus(
            "ERR_CART_EMPTY",
            -19,
            "Cart is empty, cannot proceed to checkout"
    );

    public static final ApiStatus ERR_CONCURRENT_MODIFICATION = new ApiStatus(
            "ERR_CONCURRENT_MODIFICATION",
            -20,
            "Product stock was modified by another request, please retry"
    );

    public ApiResponseMessage map2ApiResponseMessage() {
        return new ApiResponseMessage(this.code, this.defaultMessage);
    }


}
