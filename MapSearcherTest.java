package com.sds.ioffice.common.adm.utl;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapSearcherTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapSearcherTest.class);

	private Map<String, Object> test = Maps.newHashMap();

	@Before
	public void init(){
		Map<String, Object> status = Maps.newHashMap();
		status.put("a", 1);
		status.put("b", "ha");
		status.put("c", false);
		Map<String, Object> tasks = Maps.newHashMap();
		tasks.put("status", status);
		Map<String, Object> nodes = Maps.newHashMap();
		nodes.put("tasks", tasks);

		test.put("nodes", nodes);
	}

	@Test
	public void getMap() {
		Object mapType = new MapSearcher<>().get(test,"nodes.tasks.status");
		Assert.assertEquals(mapType.getClass().getName(), HashMap.class.getName());
	}
	
	@Test
	public void getString() {
		Object stringType = new MapSearcher<>().get(test,"nodes.tasks.status.b");
		Assert.assertEquals(stringType.getClass().getName(), String.class.getName());
		Assert.assertEquals((String) stringType, "ha");
	}

	@Test
	public void getBool() {
		Object boolType = new MapSearcher<>().get(test,"nodes.tasks.status.c");
		Assert.assertEquals(boolType.getClass().getName(), Boolean.class.getName());
		Assert.assertEquals((boolean) boolType, false);
	}

	@Test
	public void getInteger() {
		Object intType = new MapSearcher<Object>().get(test,"nodes.tasks.status.a");
		Assert.assertEquals(intType.getClass().getName(), Integer.class.getName());
		Assert.assertEquals((int) intType, 1);
	}

	@Test
	public void getDefault() {
		Integer result = new MapSearcher<Integer>().getOrDeafult(test,"a.b.c.d.e", 1);
		Assert.assertEquals(result.intValue(), 1);
	}

	@Test
	public void wrong_path_first_element() {
		try{
			new MapSearcher<>().get(test,"qwe.qwe.qwe");
		}catch (Exception e){
			LOGGER.info("wrong_path_first_element - error message : {}", e.getMessage());
			Assert.assertEquals(e.getClass(), IllegalArgumentException.class);
		}
	}

	@Test
	public void wrong_path_second_element() {
		try{
			new MapSearcher<>().get(test,"nodes.qwe.qwe");
		}catch (Exception e){
			LOGGER.info("wrong_path_second_element - error message : {}", e.getMessage());
			Assert.assertEquals(e.getClass(), IllegalArgumentException.class);
		}
	}

	@Test
	public void wrong_return_type() {
		try{
			new MapSearcher<>().get(test,"nodes.tasks.status.c");
		}catch (Exception e){
			LOGGER.info("wrong_return_type - error message : {}", e.getMessage());
			Assert.assertEquals(e.getClass(), IllegalArgumentException.class);
		}
	}
}