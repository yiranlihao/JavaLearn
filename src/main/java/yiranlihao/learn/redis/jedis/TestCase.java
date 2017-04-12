package yiranlihao.learn.redis.jedis;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.SortingParams;  
  
  
  
public class TestCase {  
  
    /** 
     * �ڲ�ͬ���߳���ʹ����ͬ��Jedisʵ���ᷢ����ֵĴ��󡣵��Ǵ���̫���ʵ��Ҳ������Ϊ����ζ�ŻὨ���ܶ�sokcet���ӣ� 
     * Ҳ�ᵼ����ֵĴ���������һJedisʵ�������̰߳�ȫ�ġ�Ϊ�˱�����Щ���⣬����ʹ��JedisPool, 
     * JedisPool��һ���̰߳�ȫ���������ӳء�������JedisPool����һЩ�ɿ�Jedisʵ�������Դӳ����õ�Jedis��ʵ���� 
     * ���ַ�ʽ���Խ����Щ���Ⲣ�һ�ʵ�ָ�Ч������ 
     */  
  
    public static void main(String[] args) {  
  
        // ...when closing your application:  
        RedisUtil.getPool().destroy();  
  
    }  
  
    public static void Hello() {  
        Jedis jedis = RedisUtil.getJedis();  
        try {  
            // ��key-->name�з�����value-->minxr  
            jedis.set("name", "minxr");  
            String ss = jedis.get("name");  
            System.out.println(ss);  
  
            // ��ֱ�ۣ�����map ��jintao append���Ѿ��е�value֮��  
            jedis.append("name", "jintao");  
            ss = jedis.get("name");  
            System.out.println(ss);  
  
            // 2��ֱ�Ӹ���ԭ��������  
            jedis.set("name", "jintao");  
            System.out.println(jedis.get("jintao"));  
  
            // ɾ��key��Ӧ�ļ�¼  
            jedis.del("name");  
            System.out.println(jedis.get("name"));// ִ�н����null  
  
            /** 
             * mset�൱�� jedis.set("name","minxr"); jedis.set("jarorwar","aaa"); 
             */  
            jedis.mset("name", "minxr", "jarorwar", "aaa");  
            System.out.println(jedis.mget("name", "jarorwar"));  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            RedisUtil.getPool().returnResource(jedis);  
        }  
  
    }  
  
    private void testKey() {  
        Jedis jedis = RedisUtil.getJedis();  
        System.out.println("=============key==========================");  
        // �������  
        System.out.println(jedis.flushDB());  
        System.out.println(jedis.echo("foo"));  
        // �ж�key�����  
        System.out.println(jedis.exists("foo"));  
        jedis.set("key", "values");  
        System.out.println(jedis.exists("key"));  
    }  
  
    public static void testString() {  
        System.out.println("==String==");  
        Jedis jedis = RedisUtil.getJedis();  
        try {  
            // String  
            jedis.set("key", "Hello World!");  
            String value = jedis.get("key");  
            System.out.println(value);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            RedisUtil.getPool().returnResource(jedis);  
        }  
  
        System.out.println("=============String==========================");  
        // �������  
        System.out.println(jedis.flushDB());  
        // �洢����  
        jedis.set("foo", "bar");  
        System.out.println(jedis.get("foo"));  
        // ��key�����ڣ���洢  
        jedis.setnx("foo", "foo not exits");  
        System.out.println(jedis.get("foo"));  
        // ��������  
        jedis.set("foo", "foo update");  
        System.out.println(jedis.get("foo"));  
        // ׷������  
        jedis.append("foo", " hello, world");  
        System.out.println(jedis.get("foo"));  
        // ����key����Ч�ڣ����洢����  
        jedis.setex("foo", 2, "foo not exits");  
        System.out.println(jedis.get("foo"));  
        try {  
            Thread.sleep(3000);  
        } catch (InterruptedException e) {  
        }  
        System.out.println(jedis.get("foo"));  
        // ��ȡ����������  
        jedis.set("foo", "foo update");  
        System.out.println(jedis.getSet("foo", "foo modify"));  
        // ��ȡvalue��ֵ  
        System.out.println(jedis.getrange("foo", 1, 3));  
        System.out.println(jedis.mset("mset1", "mvalue1", "mset2", "mvalue2",  
                "mset3", "mvalue3", "mset4", "mvalue4"));  
        System.out.println(jedis.mget("mset1", "mset2", "mset3", "mset4"));  
        System.out.println(jedis.del(new String[] { "foo", "foo1", "foo3" }));  
    }  
  
