package gabbi.netty.learn.echo

import io.netty.buffer.{ByteBuf, Unpooled}
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}
import io.netty.util.CharsetUtil

@Sharable
class EchoClientHandler extends SimpleChannelInboundHandler[ByteBuf] {

  override def channelActive(ctx: ChannelHandlerContext): Unit = {
    ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8))
  }

  override def channelRead0(ctx: ChannelHandlerContext, msg: ByteBuf): Unit = {
    println(s"client received : ${msg.toString(CharsetUtil.UTF_8)}")
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    cause.printStackTrace()
    ctx.close()
  }
}
