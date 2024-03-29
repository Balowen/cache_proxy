package proxy;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class proxy {
    public static void main(String[] args) throws SQLException, IOException {
        //Connecting to Redis server on localhost
        Jedis jedis = new Jedis("localhost");
        //Connecting to SQLite database
        String url = "jdbc:sqlite:E:/projekty/proxy_redis/chinook.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(url);
        //Utworzenie data access object dla tabeli customer
        Dao<customer,Integer> daoCustomer = DaoManager.createDao(connectionSource, customer.class);

        Gson gson = new Gson();
        while(true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Wpisz stop by zamknac program");
            System.out.println("Podaj query: ");

            String query = sc.nextLine();
            String lCaseQuery = query.toLowerCase();

            if (lCaseQuery.equals("stop")) {
                connectionSource.close();
                break;
            }
            else {
                //Ustawienie klucza dla query
                //klucz to query malymi literami - bez spacji
                String redisKey = lCaseQuery.replaceAll(" ","");

                //Sprawdzenie czy wyniki sa w Redis
                if (jedis.exists(redisKey))
                    showRedisResults(jedis,gson, redisKey);
                else {
                    //Pobranie wynikow z bazy danych
                    List<String[]> resultDB =
                            getDBresults(daoCustomer,query);
                    //Serializacja wynikow do json
                    String queryResult = gson.toJson(resultDB);
                    //umieszczenie wynikow w Redis
                    jedis.set(redisKey, queryResult);
                    //Dopuszczalny czas nieświeżości danych -> 2 minuty
                    jedis.expire(redisKey, 120);
                }
            }
        }
    }

    private static void showRedisResults(Jedis jedis, Gson gson, String redisKey){
        Type myList = new TypeToken<List<String[]>>(){}.getType();
        String fromJedis = jedis.get(redisKey);
        List<String[]> resultRedis = gson.fromJson(fromJedis, myList);

        //Wyswietlanie wynikow z Redis
        resultRedis.forEach(arr -> System.out.println(Arrays.toString(arr)));
        System.out.println("To byly wyniki z redisa");
    }

    private static  List<String[]> getDBresults(Dao<customer, Integer> daoCustomer, String query) throws SQLException{
        GenericRawResults<String[]> rawResults = daoCustomer.queryRaw(query);
        List<String[]> resultDB = rawResults.getResults();
        //Wyswietlenie wynikow z bazy
        resultDB.forEach(ar -> System.out.println(Arrays.toString(ar)));
        System.out.println("To byly wyniki z bazy");
        return resultDB;
    }

}
