package gabbi.netty.learn.telnet

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.string.{StringDecoder, StringEncoder}
import io.netty.handler.codec.{DelimiterBasedFrameDecoder, Delimiters}

class TelnetServerInitializer extends ChannelInitializer[SocketChannel] {

  import TelnetServerInitializer._

  override def initChannel(ch: SocketChannel): Unit = {
    val pipeline = ch.pipeline()

    pipeline.addLast(
      new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter(): _*),
      decoder,
      encoder,
      serverHandler
    )
  }
}

object TelnetServerInitializer {
  val decoder = new StringDecoder()
  val encoder = new StringEncoder()
  val serverHandler = new TelnetServerHandler
}
