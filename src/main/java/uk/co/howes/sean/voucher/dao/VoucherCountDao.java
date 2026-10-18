package uk.co.howes.sean.voucher.dao;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import uk.co.howes.sean.voucher.data.VoucherCount;

public class VoucherCountDao {

    private static final String VOUCHER_COUNT = "SELECT voucher_count FROM voucher_count";

    private final Pool pool;

    public VoucherCountDao(Pool pool) {
        this.pool = pool;
    }

    public Future<VoucherCount> getVoucherCount() {
        return pool.preparedQuery(VOUCHER_COUNT).execute().compose(rs -> {
                    Integer count = rs.stream().iterator().next().getInteger(0);
                    return Future.succeededFuture(new VoucherCount(count));
                })
                .onFailure(Future::failedFuture);
    }
}
