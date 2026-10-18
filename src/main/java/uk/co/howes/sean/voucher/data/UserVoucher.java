package uk.co.howes.sean.voucher.data;

import java.util.Objects;

public record UserVoucher(String userId, String voucherCode) {
        public UserVoucher {
            Objects.requireNonNull(userId);
            Objects.requireNonNull(voucherCode);
        }
}
