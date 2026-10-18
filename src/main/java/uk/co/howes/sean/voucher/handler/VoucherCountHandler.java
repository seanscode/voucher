package uk.co.howes.sean.voucher.handler;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import uk.co.howes.sean.voucher.dao.VoucherCountDao;

public class VoucherCountHandler implements Handler<RoutingContext> {

    private final VoucherCountDao voucherCountDao;

    public VoucherCountHandler(VoucherCountDao voucherCountDao) {
        this.voucherCountDao = voucherCountDao;
    }

    @Override
    public void handle(RoutingContext rc) {
        voucherCountDao.getVoucherCount()
                .onSuccess(count -> {
                    rc.json(JsonObject.of("count", count.count()));
                })
                .onFailure(t -> {
                    rc.fail(500, t);
                });
    }
}