    public static void testList() {  
        System.out.println("==List==");  
        Jedis jedis = RedisUtil.getJedis();  
        try {  
            // ��ʼǰ�����Ƴ����е�����  
            jedis.del("messages");  
            jedis.rpush("messages", "Hello how are you?");  
            jedis.rpush("messages", "Fine thanks. I'm having fun with redis.");  
            jedis.rpush("messages", "I should look into this NOSQL thing ASAP");  
  
            // ��ȡ����������jedis.lrange�ǰ���Χȡ����  
            // ��һ����key���ڶ�������ʼλ�ã��������ǽ���λ�ã�jedis.llen��ȡ���� -1��ʾȡ������  
            List<String> values = jedis.lrange("messages", 0, -1);  
            System.out.println(values);  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            RedisUtil.getPool().returnResource(jedis);  
        }  
  
        // �������  
        System.out.println(jedis.flushDB());  
        // �������  
        jedis.lpush("lists", "vector");  
        jedis.lpush("lists", "ArrayList");  
        jedis.lpush("lists", "LinkedList");  
        // ���鳤��  
        System.out.println(jedis.llen("lists"));  
        // ����  
        System.out.println(jedis.sort("lists"));  
        // �ִ�  
        System.out.println(jedis.lrange("lists", 0, 3));  
        // �޸��б��е���ֵ  
        jedis.lset("lists", 0, "hello list!");  
        // ��ȡ�б�ָ���±��ֵ  
        System.out.println(jedis.lindex("lists", 1));  
        // ɾ���б�ָ���±��ֵ  
        System.out.println(jedis.lrem("lists", 1, "vector"));  
        // ɾ���������������  
        System.out.println(jedis.ltrim("lists", 0, 1));  
        // �б��ջ  
        System.out.println(jedis.lpop("lists"));  
        // �����б�ֵ  
        System.out.println(jedis.lrange("lists", 0, -1));  
    }  
  
    public static void testSet() {  
        System.out.println("==Set==");  
        Jedis jedis = RedisUtil.getJedis();  
        try {  
            jedis.sadd("myset", "1");  
            jedis.sadd("myset", "2");  
            jedis.sadd("myset", "3");  
            jedis.sadd("myset", "4");  
            Set<String> setValues = jedis.smembers("myset");  
            System.out.println(setValues);  
  
            // �Ƴ�noname  
            jedis.srem("myset", "4");  
            System.out.println(jedis.smembers("myset"));// ��ȡ���м����value  
            System.out.println(jedis.sismember("myset", "4"));// �ж� minxr  
                                                                // �Ƿ���sname���ϵ�Ԫ��  
            System.out.println(jedis.scard("sname"));// ���ؼ��ϵ�Ԫ�ظ���  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            RedisUtil.getPool().returnResource(jedis);  
        }  
  
        // �������  
        System.out.println(jedis.flushDB());  
        // �������  
        jedis.sadd("sets", "HashSet");  
        jedis.sadd("sets", "SortedSet");  
        jedis.sadd("sets", "TreeSet");  
        // �ж�value�Ƿ����б���  
        System.out.println(jedis.sismember("sets", "TreeSet"));  
        ;  
        // �����б�ֵ  
        System.out.println(jedis.smembers("sets"));  
        // ɾ��ָ��Ԫ��  
        System.out.println(jedis.srem("sets", "SortedSet"));  
        // ��ջ  
        System.out.println(jedis.spop("sets"));  
        System.out.println(jedis.smembers("sets"));  
        //  
        jedis.sadd("sets1", "HashSet1");  
        jedis.sadd("sets1", "SortedSet1");  
        jedis.sadd("sets1", "TreeSet");  
        jedis.sadd("sets2", "HashSet2");  
        jedis.sadd("sets2", "SortedSet1");  
        jedis.sadd("sets2", "TreeSet1");  
        // ����  
        System.out.println(jedis.sinter("sets1", "sets2"));  
        // ����  
        System.out.println(jedis.sunion("sets1", "sets2"));  
        // �  
        System.out.println(jedis.sdiff("sets1", "sets2"));  
    }  
  
    public static void sortedSet() {  
        System.out.println("==SoretedSet==");  
        Jedis jedis = RedisUtil.getJedis();  
        try {  
            jedis.zadd("hackers", 1940, "Alan Kay");  
            jedis.zadd("hackers", 1953, "Richard Stallman");  
            jedis.zadd("hackers", 1965, "Yukihiro Matsumoto");  
            jedis.zadd("hackers", 1916, "Claude Shannon");  
            jedis.zadd("hackers", 1969, "Linus Torvalds");  
            jedis.zadd("hackers", 1912, "Alan Turing");  
            Set<String> setValues = jedis.zrange("hackers", 0, -1);  
            System.out.println(setValues);  
            Set<String> setValues2 = jedis.zrevrange("hackers", 0, -1);  
            System.out.println(setValues2);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            RedisUtil.getPool().returnResource(jedis);  
        }  
  
        // �������  
        System.out.println(jedis.flushDB());  
        // �������  
        jedis.zadd("zset", 10.1, "hello");  
        jedis.zadd("zset", 10.0, ":");  
        jedis.zadd("zset", 9.0, "zset");  
        jedis.zadd("zset", 11.0, "zset!");  
        // Ԫ�ظ���  
        System.out.println(jedis.zcard("zset"));  
        // Ԫ���±�  
        System.out.println(jedis.zscore("zset", "zset"));  
        // �����Ӽ�  
        System.out.println(jedis.zrange("zset", 0, -1));  
        // ɾ��Ԫ��  
        System.out.println(jedis.zrem("zset", "zset!"));  
        System.out.println(jedis.zcount("zset", 9.5, 10.5));  
        // ��������ֵ  
        System.out.println(jedis.zrange("zset", 0, -1));  
    }  
  
