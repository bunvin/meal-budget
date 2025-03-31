package naamaantoniouk.mealbudget.ErrorHandeling;


public class AppException extends Exception {
    private final ErrorMessage errorMessage;

    public AppException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public AppException(ErrorMessage errorMessage, Object... args) {
        super(formatMessage(errorMessage.getMessage(), args));
        this.errorMessage = new DynamicErrorMessage(errorMessage.getCode(), formatMessage(errorMessage.getMessage(), args));
    }

    // Helper method in AppException
    private static String formatMessage(String messageTemplate, Object... args) {
        String result = messageTemplate;
        for (int i = 0; i < args.length; i++) {
            result = result.replace("{" + i + "}", args[i] != null ? args[i].toString() : "null");
        }
        return result;
    }

    // Inner class in AppException
    private static class DynamicErrorMessage implements ErrorMessage {
        private final int code;
        private final String message;

        public DynamicErrorMessage(int code, String message) {
            this.code = code;
            this.message = message;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }
}
