package com.ibretail.ibretaildemo.response;

import java.util.List;
import java.util.Map;

public class TransactionData {
	private int offset;
    private int limit;
    private List<Map<String, Object>> records;

    public TransactionData(int offset, int limit, List<Map<String, Object>> records) {
        this.offset = offset;
        this.limit = limit;
        this.records = records;
    }

    public int getOffset() { return offset; }
    public void setOffset(int offset) { this.offset = offset; }

    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }

    public List<Map<String, Object>> getRecords() { return records; }
    public void setRecords(List<Map<String, Object>> records) { this.records = records; }
}
