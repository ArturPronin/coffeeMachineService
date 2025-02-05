package test.example.coffeemachineservice.persistent.enums;

public enum OrderStatus {

    CREATED("Создана"),
    REFUSED("Отменена"),
    PROGRESS("В процессе"),
    COMPLETED("Выполнено");

    private final String statusName;

    OrderStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}