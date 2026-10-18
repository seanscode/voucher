package uk.co.howes.sean.voucher.data;

import java.util.Objects;

public record VoucherCount(Integer count) {
    public VoucherCount {
        Objects.requireNonNull(count);
    }
}