    public static void testHsh() {  
        System.out.println("==Hash==");  
        Jedis jedis = RedisUtil.getJedis();  
        try {  
            Map<String, String> pairs = new HashMap<String, String>();  
            pairs.put("name", "Akshi");  
            pairs.put("age", "2");  
            pairs.put("sex", "Female");  
            jedis.hmset("kid", pairs);  
            List<String> name = jedis.hmget("kid", "name");// ����Ǹ����͵�LIST  
            // jedis.hdel("kid","age"); //ɾ��map�е�ĳ����ֵ  
            System.out.println(jedis.hmget("kid", "pwd")); // ��Ϊɾ���ˣ����Է��ص���null  
            System.out.println(jedis.hlen("kid")); // ����keyΪuser�ļ��д�ŵ�ֵ�ĸ���  
            System.out.println(jedis.exists("kid"));// �Ƿ����keyΪuser�ļ�¼  
            System.out.println(jedis.hkeys("kid"));// ����map�����е�����key  
            System.out.println(jedis.hvals("kid"));// ����map�����е�����value  
  
            Iterator<String> iter = jedis.hkeys("kid").iterator();  
            while (iter.hasNext()) {  
                String key = iter.next();  
                System.out.println(key + ":" + jedis.hmget("kid", key));  
            }  
  
            List<String> values = jedis.lrange("messages", 0, -1);  
            values = jedis.hmget("kid", new String[] { "name", "age", "sex" });  
            System.out.println(values);  
            Set<String> setValues = jedis.zrange("hackers", 0, -1);  
            setValues = jedis.hkeys("kid");  
            System.out.println(setValues);  
            values = jedis.hvals("kid");  
            System.out.println(values);  
            pairs = jedis.hgetAll("kid");  
            System.out.println(pairs);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            RedisUtil.getPool().returnResource(jedis);  
        }  
  
        // �������  
        System.out.println(jedis.flushDB());  
        // �������  
        jedis.hset("hashs", "entryKey", "entryValue");  
        jedis.hset("hashs", "entryKey1", "entryValue1");  
        jedis.hset("hashs", "entryKey2", "entryValue2");  
        // �ж�ĳ��ֵ�Ƿ����  
        System.out.println(jedis.hexists("hashs", "entryKey"));  
        // ��ȡָ����ֵ  
        System.out.println(jedis.hget("hashs", "entryKey")); // ������ȡָ����ֵ  
        System.out.println(jedis.hmget("hashs", "entryKey", "entryKey1"));  
        // ɾ��ָ����ֵ  
        System.out.println(jedis.hdel("hashs", "entryKey"));  
        // Ϊkey�е��� field ��ֵ�������� increment  
        System.out.println(jedis.hincrBy("hashs", "entryKey", 123l));  
        // ��ȡ���е�keys  
        System.out.println(jedis.hkeys("hashs"));  
        // ��ȡ���е�values  
        System.out.println(jedis.hvals("hashs"));  
    }  
  
    public static void testOther() throws InterruptedException {  
        Jedis jedis = RedisUtil.getJedis();  
  
        try {  
            // keys�д���Ŀ�����ͨ���  
            System.out.println(jedis.keys("*")); // ���ص�ǰ�������е�key [sose, sanme,  
                                                    // name, jarorwar, foo,  
                                                    // sname, java framework,  
                                                    // user, braand]  
            System.out.println(jedis.keys("*name"));// ���ص�sname [sname, name]  
            System.out.println(jedis.del("sanmdde"));// ɾ��keyΪsanmdde�Ķ��� ɾ���ɹ�����1  
                                                        // ɾ��ʧ�ܣ����߲����ڣ����� 0  
            System.out.println(jedis.ttl("sname"));// ���ظ���key����Чʱ�䣬�����-1���ʾ��Զ��Ч  
            jedis.setex("timekey", 10, "min");// ͨ���˷���������ָ��key�Ĵ���Чʱ�䣩 ʱ��Ϊ��  
            Thread.sleep(5000);// ˯��5���ʣ��ʱ�佫Ϊ<=5  
            System.out.println(jedis.ttl("timekey")); // ������Ϊ5  
            jedis.setex("timekey", 1, "min"); // ��Ϊ1�������ٿ�ʣ��ʱ�����1��  
            System.out.println(jedis.ttl("timekey")); // ������Ϊ1  
            System.out.println(jedis.exists("key"));// ���key�Ƿ����  
            System.out.println(jedis.rename("timekey", "time"));  
            System.out.println(jedis.get("timekey"));// ��Ϊ�Ƴ�������Ϊnull  
            System.out.println(jedis.get("time")); // ��Ϊ��timekey ������Ϊtime  
                                                    // ���Կ���ȡ��ֵ min  
            // jedis ����  
            // ע�⣬�˴���rpush��lpush��List�Ĳ�������һ��˫���������ӱ��������ģ�  
            jedis.del("a");// ��������ݣ��ټ������ݽ��в���  
            jedis.rpush("a", "1");  
            jedis.lpush("a", "6");  
            jedis.lpush("a", "3");  
            jedis.lpush("a", "9");  
            System.out.println(jedis.lrange("a", 0, -1));// [9, 3, 6, 1]  
            System.out.println(jedis.sort("a")); // [1, 3, 6, 9] //�����������  
            System.out.println(jedis.lrange("a", 0, -1));  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            RedisUtil.getPool().returnResource(jedis);  
        }  
  
    }  
  
