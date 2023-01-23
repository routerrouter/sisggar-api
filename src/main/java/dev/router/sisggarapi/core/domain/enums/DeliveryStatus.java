package dev.router.sisggarapi.core.domain.enums;

public enum DeliveryStatus {
    PENDING("Pendente"),
    DELIVERED("Entregue"),
    CANCELED("Cancelada"),
    FINISHED("Finalizada");

    private  String value;
    private DeliveryStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
