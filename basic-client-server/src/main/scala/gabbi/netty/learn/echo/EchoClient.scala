package gabbi.netty.learn.echo

import gabbi.netty.learn.base.BaseClient
import io.netty.buffer.ByteBuf
import io.netty.channel.Channel
import io.netty.channel.socket.SocketChannel

object EchoClient extends App {
  override def main(args: Array[String]): Unit = {
    if (args.length != 2) System.exit(1)
    else {
      val client: BaseClient[SocketChannel] = BaseClient[SocketChannel, ByteBuf](host = args(0), port = args(1).toInt)(new EchoClientHandler)
      client.connect { (channel: Channel) =>
        channel.closeFuture().sync()
      }
    }
  }
}
