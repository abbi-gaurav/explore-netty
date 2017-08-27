package gabbi.netty.learn.basic

import java.net.InetSocketAddress

import io.netty.bootstrap.Bootstrap
import io.netty.channel.{ChannelInitializer, EventLoopGroup}
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel

class EchoClient(host: String, port: Int) {
  def start(): Unit = {
    val eventLoopGroup: NioEventLoopGroup = new NioEventLoopGroup()
    try {
      val bootstrap: Bootstrap = new Bootstrap()
      bootstrap
        .group(eventLoopGroup)
        .channel(classOf[NioSocketChannel])
        .remoteAddress(new InetSocketAddress(host, port))
        .handler(new ChannelInitializer[SocketChannel] {
          override def initChannel(ch: SocketChannel): Unit = {
            ch.pipeline().addLast(new EchoClientHandler)
          }
        })
      val channelFuture = bootstrap.connect().sync()
      channelFuture.channel().closeFuture().sync()
    } finally {
      eventLoopGroup.shutdownGracefully().sync()
    }
  }

}

object EchoClient extends App {
  override def main(args: Array[String]): Unit = {
    if (args.length != 2) System.exit(1)
    else new EchoClient(host = args(0), port = args(1).toInt).start()
  }
}
