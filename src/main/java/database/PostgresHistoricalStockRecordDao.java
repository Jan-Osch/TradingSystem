package database;

import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.entities.record.HistoricalStockRecord;
import markets.exceptions.InstrumentUuidNotFoundException;
import markets.exceptions.MarketNotFoundException;
import persistance.HistoricalStockRecordGateWay;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

public class PostgresHistoricalStockRecordDao implements HistoricalStockRecordGateWay {
    private final String tableName = "STOCK_RECORDS_HISTORICAL";
    private PostgreSQLJDBC postgreSQLJDBC = PostgreSQLJDBC.getInstance();
    private static String dateFormat = "YYYY-MM-dd";
    private static SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

    @Override
    public void saveHistoricalStockRecord(HistoricalStockRecord historicalStockRecord) {
        String sql = "INSERT INTO \":tableName\" (UUID, DATE, CODE, VALUE, VOLUME, OPENING, MINIMUM, CHANGE, TRANSACTIONS) "
                + "VALUES (':uuid',':date', ':code', :value, :volume, :opening, :minimum, :change, :transactions)";

        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        sql = SqlUtils.addParameterToSqlStatement(sql, "uuid", historicalStockRecord.getInstrumentUuid().toString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "date", historicalStockRecord.getDate().toGMTString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "code", historicalStockRecord.getInstrument().getCodeName());
        sql = SqlUtils.addParameterToSqlStatement(sql, "value", historicalStockRecord.getClosingValue().toString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "volume", historicalStockRecord.getVolumeInShares().toString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "opening", historicalStockRecord.getOpeningValue().toString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "minimum", historicalStockRecord.getMinimumValue().toString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "change", String.valueOf(historicalStockRecord.getValueChangePercentage()));
        sql = SqlUtils.addParameterToSqlStatement(sql, "transactions", String.valueOf(historicalStockRecord.getNumberOfTransactions()));

        try {
            this.postgreSQLJDBC.executeSql(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Iterable<HistoricalStockRecord> getHistoricalStockRecordForPeriod(UUID instrumentUuid, UUID marketUuid, Date startDate, Date endDate) {
        String sql = "SELECT * FROM \":tableName\" WHERE "
                + "uuid = ':uuid' AND date >= ':startDate' AND date <= ':endDate'";

        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        sql = SqlUtils.addParameterToSqlStatement(sql, "uuid", instrumentUuid.toString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "startDate", startDate.toString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "endDate", endDate.toString());

        LinkedList<HistoricalStockRecord> result = new LinkedList<>();
        Market market;
        try {
            market = MarketManager.getMarketByUuid(marketUuid);
        } catch (MarketNotFoundException e) {
            e.printStackTrace();
            return result;
        }
        ResultSet resultSet = this.postgreSQLJDBC.executeSqlWithReturn(sql);
        try {
            while (resultSet.next()) {
                result.add(parseHistoricalStockRecordFromResultSet(instrumentUuid, market, resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstrumentUuidNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    private HistoricalStockRecord parseHistoricalStockRecordFromResultSet(UUID instrumentUuid, Market market, ResultSet resultSet) throws SQLException, InstrumentUuidNotFoundException {
        Date date = resultSet.getTimestamp("date");
        BigDecimal value = resultSet.getBigDecimal("value");
        BigDecimal volume = resultSet.getBigDecimal("volume");
        BigDecimal opening = resultSet.getBigDecimal("opening");
        BigDecimal minimum = resultSet.getBigDecimal("minimum");
        Float change = resultSet.getFloat("change");
        int transactions = resultSet.getInt("transactions");
        return new HistoricalStockRecord(market.getInstrumentByUuid(instrumentUuid),
                date, volume, opening, value, minimum, change, transactions);
    }
}
