package naamaantoniouk.mealbudget.ErrorHandeling;

public class Error {
    private int code;
    private String message;

    // No-args constructor
    public Error() {
    }

    // All-args constructor
    public Error(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // Getter and Setter methods (optional, based on your need)
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