    @org.junit.Test  
    public void testUnUsePipeline() {  
        long start = new Date().getTime();  
  
        Jedis jedis = RedisUtil.getJedis();  
        for (int i = 0; i < 10000; i++) {  
            jedis.set("age1" + i, i + "");  
            jedis.get("age1" + i);// ÿ�����������������redis-server  
        }  
        long end = new Date().getTime();  
  
        System.out.println("unuse pipeline cost:" + (end - start) + "ms");  
  
        RedisUtil.getPool().returnResource(jedis);  
    }  
  
    @org.junit.Test  
    /** 
     * �ο�:http://blog.csdn.net/freebird_lb/article/details/7778919 
     */  
    public void testUsePipeline() {  
        long start = new Date().getTime();  
  
        Jedis jedis = RedisUtil.getJedis();  
        jedis.flushDB();  
        Pipeline p = jedis.pipelined();  
        for (int i = 0; i < 10000; i++) {  
            p.set("age2" + i, i + "");  
            System.out.println(p.get("age2" + i));  
        }  
        p.sync();// ��δ����ȡ���е�response  
  
        long end = new Date().getTime();  
  
        System.out.println("use pipeline cost:" + (end - start) + "ms");  
  
        RedisUtil.getPool().returnResource(jedis);  
    }  
  
  
    @org.junit.Test  
    /** 
     * ʱ�临�Ӷȣ� 
          O(N+M*log(M))�� N ΪҪ������б�򼯺��ڵ�Ԫ�������� M ΪҪ���ص�Ԫ�������� 
            ���ֻ��ʹ�� SORT ����� GET ѡ���ȡ���ݶ�û�н�������ʱ�临�Ӷ� O(N)�� 
     */  
    public void testSort1() {  
        // ����Ĭ����������Ϊ����ֵ������Ϊ˫���ȸ�������Ȼ����бȽ�  
        Jedis redis = RedisUtil.getJedis();  
        // һ��SORT�÷� ��򵥵�SORTʹ�÷�����SORT key��  
        redis.lpush("mylist", "1");  
        redis.lpush("mylist", "4");  
        redis.lpush("mylist", "6");  
        redis.lpush("mylist", "3");  
        redis.lpush("mylist", "0");  
        // List<String> list = redis.sort("sort");// Ĭ��������  
        SortingParams sortingParameters = new SortingParams();  
        sortingParameters.desc();  
        // sortingParameters.alpha();//�����ݼ��б�������ַ���ֵʱ��������� ALPHA  
        // ���η�(modifier)��������  
        sortingParameters.limit(0, 2);// �����ڷ�ҳ��ѯ  
        List<String> list = redis.sort("mylist", sortingParameters);// Ĭ��������  
        for (int i = 0; i < list.size(); i++) {  
            System.out.println(list.get(i));  
        }  
        redis.flushDB();  
        RedisUtil.closeJedis(redis);  
    }  
  
    @org.junit.Test  
    /** 
     * sort list 
     * LIST���hash������ 
     */  
    public void testSort2() {  
        Jedis jedis = RedisUtil.getJedis();  
        jedis.del("user:66", "user:55", "user:33", "user:22", "user:11",  
                "userlist");  
        jedis.lpush("userlist", "33");  
        jedis.lpush("userlist", "22");  
        jedis.lpush("userlist", "55");  
        jedis.lpush("userlist", "11");  
  
        jedis.hset("user:66", "name", "66");  
        jedis.hset("user:55", "name", "55");  
        jedis.hset("user:33", "name", "33");  
        jedis.hset("user:22", "name", "79");  
        jedis.hset("user:11", "name", "24");  
        jedis.hset("user:11", "add", "beijing");  
        jedis.hset("user:22", "add", "shanghai");  
        jedis.hset("user:33", "add", "guangzhou");  
        jedis.hset("user:55", "add", "chongqing");  
        jedis.hset("user:66", "add", "xi'an");  
  
        SortingParams sortingParameters = new SortingParams();  
        // ���� "->" ���ڷָ��ϣ��ļ���(key name)��������(hash field)����ʽΪ "key->field" ��  
        sortingParameters.get("user:*->name");  
        sortingParameters.get("user:*->add");  
//      sortingParameters.by("user:*->name");  
//      sortingParameters.get("#");  
        List<String> result = jedis.sort("userlist", sortingParameters);  
        for (String item : result) {  
            System.out.println("item...." + item);  
        }  
        /** 
         * ��Ӧ��redis�ͻ��������ǣ�sort ml get user*->name sort ml get user:*->name get 
         * user:*->add 
         */  
    }  
  
