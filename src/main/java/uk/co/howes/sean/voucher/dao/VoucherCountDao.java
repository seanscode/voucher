package uk.co.howes.sean.voucher.dao;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import uk.co.howes.sean.voucher.data.VoucherCount;
import uk.co.howes.sean.voucher.execption.VoucherCountException;

import java.util.Optional;

public class VoucherCountDao {

    private static final String VOUCHER_COUNT = "SELECT voucher_count FROM voucher_count";

    private final Pool pool;

    public VoucherCountDao(Pool pool) {
        this.pool = pool;
    }

    public Future<VoucherCount> getVoucherCount() {
        return pool.preparedQuery(VOUCHER_COUNT).execute().map(rs -> {
            Optional<Row> first = rs.stream().findFirst();
            if (first.isPresent()) {
                Integer count = first.get().getInteger(0);
                return new VoucherCount(count);
            } else {
                throw new VoucherCountException();
            }
        });
    }
}
