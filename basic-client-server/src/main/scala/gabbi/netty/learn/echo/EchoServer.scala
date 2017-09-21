package gabbi.netty.learn.echo

import java.net.InetSocketAddress

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.{ChannelFuture, ChannelInitializer}
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel

import scala.util.Try

class EchoServer(port: Int) {
  def start(): Unit = {
    val echoServerHandler = new EchoServerHandler
    val eventLoopGroup = new NioEventLoopGroup()
    try {
      val serverBootstrap = new ServerBootstrap()
      serverBootstrap
        .group(eventLoopGroup)
        .channel(classOf[NioServerSocketChannel])
        .localAddress(new InetSocketAddress(port))
        .childHandler(new ChannelInitializer[SocketChannel] {
          override def initChannel(ch: SocketChannel): Unit = ch.pipeline().addLast(echoServerHandler)
        })

      val channelFuture: ChannelFuture = serverBootstrap.bind().sync()
      channelFuture.channel().closeFuture().sync()
    } finally {
      eventLoopGroup.shutdownGracefully().sync()
    }
  }
}

object EchoServer extends App {
  private lazy val defaultPort: Int = 8080

  override def main(args: Array[String]): Unit = {
    println(defaultPort)
    val port: Int = if (args.nonEmpty) Try(args(0).toInt).getOrElse(defaultPort) else defaultPort
    new EchoServer(port).start()
  }
}
