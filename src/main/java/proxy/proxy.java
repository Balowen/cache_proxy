package proxy;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class proxy {
    public static void main(String[] args) throws SQLException, IOException {
        //Connecting to Redis server on localhost
        Jedis jedis = new Jedis("localhost");
        //Connecting to SQLite database
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:sqlite:E:/projekty/proxy_redis/chinook.db");
        //Creating DAO for table tracks
        Dao<customer,Integer> daoCustomer = DaoManager.createDao(connectionSource, customer.class);
        //Creating HashMap for queries
        Gson gson = new Gson();
        while(true) {

            Scanner scan = new Scanner(System.in);
            System.out.println("Wpisz stop by zamknac program");
            System.out.println("Podaj query: ");

            String query = scan.nextLine();
            query = query.toLowerCase();

            if (query.equals("stop")) {
                connectionSource.close();
                break;
            }
            else {
                //Ustawienie klucza dla query
                //klucz to query malymi literami - bez spacji
                String redisKey = query.replaceAll(" ","");
                //Checking if result of query exists in Redis
                if (jedis.exists(redisKey)) {

                    //Getting result from Redis
                    String fromJedis = jedis.get(redisKey);
                    List<String[]> wynik = gson.fromJson(fromJedis, List.class);


                    //Printing result
                    for (int i = 0; i < wynik.size(); i++) {
                        System.out.println(wynik.get(i));
                    }
                    System.out.println("To byly wyniki z redisa");
                } else {
                    //Getting result from query
                    GenericRawResults<String[]> rawResults = daoCustomer.queryRaw(query);
                    List<String[]> result = rawResults.getResults();

                    result.forEach(e -> {
                        for (String s : e) {
                            System.out.println(s);
                        }
                    });
                    System.out.println("To byly wyniki z bazy");
                    //Serializing result of query to JSON
                    String queryRes = gson.toJson(result);
                    //Setting result in Redis
                    jedis.set(redisKey, queryRes);
                    //Setting expire time
                    jedis.expire(redisKey, 300);
                }
            }
        }
    }
}
