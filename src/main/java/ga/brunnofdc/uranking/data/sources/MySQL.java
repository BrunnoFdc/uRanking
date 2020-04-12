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

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MySQL extends RankDataSource {

    private HikariDataSource pool;

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
                        mysqlAccessData.getString("DB_Name")
        );
        connectionConfig.setDriverClassName("com.mysql.jdbc.Driver");
        connectionConfig.setUsername(mysqlAccessData.getString("Username"));
        connectionConfig.setPassword(mysqlAccessData.getString("Password"));
        connectionConfig.setMinimumIdle(minimumConnections);
        connectionConfig.setMaximumPoolSize(maximumConnections);
        connectionConfig.setConnectionTimeout(connectionTimeout);

        pool = new HikariDataSource(connectionConfig);
        execUpdate("CREATE TABLE IF NOT EXISTS `uranking_ranks` (`UUID` TEXT NULL DEFAULT NULL, `RankID` TEXT NULL DEFAULT NULL)");

    }

    private void execUpdate(String statement) {
        Bukkit.getScheduler().runTaskAsynchronously(uRanking.getInstance(), () -> {
            try {
                Connection conn = pool.getConnection();
                conn.prepareStatement(statement).executeUpdate(statement);
                pool.evictConnection(conn);
            } catch(SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private ResultSet execQuery(String statement) {
        try {
            Connection conn = this.pool.getConnection();
            return conn.prepareStatement(statement).executeQuery();
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void finishQuery(ResultSet rs) {
        try {
            Statement statement = rs.getStatement();
            Connection conn = statement.getConnection();

            if(conn != null) {
                statement.close();
                pool.evictConnection(conn);
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean exists(UUID playeruuid) {
        boolean result;
        ResultSet rs = execQuery("SELECT * FROM `uranking_ranks` WHERE `UUID`='" + playeruuid.toString() + "'");
        try {
            result = rs != null && rs.first();
        } catch (SQLException e) {
            ErrorReporter.sendReport(ErrorType.MYSQL_QUERY, e);
            return false;
        } finally {
            if (rs != null) {
                finishQuery(rs);
            }
        }
        return result;

    }

    @Nullable
    public Rank read(UUID playeruuid) {
        ResultSet result = execQuery("SELECT `RankID` FROM `uranking_ranks` WHERE `UUID`='" + playeruuid + "'");
        try {
            if(result != null && result.first()) {
                return RankUtils.getRankByID(result.getString("RankID"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            ErrorReporter.sendReport(ErrorType.MYSQL_QUERY, e);
            return null;
        } finally {
            if (result != null) {
                finishQuery(result);
            }
        }
    }

    public void set(UUID playeruuid, String rankid) {
        ResultSet result = execQuery("SELECT * FROM `uranking_ranks` WHERE `UUID`='" + playeruuid + "'");

        try {
            if(result != null && result.first()) {
                execUpdate("UPDATE `uranking_ranks` SET `RankID`='" + rankid + "' WHERE `UUID`='" + playeruuid + "'");
            } else {
                execUpdate("INSERT INTO `uranking_ranks` (UUID, RankID) VALUES ('" + playeruuid + "', '" + rankid + "')");
            }
        } catch (SQLException e) {
            ErrorReporter.sendReport(ErrorType.MYSQL_UPDATE, e);
        } finally {
            if (result != null) {
                finishQuery(result);
            }
        }
    }

    public Map<UUID, String> index() {
        Map<UUID, String> entries = new HashMap<>();
        ResultSet result = execQuery("SELECT * FROM `uranking_ranks`");
        try {
            if(result != null && result.first()) {
                do {
                    UUID uuid = UUID.fromString(result.getString("UUID"));
                    String rank = result.getString("RankID");
                    entries.put(uuid, rank);
                } while (result.next());
            }
        } catch (SQLException e) {
            ErrorReporter.sendReport(ErrorType.MYSQL_QUERY, e);
        } finally {
            if (result != null) {
                finishQuery(result);
            }
        }
        return entries;
    }

    public void onDisable() {
        pool.close();
    }

}
