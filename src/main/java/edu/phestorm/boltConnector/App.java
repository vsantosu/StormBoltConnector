package edu.phestorm.boltConnector;

import java.util.Scanner;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		BoltConnector bcon = new BoltConnector();

		System.out.print("Enter mode:\n1. Server\n2. Client\nEnter Option:");

		int option = scan.nextInt();

		/* Enter Server mode */
		if (option == 1) {
			
			bcon.setReader(new BoltConnectorReader() {

				@Override
				public void read(ChannelHandlerContext conCtx, BoltConnectorMessage msg) {
					
					String m = msg.getString();
					
					System.out.println("Server got message: " + m);
					
					final ByteBuf buff = conCtx.alloc().buffer(m.length());
					buff.writeBytes(m.getBytes());
					  
					conCtx.writeAndFlush(buff);
				
				}

				@Override
				public void exceptionCatcher(Throwable cause) {
					// TODO Auto-generated method stub

				}
			});
			
			try {
				bcon.listen(8080);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else/* Enter Client mode */
		{
			bcon.setReader(new BoltConnectorReader() {

				@Override
				public void read(ChannelHandlerContext conCtx, BoltConnectorMessage msg) {

					System.out.println("Client Received echo: " + msg.getString());	
				}
				
				@Override
				public void exceptionCatcher(Throwable cause) {
					// TODO Auto-generated method stub
				}
			});
				
			bcon.connect("localhost", 8080);
			
			while(true)
			{	
				
				System.out.print("Enter message:");
				String str = scan.nextLine();
				
				
				if(bcon.isConnected())
					bcon.write(str);
				else
					System.out.println("Client Not Connected!");
			}
			
		}
			
		scan.close();
	}
}
