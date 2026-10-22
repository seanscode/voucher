package uk.co.howes.sean.voucher.data;

import java.math.BigInteger;
import java.util.Objects;

public record UserVoucher(Long userId, String voucherCode) {
        public UserVoucher {
            Objects.requireNonNull(userId);
            Objects.requireNonNull(voucherCode);
        }
}