    @org.junit.Test  
    /** 
     * sort set 
     * SET���String������ 
     */  
    public void testSort3() {  
        Jedis jedis = RedisUtil.getJedis();  
        jedis.del("tom:friend:list", "score:uid:123", "score:uid:456",  
                "score:uid:789", "score:uid:101", "uid:123", "uid:456",  
                "uid:789", "uid:101");  
  
        jedis.sadd("tom:friend:list", "123"); // tom�ĺ����б�  
        jedis.sadd("tom:friend:list", "456");  
        jedis.sadd("tom:friend:list", "789");  
        jedis.sadd("tom:friend:list", "101");  
  
        jedis.set("score:uid:123", "1000"); // ���Ѷ�Ӧ�ĳɼ�  
        jedis.set("score:uid:456", "6000");  
        jedis.set("score:uid:789", "100");  
        jedis.set("score:uid:101", "5999");  
  
        jedis.set("uid:123", "{'uid':123,'name':'lucy'}"); // ���ѵ���ϸ��Ϣ  
        jedis.set("uid:456", "{'uid':456,'name':'jack'}");  
        jedis.set("uid:789", "{'uid':789,'name':'jay'}");  
        jedis.set("uid:101", "{'uid':101,'name':'jolin'}");  
  
        SortingParams sortingParameters = new SortingParams();  
  
        sortingParameters.desc();  
        // sortingParameters.limit(0, 2);  
        // ע��GET����������ģ�GET user_name_* GET user_password_*  
        // �� GET user_password_* GET user_name_*���صĽ��λ�ò�ͬ  
        sortingParameters.get("#");// GET ����һ������Ĺ��򡪡� "GET #"  
                                    // �����ڻ�ȡ���������(��������������� user_id )�ĵ�ǰԪ�ء�  
        sortingParameters.get("uid:*");  
        sortingParameters.get("score:uid:*");  
        sortingParameters.by("score:uid:*");  
        // ��Ӧ��redis ������./redis-cli sort tom:friend:list by score:uid:* get # get  
        // uid:* get score:uid:*  
        List<String> result = jedis.sort("tom:friend:list", sortingParameters);  
        for (String item : result) {  
            System.out.println("item..." + item);  
        }  
  
    }  
  
    /** 
     *  
     * ֻ��ȡ����������� BY ���η����Խ�һ�������ڵ� key ����Ȩ�أ��� SORT ������������� 
     * �÷���������ϣ����ȡ�ⲿ������ֲ�ϣ������������ʱʹ�á� # ȷ��fake_key������ redis> EXISTS fake_key 
     * (integer) 0 # ��fake_key��BY������������ֻGET name �� GET password redis> SORT 
     * user_id BY fake_key GET # GET user_name_* GET user_password_* 1) "222" # 
     * id 2) "hacker" # user_name 3) "hey,im in" # password 4) "59230" 5) "jack" 
     * 6) "jack201022" 7) "2" 8) "huangz" 9) "nobodyknows" 10) "1" 11) "admin" 
     * 12) "a_long_long_password" 
     */  
    public void testSort4() {  
  
    }  
  
