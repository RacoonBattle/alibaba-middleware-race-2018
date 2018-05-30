package com.moekr.dubbo.agent;

import com.moekr.dubbo.agent.netty.NettyClientBootstrap;
import com.moekr.dubbo.agent.netty.NettyServerBootstrap;
import com.moekr.dubbo.agent.netty.RequestSender;
import com.moekr.dubbo.agent.netty.ResponseSender;
import com.moekr.dubbo.agent.protocol.codec.AgentMessageDecoder;
import com.moekr.dubbo.agent.protocol.codec.AgentMessageEncoder;
import com.moekr.dubbo.agent.protocol.codec.DubboRequestEncoder;
import com.moekr.dubbo.agent.protocol.codec.DubboResponseDecoder;
import com.moekr.dubbo.agent.protocol.converter.AgentToDubboRequestConverter;
import com.moekr.dubbo.agent.protocol.converter.DubboToAgentResponseConverter;
import com.moekr.dubbo.agent.registry.Registry;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.moekr.dubbo.agent.util.Constants.*;

@Component
@ConditionalOnProperty(name = AGENT_TYPE_PROPERTY, havingValue = PROVIDER_TYPE)
public class ProviderAgent {
	private final Registry registry;

	private SocketChannel channel;

	@Autowired
	public ProviderAgent(Registry registry) {
		this.registry = registry;
	}

	@PostConstruct
	public void initialize() throws Exception {
		int dubboPort = Integer.valueOf(System.getProperty(DUBBO_PORT_PROPERTY));
		int serverPort = Integer.valueOf(System.getProperty(SERVER_PORT_PROPERTY));
		int weight = Integer.valueOf(System.getProperty(PROVIDER_WEIGHT_PROPERTY));
		channel = new NettyClientBootstrap(LOCAL_HOST, dubboPort, new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel channel) {
				channel.pipeline()
						.addLast(new DubboRequestEncoder())
						.addLast(new DubboResponseDecoder())
						.addLast(new DubboToAgentResponseConverter())
						.addLast(new ResponseSender());
			}
		}).getSocketChannel();
		new NettyServerBootstrap(serverPort, new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel channel) {
				channel.pipeline()
						.addLast(new AgentMessageDecoder())
						.addLast(new AgentMessageEncoder())
						.addLast(new AgentToDubboRequestConverter())
						.addLast(new RequestSender(() -> ProviderAgent.this.channel));
			}
		});
		registry.register(SERVICE_NAME, serverPort, weight);
	}
}