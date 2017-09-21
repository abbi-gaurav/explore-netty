package gabbi.netty.learn.telnet

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.{LogLevel, LoggingHandler}

object TelnetServer extends App {
  val bossGroup = new NioEventLoopGroup()
  val workerGroup = new NioEventLoopGroup()

  try {
    val serverBootstrap = new ServerBootstrap()
    serverBootstrap
      .group(bossGroup, workerGroup)
      .channel(classOf[NioServerSocketChannel])
      .handler(new LoggingHandler(LogLevel.INFO))
      .childHandler(new TelnetServerInitializer)

    serverBootstrap
      .bind(8023)
      .sync()
      .channel()
      .closeFuture().sync()
  } finally {
    bossGroup.shutdownGracefully()
    workerGroup.shutdownGracefully()
  }
}
