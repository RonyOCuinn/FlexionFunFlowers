package com.ronan.purchase;

import com.flexionmobile.codingchallenge.integration.Purchase;

public class PurchaseImpl implements Purchase {

    private String id;
    private String itemId;
    private boolean consumed;

    public PurchaseImpl(String id, String itemId, boolean consumed) {
        this.id = id;
        this.itemId = itemId;
        this.consumed = consumed;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    @Override
    public boolean getConsumed() {
        return consumed;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }
}
