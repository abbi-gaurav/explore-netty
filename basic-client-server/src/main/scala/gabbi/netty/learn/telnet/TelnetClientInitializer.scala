package gabbi.netty.learn.telnet

import gabbi.netty.learn.telnet.TelnetClientInitializer._
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.string.{StringDecoder, StringEncoder}
import io.netty.handler.codec.{DelimiterBasedFrameDecoder, Delimiters}

class TelnetClientInitializer extends ChannelInitializer[SocketChannel] {
  override def initChannel(ch: SocketChannel): Unit = {
    ch.pipeline().addLast(
      new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter(): _*),
      decoder,
      encoder,
      clientHandler
    )
  }
}

object TelnetClientInitializer {
  val encoder = new StringEncoder()
  val decoder = new StringDecoder()
  val clientHandler = new TelnetClientHandler
}
