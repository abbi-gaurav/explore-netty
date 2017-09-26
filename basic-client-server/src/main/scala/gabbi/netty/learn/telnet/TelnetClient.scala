package gabbi.netty.learn.telnet

import io.netty.bootstrap.Bootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.{NioServerSocketChannel, NioSocketChannel}
import io.netty.channel.{Channel, ChannelFuture}

import scala.annotation.tailrec
import scala.io.StdIn

object TelnetClient extends App {
  val eventLoopGroup = new NioEventLoopGroup()
  try {
    val bootstrap = new Bootstrap()
    bootstrap
      .group(eventLoopGroup)
      .channel(classOf[NioSocketChannel])
      .handler(new TelnetClientInitializer)

    val channel: Channel = bootstrap.connect("localhost", 8023).sync().channel()
    val finalState = talk(channel)

    if (finalState.isBye) {
      channel.closeFuture().sync()
    }

    finalState.channelFutureOpt.foreach(_.sync())

  } finally {
    eventLoopGroup.shutdownGracefully()
  }

  def talk(channel: Channel): State = {
    def readInput(): Option[String] = Option(StdIn.readLine()).filter(_.nonEmpty)

    def write(string: String): ChannelFuture = {
      channel.writeAndFlush(s"$string\r\n")
    }

    @tailrec
    def loop(channel: Channel, state: State, input: Option[String]): State = input match {
      case None => state
      case Some(x) if "bye".equalsIgnoreCase(x) => State(channelFutureOpt = Option(write(x)), isBye = true)
      case Some(x) => loop(
        channel = channel,
        state = State(channelFutureOpt = Option(write(x)), isBye = false),
        input = readInput()
      )
    }

    loop(channel, State.initial, readInput())
  }
}

case class State(channelFutureOpt: Option[ChannelFuture], isBye: Boolean)

object State {
  val initial = State(channelFutureOpt = None, isBye = false)
}
