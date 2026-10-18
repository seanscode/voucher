package uk.co.howes.sean.voucher;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.ext.web.Router;
import io.vertx.sqlclient.Pool;
import uk.co.howes.sean.voucher.dao.UserVoucherDao;
import uk.co.howes.sean.voucher.dao.VoucherCountDao;
import uk.co.howes.sean.voucher.handler.VoucherIssuerHandler;
import uk.co.howes.sean.voucher.handler.VoucherListHandler;
import uk.co.howes.sean.voucher.handler.VoucherCountHandler;

import java.util.concurrent.TimeUnit;

public class VoucherVerticle extends VerticleBase {

    public static final String FAILED_TO_START_SERVER = "Failed to start server";
    private final Pool pool;
    private final int port;

    public VoucherVerticle(Pool pool, int port) {
        this.pool = pool;
        this.port = port;
    }

    @Override
    public Future<?> start() {
        Router router = Router.router(vertx);

        UserVoucherDao userVoucherDao = new UserVoucherDao(pool);
        VoucherCountDao voucherCountDao = new VoucherCountDao(pool);

        router.get("/voucher/:userId").handler(new VoucherIssuerHandler(userVoucherDao));
        router.get("/voucher/").handler(new VoucherListHandler(userVoucherDao));
        router.get("/count").handler(new VoucherCountHandler(voucherCountDao));

        return vertx.createHttpServer()
                .requestHandler(router)
                .listen(port)
                .onSuccess(h -> IO.println("We are up and running on port " + port))
                .onFailure(f -> IO.println(FAILED_TO_START_SERVER))
                .timeout(10, TimeUnit.SECONDS);

    }
}
