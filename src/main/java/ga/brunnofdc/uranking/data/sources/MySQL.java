package ga.brunnofdc.uranking.data.sources;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ga.brunnofdc.uranking.data.RankDataSource;
import ga.brunnofdc.uranking.ranking.Rank;
import ga.brunnofdc.uranking.ranking.RankUtils;
import ga.brunnofdc.uranking.uRanking;
import ga.brunnofdc.uranking.utils.ErrorReporter;
import ga.brunnofdc.uranking.utils.enums.ErrorType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class MySQL extends RankDataSource {

    private final HikariDataSource pool;

    public MySQL(JavaPlugin plugin) throws Exception {

        ConfigurationSection mysqlAccessData = plugin.getConfig().getConfigurationSection("System-Settings.Database.MySQL");

        int minimumConnections = (minimumConnections = Bukkit.getMaxPlayers() / 2) >= 1 ? minimumConnections : minimumConnections + 1;
        int maximumConnections = minimumConnections * 2;
        long connectionTimeout = 10000;

        HikariConfig connectionConfig = new HikariConfig();
        connectionConfig.setJdbcUrl(
                "jdbc:mysql://" +
                        mysqlAccessData.getString("Host") +
                        ":" +
                        mysqlAccessData.getString("Port") +
                        "/" +
                        mysqlAccessData.getString("DB_Name") +
                        "?verifyServerCertificate=false&useSSL=false&useUnicode=true&characterEncoding=utf8"
        );
        connectionConfig.setDriverClassName("com.mysql.jdbc.Driver");
        connectionConfig.setUsername(mysqlAccessData.getString("Username"));
        connectionConfig.setPassword(mysqlAccessData.getString("Password"));
        connectionConfig.setMinimumIdle(minimumConnections);
        connectionConfig.setMaximumPoolSize(maximumConnections);
        connectionConfig.setConnectionTimeout(connectionTimeout);

        pool = new HikariDataSource(connectionConfig);
        execUpdate("CREATE TABLE IF NOT EXISTS uranking_ranks (" +
                    "UUID VARCHAR(36) PRIMARY KEY NOT NULL, " +
                    "RankID VARCHAR(128) NOT NULL " +
                    ")");
    }

    private void execUpdate(String statement) {
        Bukkit.getScheduler().runTaskAsynchronously(uRanking.getInstance(), () -> {
            try {
                Connection conn = pool.getConnection();
                conn.prepareStatement(statement).executeUpdate(statement);
                pool.evictConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void execQuery(String statement, Consumer<ResultSet> callback) {
        ResultSet rs = null;
        try {
            Connection conn = this.pool.getConnection();
            rs = conn.prepareStatement(statement).executeQuery();
            callback.accept(rs);
        } catch (SQLException e) {
            callback.accept(null);
        } finally {
            if (rs != null) {
                finishQuery(rs);
            }
        }
    }

    private void finishQuery(ResultSet rs) {
        try {
            Statement statement = rs.getStatement();
            Connection conn = statement.getConnection();

            if (conn != null) {
                statement.close();
                pool.evictConnection(conn);
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void exists(UUID playeruuid, Consumer<Boolean> callback) {
        final String query = "SELECT * FROM `uranking_ranks` WHERE `UUID`='" + playeruuid.toString() + "'";

        Bukkit.getScheduler().runTaskAsynchronously(
                uRanking.getInstance(),
                () -> execQuery(query, resultSet -> {
                    try {
                        callback.accept(resultSet != null && resultSet.first());
                    } catch (SQLException e) {
                        ErrorReporter.sendReport(ErrorType.MYSQL_QUERY, e);
                        callback.accept(false);
                    }
                })
        );
    }

    public void read(UUID playeruuid, Consumer<Rank> callback) {
        final String query = "SELECT `RankID` FROM `uranking_ranks` WHERE `UUID`='" + playeruuid + "'";

        Bukkit.getScheduler().runTaskAsynchronously(
                uRanking.getInstance(),
                () -> execQuery(query, (resultSet) -> {
                    try {
                        if (resultSet != null && resultSet.first()) {
                            callback.accept(RankUtils.getRankByID(resultSet.getString("RankID")));
                        }
                    } catch (SQLException e) {
                        ErrorReporter.sendReport(ErrorType.MYSQL_QUERY, e);
                        callback.accept(null);
                    }
                })
        );
    }

    public void set(UUID playeruuid, String rankid) {
        exists(playeruuid, (result) -> {
            String query = (result) ?
                    "UPDATE `uranking_ranks` SET `RankID`='" + rankid + "' WHERE `UUID`='" + playeruuid + "'" :
                    "INSERT INTO `uranking_ranks` (UUID, RankID) VALUES ('" + playeruuid + "', '" + rankid + "')";

            execUpdate(query);
        });
    }

    public Map<UUID, String> index() {
        Map<UUID, String> entries = new HashMap<>();

        Bukkit.getScheduler().runTaskAsynchronously(
                uRanking.getInstance(),
                () -> execQuery("SELECT * FROM `uranking_ranks`", (resultSet) -> {
                    try {
                        if (resultSet != null && resultSet.first()) {
                            do {
                                UUID uuid = UUID.fromString(resultSet.getString("UUID"));
                                String rank = resultSet.getString("RankID");
                                entries.put(uuid, rank);
                            } while (resultSet.next());
                        }
                    } catch (SQLException e) {
                        ErrorReporter.sendReport(ErrorType.MYSQL_QUERY, e);
                    }
                })
        );

        return entries;
    }

    public void onDisable() {
        pool.close();
    }

}
