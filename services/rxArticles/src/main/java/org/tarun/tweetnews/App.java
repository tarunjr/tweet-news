package org.tarun.tweetnews;

import redis.clients.jedis.Jedis;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Jedis client = new Jedis();

        Observable<String> redisObservalbe = RedisPubSub.observe(client,"test");
        redisObservalbe.filter(new Func1<String, Boolean>() {
            public Boolean call(String s) {
                return true;
            }
        }).map(new Func1<String, String>() {
            public String call(String s) {
                return "";
            }
        });

        System.out.println( "Hello World!" );
    }
    public static void hello(String... names) {
        Observable.from(names).subscribe(new Action1<String>() {

            @Override
            public void call(String s) {
                System.out.println("Hello " + s + "!");
            }

        });
    }
}