    /** 
     *  
     ���������� Ĭ������£� SORT ����ֻ�Ǽ򵥵ط����������������ϣ�����������������Ը� STORE ѡ��ָ��һ�� key 
     * ��Ϊ�����������������б����ʽ�����浽��� key �ϡ�(��ָ�� key �Ѵ��ڣ��򸲸ǡ�) redis> EXISTS 
     * user_info_sorted_by_level # ȷ��ָ��key������ (integer) 0 redis> SORT user_id BY 
     * user_level_* GET # GET user_name_* GET user_password_* STORE 
     * user_info_sorted_by_level # ���� (integer) 12 # ��ʾ��12������������� redis> LRANGE 
     * user_info_sorted_by_level 0 11 # �鿴������ 1) "59230" 2) "jack" 3) 
     * "jack201022" 4) "2" 5) "huangz" 6) "nobodyknows" 7) "222" 8) "hacker" 9) 
     * "hey,im in" 10) "1" 11) "admin" 12) "a_long_long_password" һ����Ȥ���÷��ǽ� SORT 
     * ������棬�� EXPIRE Ϊ�������������ʱ�䣬����������ͳ��� SORT ������һ�����档 �����Ͳ���Ƶ���ص��� SORT 
     * �����ˣ�ֻ�е����������ʱ������Ҫ�ٵ���һ�� SORT ������ 
     * ��ʱ��Ϊ����ȷʵ����һ�÷����������Ҫ�����Ա������ͻ���ͬʱ���л����ؽ�(Ҳ���Ƕ���ͻ��ˣ�ͬһʱ����� SORT 
     * ������������Ϊ�����)������μ� SETNX ��� 
     */  
    @Test  
    public void testSort5() {  
        // ����Ĭ����������Ϊ����ֵ������Ϊ˫���ȸ�������Ȼ����бȽ�  
        Jedis jedis = RedisUtil.getJedis();  
        // һ��SORT�÷� ��򵥵�SORTʹ�÷�����SORT key��  
        jedis.lpush("mylist", "1");  
        jedis.lpush("mylist", "4");  
        jedis.lpush("mylist", "6");  
        jedis.lpush("mylist", "3");  
        jedis.lpush("mylist", "0");  
        // List<String> list = redis.sort("sort");// Ĭ��������  
        SortingParams sortingParameters = new SortingParams();  
        sortingParameters.desc();  
        // sortingParameters.alpha();//�����ݼ��б�������ַ���ֵʱ��������� ALPHA  
        // ���η�(modifier)��������  
        // sortingParameters.limit(0, 2);//�����ڷ�ҳ��ѯ  
  
        // û��ʹ�� STORE �����������б���ʽ��������. ʹ�� STORE ������������������Ԫ��������  
  
        jedis.sort("mylist", sortingParameters, "mylist");// �����ָ����������һ��KEY�У����ｲ�������ԭ����KEY  
  
        List<String> list = jedis.lrange("mylist", 0, -1);  
        for (int i = 0; i < list.size(); i++) {  
            System.out.println(list.get(i));  
        }  
  
        jedis.sadd("tom:friend:list", "123"); // tom�ĺ����б�  
        jedis.sadd("tom:friend:list", "456");  
        jedis.sadd("tom:friend:list", "789");  
        jedis.sadd("tom:friend:list", "101");  
  
        jedis.set("score:uid:123", "1000"); // ���Ѷ�Ӧ�ĳɼ�  
        jedis.set("score:uid:456", "6000");  
        jedis.set("score:uid:789", "100");  
        jedis.set("score:uid:101", "5999");  
  
        jedis.set("uid:123", "{'uid':123,'name':'lucy'}"); // ���ѵ���ϸ��Ϣ  
        jedis.set("uid:456", "{'uid':456,'name':'jack'}");  
        jedis.set("uid:789", "{'uid':789,'name':'jay'}");  
        jedis.set("uid:101", "{'uid':101,'name':'jolin'}");  
  
        sortingParameters = new SortingParams();  
        // sortingParameters.desc();  
        sortingParameters.get("#");// GET ����һ������Ĺ��򡪡� "GET #"  
                                    // �����ڻ�ȡ���������(��������������� user_id )�ĵ�ǰԪ�ء�  
        sortingParameters.by("score:uid:*");  
        jedis.sort("tom:friend:list", sortingParameters, "tom:friend:list");  
        List<String> result = jedis.lrange("tom:friend:list", 0, -1);  
        for (String item : result) {  
            System.out.println("item..." + item);  
        }  
  
        jedis.flushDB();  
        RedisUtil.closeJedis(jedis);  
    }  
      
      
    public void testMore(){  
        //ZRANGEȡ�����µ�10����Ŀ��  
        //ʹ��LPUSH + LTRIM��ȷ��ֻȡ�����µ�1000����Ŀ��  
        //HINCRBY key field increment,Ϊ��ϣ�� key �е��� field ��ֵ�������� increment  
        //INCRBY,HINCRBY�ȵȣ�redis����ԭ�ӵ�����atomic increment��������Է��ĵļ��ϸ��ּ�������GETSET���ã������������ǹ��ڡ�  
        // LREM greet 2 morning     # �Ƴ��ӱ�ͷ����β�����ȷ��ֵ����� morning,�����������ɾ���ض�����  
        // zrevrank test a �鿴a��sorted set�е�����ʱ���ڵڼ�������ѯ�������INDEX������INDEX��3��ʾ���ڵ�����  
        // zrank test a �෴����ʾ������ʱ�������  
        // zscore test one��ʾone���Ԫ����sorted set�е�scoreΪ����  
        // zrevrange test 0 -1 ��ʾsorted set������,zrange test 0 -1��ʾ������  
        //��һ������ member Ԫ�ؼ��� score ֵ���뵽���� key ���С����ĳ�� member �Ѿ������򼯵ĳ�Ա����ô������� member �� score ֵ����ͨ�����²������ member Ԫ�أ�����֤�� member ����ȷ��λ���ϡ�  
        //zrem test oneɾ��sorted set��ĳ��Ԫ��  
    }  
      
    public List<String> get_latest_comments(int start, int num_items){  
        //��ȡ��������  
        //LPUSH latest.comments <ID>   
        //-���ǽ��б�ü�Ϊָ�����ȣ����Redisֻ��Ҫ�������µ�5000�����ۣ�  
        //LTRIM latest.comments 0 5000   
        //���������Ʋ��ܳ���5000��ID��������ǵĻ�ȡID������һֱѯ��Redis��ֻ����start/count���������������Χ��ʱ�򣬲���Ҫȥ�������ݿ⡣  
        Jedis jedis = RedisUtil.getJedis();  
        List<String> id_list = jedis.lrange("latest.comments",start,start+num_items-1) ;  
          
        if(id_list.size()<num_items){  
            //id_list = SQL.EXECUTE("SELECT ... ORDER BY time LIMIT ...");  
        }  
        return id_list;  
    }  
             
      
  
