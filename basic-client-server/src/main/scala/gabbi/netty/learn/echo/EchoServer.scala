package gabbi.netty.learn.echo

import gabbi.netty.learn.base.BaseServer
import io.netty.channel.socket.SocketChannel

import scala.util.Try

object EchoServer extends App {

  override def main(args: Array[String]): Unit = {
    val port: Int = if (args.nonEmpty) Try(args(0).toInt).getOrElse(8080) else 8080
    BaseServer[SocketChannel](new EchoServerHandler, port).start()
  }

}
