package com.jadeStone.thirdLib.netty.chapter2FileServerDemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class Server {

	public static void main(String[] args) {
		int port = 8081;
		String url="/src/main/java/com/jadeStone/netty";
		
		new Server().run(port, url);
	}
	
	public void run(int port, String uri) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup)
		.channel(NioServerSocketChannel.class)
		.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
				ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
				ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
				ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
				ch.pipeline().addLast("fileServerHandler", new HttpFileServerHandler());
			}
			
		});
		ChannelFuture future;
		try {
			future = b.bind("localhost", port).sync();
			future.channel().closeFuture().sync();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
		System.out.println("文件目录服务器已启动");
		
		
	}
}
