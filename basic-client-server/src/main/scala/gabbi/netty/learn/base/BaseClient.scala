package gabbi.netty.learn.base

import java.net.InetSocketAddress

import io.netty.bootstrap.Bootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.{Channel, ChannelInitializer, SimpleChannelInboundHandler}

class BaseClient[C <: Channel](host: String = "localhost", port: Int = 8080)(channelInitializer: ChannelInitializer[C]) {
  def connect(work: Channel => Unit): Unit = {
    val eventLoopGroup: NioEventLoopGroup = new NioEventLoopGroup()
    try {
      val bootstrap: Bootstrap = new Bootstrap()

      bootstrap
        .group(eventLoopGroup)
        .channel(classOf[NioSocketChannel])
        .remoteAddress(new InetSocketAddress(host, port))
        .handler(channelInitializer)

      work(bootstrap.connect().sync().channel())
    } finally {
      eventLoopGroup.shutdownGracefully().sync()
    }
  }
}

object BaseClient {
  def apply[C <: Channel, T](host: String = "localhost", port: Int = 8080)(simpleChannelInboundHandler: SimpleChannelInboundHandler[T]): BaseClient[C] = {
    new BaseClient[C](host, port)(new ChannelInitializer[C] {
      override def initChannel(channel: C): Unit = channel.pipeline().addLast(simpleChannelInboundHandler)
    })
  }
}
