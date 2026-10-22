package uk.co.howes.sean.voucher.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import uk.co.howes.sean.voucher.dao.UserVoucherDao;

public class VoucherIssuerHandler implements Handler<RoutingContext> {

    private final UserVoucherDao userVoucherDao;

    public VoucherIssuerHandler(UserVoucherDao userVoucherDao) {
        this.userVoucherDao = userVoucherDao;
    }

    @Override
    public void handle(RoutingContext rc) {
        Long userId = Long.valueOf(rc.pathParam("userId"));
        userVoucherDao.getUserVoucher(userId).onSuccess(userVoucher -> {
            rc.response().setStatusCode(200).end(userVoucher.voucherCode());
        }).onFailure(t -> {
            t.printStackTrace();
            rc.response().setStatusCode(422).end("no voucher for you");
        });
    }

}
