package uk.co.howes.sean.voucher.execption;

public class VoucherCountException extends RuntimeException {
    public VoucherCountException() {
        super("Voucher Count Not Found");
    }
}
