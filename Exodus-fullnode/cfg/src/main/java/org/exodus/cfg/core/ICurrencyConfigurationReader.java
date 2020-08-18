package org.exodus.cfg.core;

/**
 * 
 * 
 *  All rights reserved.
 * 
 * @Description: TODO
 * @author: Francis.Deng [francis_xiiiv@163.com]
 * @date: Sep 11, 2019 11:37:45 PM
 * @version: V1.0
 * @see CurrencyYamlReader
 */
public interface ICurrencyConfigurationReader {
	public static String INTERVALUE_CONF_FILE_VARIABLE_NAME = "intervalue_conf_file";

	ICurrencyConf read(String[] args);

	public static ICurrencyConfigurationReader getDefaultImpl() {
		return (ICurrencyConfigurationReader) ReflectionUtils
				.getInstanceByClassName("org.exodus.cfg.core.InterValueYamlReader");
	}
}
