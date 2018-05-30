package com.moekr.dubbo.agent.util;

public abstract class Constants {
	public static final String AGENT_TYPE_PROPERTY = "type";
	public static final String ETCD_ADDRESS_PROPERTY = "etcd.url";
	public static final String SERVER_PORT_PROPERTY = "server.port";
	public static final String PROVIDER_WEIGHT_PROPERTY = "weight";
	public static final String DUBBO_PORT_PROPERTY = "dubbo.protocol.port";

	public static final String LOCAL_HOST = "localhost";

	public static final String CONSUMER_TYPE = "consumer";
	public static final String PROVIDER_TYPE = "provider";

	public static final String SERVICE_NAME = "com.alibaba.dubbo.performance.demo.provider.IHelloService";

	public static final String ETCD_ROOT_PATH = "DUBBO_AGENT";

	public static final String HTTP_ID_HEADER = "X-REQ-ID";

	public static final int RETRY_TIME = 10;
	public static final long RETRY_DELAY = 2000;
}
