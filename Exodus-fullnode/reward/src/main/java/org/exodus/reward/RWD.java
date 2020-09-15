package org.exodus.reward;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.exodus.reward.util.ThreadPool;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "rwd")
public class RWD {
	public static final String VERSION = "1.2.3";
	public static final String APPLICATION = "NRS";

	private static final Properties defaultProperties = new Properties();
	static {
		log.info("Initializing server version " + RWD.VERSION);
		try (InputStream is = ClassLoader.getSystemResourceAsStream("rwd-default.properties")) {
			if (is != null) {
				RWD.defaultProperties.load(is);
			} else {
				String configFile = System.getProperty("rwd-default.properties");
				if (configFile != null) {
					try (InputStream fis = new FileInputStream(configFile)) {
						RWD.defaultProperties.load(fis);
					} catch (IOException e) {
						throw new RuntimeException("Error loading rwd-default.properties from " + configFile);
					}
				} else {
					throw new RuntimeException(
							"rwd-default.properties not in classpath and system property nxt-default.properties not defined either");
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Error loading rwd-default.properties", e);
		}
	}
	private static final Properties properties = new Properties(defaultProperties);
	static {
		try (InputStream is = ClassLoader.getSystemResourceAsStream("rwd.properties")) {
			if (is != null) {
				RWD.properties.load(is);
			} // ignore if missing
		} catch (IOException e) {
			throw new RuntimeException("Error loading rwd.properties", e);
		}
	}

	public static int getIntProperty(String name) {
		try {
			int result = Integer.parseInt(properties.getProperty(name));
			log.info(name + " = \"" + result + "\"");
			return result;
		} catch (NumberFormatException e) {
			log.info(name + " not defined, assuming 0");
			return 0;
		}
	}

	public static String getStringProperty(String name) {
		return getStringProperty(name, null);
	}

	public static String getStringProperty(String name, String defaultValue) {
		String value = properties.getProperty(name);
		if (value != null && !"".equals(value)) {
			log.info(name + " = \"" + value + "\"");
			return value;
		} else {
			log.info(name + " not defined");
			return defaultValue;
		}
	}

	public static List<String> getStringListProperty(String name) {
		String value = getStringProperty(name);
		if (value == null || value.length() == 0) {
			return Collections.emptyList();
		}
		List<String> result = new ArrayList<>();
		for (String s : value.split(";")) {
			s = s.trim();
			if (s.length() > 0) {
				result.add(s);
			}
		}
		return result;
	}

	public static Boolean getBooleanProperty(String name) {
		String value = properties.getProperty(name);
		if (Boolean.TRUE.toString().equals(value)) {
			log.info(name + " = \"true\"");
			return true;
		} else if (Boolean.FALSE.toString().equals(value)) {
			log.info(name + " = \"false\"");
			return false;
		}
		log.info(name + " not defined, assuming false");
		return false;
	}

	public static void main(String[] args) {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.setAllowCircularReferences(false);
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(beanFactory);
		context.register(DefaultConfig.class);

		context.refresh();

		ThreadPool.start(1000);
		FullNodeBlock block = context.getBean(FullNodeBlock.class);
		block.awaitTermination();

	}

}