    @Test  
    public void testDB() {  
        Jedis jedis = RedisUtil.getJedis();  
        System.out.println(jedis.select(0));// select db-index  
                                            // ͨ������ѡ�����ݿ⣬Ĭ�����ӵ����ݿ�������0,Ĭ�����ݿ�����16��������1��ʾ�ɹ���0ʧ��  
        System.out.println(jedis.dbSize());// dbsize ���ص�ǰ���ݿ��key����  
        System.out.println(jedis.keys("*")); // ����ƥ��ָ��ģʽ������key  
        System.out.println(jedis.randomKey());  
        jedis.flushDB();// ɾ����ǰ���ݿ�������key,�˷�������ʧ�ܡ�����  
        jedis.flushAll();// ɾ���������ݿ��е�����key���˷�������ʧ�ܡ���������  
  
    }  
  
    @Test  
    public void testMget() {  
  
        Jedis jedis = RedisUtil.getJedis();  
        jedis.flushDB();// ɾ����ǰ���ݿ�������key,�˷�������ʧ�ܡ�����  
  
        jedis.rpush("ids", "aa");  
        jedis.rpush("ids", "bb");  
        jedis.rpush("ids", "cc");  
  
        List<String> ids = jedis.lrange("ids", 0, -1);  
  
        jedis.set("aa", "{'name':'zhoujie','age':20}");  
        jedis.set("bb", "{'name':'yilin','age':28}");  
        jedis.set("cc", "{'name':'lucy','age':21}");  
        List<String> list = jedis.mget(ids.toArray(new String[ids.size()]));  
        System.out.println(list);  
    }  
  
    /** 
     * ��������lrange��list���з�ҳ���� 
     */  
    @Test  
    public void queryPageBy() {  
        int pageNo = 6;  
        int pageSize = 6;  
        Jedis jedis = RedisUtil.getJedis();  
        jedis.del("a");  
        for (int i = 1; i <= 30; i++) {  
            jedis.rpush("a", i + "");  
        }  
  
        int start = pageSize * (pageNo - 1);// ��Ϊredis��listԪ��λ�û�����0  
        int end = start + pageSize - 1;  
  
        List<String> results = jedis.lrange("a", start, end);// ��start����start��һ��Ԫ�أ��������Ǹ�Ԫ��  
        for (String str : results) {  
            System.out.println(str);  
        }  
  
    }  
  
    @Test  
    /** 
     * [��Redis listѹ��ID������ʵ�ʵ�����] 
        ������������� �����ǽ������󡱣��������Ǽ���Ϣ��ֱ��ѹ��Redis list����ͨ����Ӧ��ô���� 
        ���ڶ�����ܱ�������ã�������һ��list��ά����ʱ��˳����һ�������б����������ֻҪ�б�Ҫ�����������������list�У��ȵȡ� 
        �����ǻص�reddit.com�����ӣ����û��ύ�����ӣ����ţ���ӵ�list�У��и��ɿ��ķ���������ʾ�� 
        $ redis-cli incr next.news.id 
        (integer) 1 
        $ redis-cli set news:1:title "Redis is simple" 
        OK 
        $ redis-cli set news:1:url "http://code.google.com/p/redis" 
        OK 
        $ redis-cli lpush submitted.news 1 
        OK 
        ��������һ��key�������׵õ�һ����һ�޶�������ID��Ȼ��ͨ����ID��������CΪ�����ÿ���ֶ�����һ��key������¶����IDѹ��submitted.news list�� 
        ��ֻ��ţ��С�ԡ�������ο��ĵ��п��Զ������к�list�йص���������ɾ��Ԫ�أ���תlist������������ȡ������Ԫ�أ���ȻҲ������LLEN�õ�list�ĳ��ȡ� 
     */  
    public void testListStrUsage() {  
        String title = "̫��������ɫ��Դ4";  
        String url = "http://javacreazyer.iteye.com";  
        Jedis jedis = RedisUtil.getJedis();  
  
        long adInfoId = jedis.incr("ad:adinfo:next.id");  
        jedis.set("ad:adinfo:" + adInfoId + ":title", title);  
        jedis.set("ad:adinfo:" + adInfoId + ":url", url);  
        jedis.lpush("ad:adinfo", String.valueOf(adInfoId));  
  
        String resultTitle = jedis.get("ad:adinfo:" + adInfoId + ":title");  
        String resultUrl = jedis.get("ad:adinfo:" + adInfoId + ":url");  
        List<String> ids = jedis.lrange("ad:adinfo", 0, -1);  
        System.out.println(resultTitle);  
        System.out.println(resultUrl);  
        System.out.println(ids);  
  
        /** 
         * dbsize���ص�������key����Ŀ�������Ѿ����ڵģ� ��redis-cli keys "*"��ѯ�õ�������Ч��key��Ŀ 
         */  
        System.out.println(jedis.dbSize());  
  
        jedis.flushAll();  
    }  
  
