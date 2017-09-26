package gabbi.netty.learn.base

import java.net.InetSocketAddress

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel

class BaseServer[C <: Channel](channelInitializer: ChannelInitializer[C], port: Int = 8080) {
  def start(): Unit = {
    val eventLoopGroup: EventLoopGroup = new NioEventLoopGroup()
    try {
      val serverBootstrap = new ServerBootstrap()
      serverBootstrap
        .group(eventLoopGroup)
        .channel(classOf[NioServerSocketChannel])
        .localAddress(new InetSocketAddress(port))
        .childHandler(channelInitializer)

      val channelFuture: ChannelFuture = serverBootstrap.bind().sync()
      channelFuture.channel().closeFuture().sync()
    } finally {
      eventLoopGroup.shutdownGracefully().sync()
    }
  }

}

object BaseServer {
  def apply[C <: Channel](channelInboundHandlerAdapter: ChannelInboundHandlerAdapter, port: Int = 8080) = new BaseServer[C](
    new ChannelInitializer[C] {
      override def initChannel(channel: C): Unit = channel.pipeline().addLast(channelInboundHandlerAdapter)
    },
    port

  )
}
