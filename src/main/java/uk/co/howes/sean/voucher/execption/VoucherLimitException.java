package uk.co.howes.sean.voucher.execption;

public class VoucherLimitException extends RuntimeException {
    public VoucherLimitException() {
        super("Voucher Limit Reached");
    }
}
