package uk.co.howes.sean.voucher;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.jdbcclient.JDBCConnectOptions;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

class Main {

    private static final String CREATE_USER_TABLE = """
               CREATE TABLE IF NOT EXISTS user_vouchers (
                              user_id BIGINT NOT NULL UNIQUE,
                              voucher_id VARCHAR(36) NOT NULL,
                              PRIMARY KEY (user_id) 
                          );
            """;

    private static final String CREATE_COUNT_TABLE = """
                CREATE TABLE voucher_count (
                voucher_count BIGINT NOT NULL DEFAULT 0
            );
            """;
    private static final String INIT_COUNT = "INSERT INTO voucher_count (voucher_count) VALUES (0);";

    static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        JDBCConnectOptions jdbcConnectOptions = new JDBCConnectOptions()
                .setJdbcUrl("jdbc:h2:mem:voucher")
                .setUser("sa")
                .setPassword("");

        PoolOptions poolOptions = new PoolOptions();
        Pool pool = JDBCPool.pool(vertx, jdbcConnectOptions, poolOptions);
        Future<RowSet<Row>> createUserTable = pool.preparedQuery(CREATE_USER_TABLE).execute();
        Future<RowSet<Row>> createCountTable = pool.preparedQuery(CREATE_COUNT_TABLE).execute().compose(rs->
                pool.preparedQuery(INIT_COUNT).execute());

        Future<String> deploy = Future.all(createUserTable, createCountTable)
                .compose(result -> vertx.deployVerticle(new VoucherVerticle(pool, 8888)).compose(m ->
                        vertx.deployVerticle(new VoucherVerticle(pool, 8889))));

        deploy
                .onSuccess(id -> System.out.println("Voucher Verticle deployed: " + id))
                .onFailure(Throwable::printStackTrace);
    }
}

