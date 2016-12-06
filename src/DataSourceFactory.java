import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
 
public class DataSourceFactory {
    public static DataSource getMySQLDataSource(String uName, String pw) {
        
        MysqlDataSource mysqlDS = null;
        mysqlDS = new MysqlDataSource();
        mysqlDS.setUser(uName);
        mysqlDS.setPassword(pw);
        return mysqlDS;
      
    }
}