package com.taotao.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class JedisTest {
	@Test
	public void testJedisCluster()throws Exception{
		//创建一个jedisCluster对象
		Set<HostAndPort> nodes = new HashSet<>();
		//向集合中添加节点
		nodes.add(new HostAndPort("192.168.25.88",7001));
		nodes.add(new HostAndPort("192.168.25.88",7002));
		nodes.add(new HostAndPort("192.168.25.88",7003));
		nodes.add(new HostAndPort("192.168.25.88",7004));
		nodes.add(new HostAndPort("192.168.25.88",7005));
		nodes.add(new HostAndPort("192.168.25.88",7006));
		JedisCluster jc = new JedisCluster(nodes);
		jc.set("key", "jedisclustervalue");
		String string = jc.get("key");
		System.out.println(string);
		jc.close();
	}
}
