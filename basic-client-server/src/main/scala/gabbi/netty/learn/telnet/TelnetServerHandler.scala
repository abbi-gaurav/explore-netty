package gabbi.netty.learn.telnet

import java.net.InetAddress

import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.{ChannelFuture, ChannelFutureListener, ChannelHandlerContext, SimpleChannelInboundHandler}

@Sharable
class TelnetServerHandler extends SimpleChannelInboundHandler[String] {


  override def channelActive(ctx: ChannelHandlerContext): Unit = {
    ctx.write(s"welcome to ${InetAddress.getLocalHost.getHostName}!\r\n")
    ctx.flush()
  }

  override def channelRead0(ctx: ChannelHandlerContext, msg: String): Unit = {
    val response = Response(msg)

    val channelFuture: ChannelFuture = ctx.write(response.string)

    if (response.close) {
      channelFuture.addListener(ChannelFutureListener.CLOSE)
    }
  }

  override def channelReadComplete(ctx: ChannelHandlerContext): Unit = {
    ctx.flush()
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    cause.printStackTrace()
    ctx.close()
  }
}

case class Response(string: String, close: Boolean)

object Response {
  def apply(request: String): Response = request match {
    case x if x.isEmpty => Response(string = s"Please type something.\r\n", close = false)
    case x if "bye".equalsIgnoreCase(request) => Response("Have a good day!\r\n", close = true)
    case _ => Response("Did you say '" + request + "'?\r\n", close = false)
  }
}