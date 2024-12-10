package com.uow.eventticketservice.dto.repo;

public class VendorTransactionCount {
    private long count;
    private String vendorName;

    public VendorTransactionCount(long count, String vendorName) {
        this.count = count;
        this.vendorName = vendorName;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}