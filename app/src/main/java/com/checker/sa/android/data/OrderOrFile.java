package com.checker.sa.android.data;

public class OrderOrFile {
    ArchiveData aData;
    Order order;

    public OrderOrFile(ArchiveData aData, Order order) {
        this.aData = aData;
        this.order = order;
    }

    public ArchiveData getaData() {
        return aData;
    }

    public void setaData(ArchiveData aData) {
        this.aData = aData;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getOrderID() {
        if (order!=null) return order.getOrderID();
        else if (aData!=null) return aData.getOrderId();
        return null;
    }

    public String getSetID() {
        if (order!=null) return order.getSetID();
        else if (aData!=null && aData.getThisOrder()!=null) return aData.getThisOrder().getSetid();
        return null;
    }

    public SubmitQuestionnaireData getSubmitArchiveData() {
        if (order!=null) return order.getSubmitArchiveData();
        else if (aData!=null) return aData.getThisOrder();
        return null;
    }
}
