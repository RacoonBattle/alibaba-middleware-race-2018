package com.moekr.dubbo.agent.netty;

import com.moekr.dubbo.agent.protocol.AgentResponse;
import com.moekr.dubbo.agent.util.ContextHolder;
import com.moekr.dubbo.agent.util.RequestContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProviderResponseSender extends SimpleChannelInboundHandler<AgentResponse> {
	@Override
	protected void channelRead0(ChannelHandlerContext context, AgentResponse response) {
		RequestContext requestContext = ContextHolder.remove(response.getId());
		if (requestContext != null) {
			requestContext.getChannel().writeAndFlush(response);
		}
	}
}