    /** 
     * ������һ���򵥵ķ�������ÿ����ӱ�ǩ�Ķ�����һ����ǩID������֮���������Ҷ�ÿ�����еı�ǩ��һ�����ID��֮������ ����������ǵ�����ID 
     * 1000������������ǩtag 1,2,5��77���Ϳ������������������ϣ� $ redis-cli sadd news:1000:tags 1 
     * (integer) 1 $ redis-cli sadd news:1000:tags 2 (integer) 1 $ redis-cli 
     * sadd news:1000:tags 5 (integer) 1 $ redis-cli sadd news:1000:tags 77 
     * (integer) 1 $ redis-cli sadd tag:1:objects 1000 (integer) 1 $ redis-cli 
     * sadd tag:2:objects 1000 (integer) 1 $ redis-cli sadd tag:5:objects 1000 
     * (integer) 1 $ redis-cli sadd tag:77:objects 1000 (integer) 1 
     * Ҫ��ȡһ����������б�ǩ����˼򵥣� $ redis-cli smembers news:1000:tags 1. 5 2. 1 3. 77 4. 
     * 2 ����Щ����ȥ�����򵥵Ĳ�����Ȼ��ʹ����Ӧ��Redis��������ʵ�֡���������Ҳ������һ��ͬʱӵ�б�ǩ1, 2, 
     * 10��27�Ķ����б��������SINTER�����������������ڲ�ͬ����֮��ȡ�����������Ϊ��Ŀ������ֻ�裺 $ redis-cli sinter 
     * tag:1:objects tag:2:objects tag:10:objects tag:27:objects ... no result 
     * in our dataset composed of just one object ... 
     * ������ο��ĵ��п����ҵ��ͼ�����ص�����������˸���Ȥ��һץһ��ѡ�һ��Ҫ����SORT���Redis���Ϻ�list���ǿ�����ġ� 
     */  
    @Test  
    public void testSetUsage() {  
        Jedis jedis = RedisUtil.getJedis();  
        jedis.sadd("zhongsou:news:1000:tags", "1");  
        jedis.sadd("zhongsou:news:1000:tags", "2");  
        jedis.sadd("zhongsou:news:1000:tags", "5");  
        jedis.sadd("zhongsou:news:1000:tags", "77");  
        jedis.sadd("zhongsou:news:2000:tags", "1");  
        jedis.sadd("zhongsou:news:2000:tags", "2");  
        jedis.sadd("zhongsou:news:2000:tags", "5");  
        jedis.sadd("zhongsou:news:2000:tags", "77");  
        jedis.sadd("zhongsou:news:3000:tags", "2");  
        jedis.sadd("zhongsou:news:4000:tags", "77");  
        jedis.sadd("zhongsou:news:5000:tags", "1");  
        jedis.sadd("zhongsou:news:6000:tags", "5");  
  
        jedis.sadd("zhongsou:tag:1:objects", 1000 + "");  
        jedis.sadd("zhongsou:tag:2:objects", 1000 + "");  
        jedis.sadd("zhongsou:tag:5:objects", 1000 + "");  
        jedis.sadd("zhongsou:tag:77:objects", 1000 + "");  
  
        jedis.sadd("zhongsou:tag:1:objects", 2000 + "");  
        jedis.sadd("zhongsou:tag:2:objects", 2000 + "");  
        jedis.sadd("zhongsou:tag:5:objects", 2000 + "");  
        jedis.sadd("zhongsou:tag:77:objects", 2000 + "");  
  
        Set<String> sets = jedis.sinter("zhongsou:tag:1:objects",  
                "zhongsou:tag:2:objects", "zhongsou:tag:5:objects",  
                "zhongsou:tag:77:objects");  
        System.out.println(sets);  
        jedis.flushAll();  
    }  
  
    @Test  
    public void testSortedSetUsage() {  
        Jedis jedis = RedisUtil.getJedis();  
        jedis.zadd("zhongsou:hackers", 1940, "Alan Kay");  
        jedis.zadd("zhongsou:hackers", 1953, "Richard Stallman");  
        jedis.zadd("zhongsou:hackers", 1943, "Jay");  
        jedis.zadd("zhongsou:hackers", 1920, "Jellon");  
        jedis.zadd("zhongsou:hackers", 1965, "Yukihiro Matsumoto");  
        jedis.zadd("zhongsou:hackers", 1916, "Claude Shannon");  
        jedis.zadd("zhongsou:hackers", 1969, "Linus Torvalds");  
        jedis.zadd("zhongsou:hackers", 1912, "Alan Turing");  
  
        Set<String> hackers = jedis.zrange("zhongsou:hackers", 0, -1);  
        System.out.println(hackers);  
  
        Set<String> hackers2 = jedis.zrevrange("zhongsou:hackers", 0, -1);  
        System.out.println(hackers2);  
  
        // �������,��������Redis����score���ڸ����1920��֮���Ԫ�أ�������ֵҲ�����ˣ���  
        Set<String> hackers3 = jedis.zrangeByScore("zhongsou:hackers", "-inf",  
                "1920");  
        System.out.println(hackers3);  
  
        // ZREMRANGEBYSCORE ���������Ȼ����ã�����ȴ�ǳ����ã����᷵����ɾ����Ԫ��������  
        long num = jedis.zremrangeByScore("zhongsou:hackers", "-inf", "1920");  
        System.out.println(num);  
  
        jedis.flushAll();  
    }  
  
}  