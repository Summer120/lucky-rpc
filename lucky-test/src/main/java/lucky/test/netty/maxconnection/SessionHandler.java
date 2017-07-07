package lucky.test.netty.maxconnection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import remoting.server.ChannelState;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 10:07 2017/6/10
 */

@ChannelHandler.Sharable
public class SessionHandler extends ChannelInboundHandlerAdapter {

    private DefaultChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        //添加系统调用服务信息
        channel.attr(ChannelState.KEY).set(new ChannelState());
        channels.add(channel);
        //就不会再次的调用后面得channelactive了，如果想再次调用需要
        //通过责任链向后面传递
//        ctx.fireChannelActive();
//        super.channelActive(ctx);
    }


    public DefaultChannelGroup getChannels() {
        return channels;
    }

    public void setChannels(DefaultChannelGroup channels) {
        this.channels = channels;
    }
}
