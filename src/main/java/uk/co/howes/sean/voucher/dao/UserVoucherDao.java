package uk.co.howes.sean.voucher.dao;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;
import uk.co.howes.sean.voucher.data.UserVoucher;
import uk.co.howes.sean.voucher.execption.VoucherLimitException;

import java.util.ArrayList;

import java.util.List;
import java.util.UUID;

public class UserVoucherDao {
    private static final String MAX = "1000000";

    private static final String INSERT_VOUCHER = "INSERT INTO user_vouchers (user_id, voucher_id) VALUES (?, ?);";
    private static final String USER_VOUCHERS = "SELECT * FROM user_vouchers";
    private static final String UPDATE_COUNT = "UPDATE voucher_count SET voucher_count = voucher_count + 1 WHERE voucher_count < " + MAX + ";";

    private final Pool pool;

    public UserVoucherDao(Pool pool) {
        this.pool = pool;
    }

    public Future<UserVoucher> getUserVoucher(String userId) {
        return pool.withTransaction(connection ->
                connection.preparedQuery(UPDATE_COUNT).execute()
                        .compose(rowSet -> {
                            if (rowSet.rowCount() == 0) {
                                return Future.failedFuture(new VoucherLimitException());
                            }
                            UUID voucher = UUID.randomUUID();
                            return connection.preparedQuery(INSERT_VOUCHER)
                                    .execute(Tuple.of(userId, voucher))
                                    .map(rs -> new UserVoucher(userId, voucher.toString()));
                        }));
    }

    public Future<List<UserVoucher>> getAllUserVouchers() {
        return pool.preparedQuery(USER_VOUCHERS).execute()
                .compose(rowSet -> {
                    List<UserVoucher> userVoucherList = new ArrayList<>();
                    for (Row row : rowSet) {
                        UserVoucher userVoucher = new UserVoucher(row.getLong(0).toString(), row.getString(1));
                        userVoucherList.add(userVoucher);
                    }
                    return Future.succeededFuture(userVoucherList);
                }).onFailure(Future::failedFuture);

    }


}

