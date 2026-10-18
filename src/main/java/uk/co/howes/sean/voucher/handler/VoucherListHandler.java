package uk.co.howes.sean.voucher.handler;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import uk.co.howes.sean.voucher.data.UserVoucher;
import uk.co.howes.sean.voucher.dao.UserVoucherDao;

public class VoucherListHandler implements Handler<RoutingContext> {

    private final UserVoucherDao userVoucherDao;

    public VoucherListHandler(UserVoucherDao userVoucherDao) {
        this.userVoucherDao = userVoucherDao;
    }

    @Override
    public void handle(RoutingContext rc) {
        userVoucherDao.getAllUserVouchers()
                .onSuccess(userVouchers -> {
                    JsonArray userVouchersJson = new JsonArray();
                    for (UserVoucher userVoucher : userVouchers) {
                        userVouchersJson.add(buildUserVoucherJson(userVoucher));
                    }
                    rc.json(userVouchersJson);
                })
                .onFailure(throwable -> {
                    rc.fail(500, throwable);
                });
    }

    private JsonObject buildUserVoucherJson(UserVoucher userVoucher) {
        JsonObject userVoucherJson = new JsonObject();
        userVoucherJson.put("userId", userVoucher.userId());
        userVoucherJson.put("voucherCode", userVoucher.voucherCode());
        return userVoucherJson;
    }
}
