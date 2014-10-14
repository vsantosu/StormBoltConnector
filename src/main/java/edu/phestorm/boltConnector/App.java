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
			
			System.out.print("Enter port:");
			int port = scan.nextInt();
			System.out.println();
			
			bcon.setReader(new BoltConnectorReader() {

				@Override
				public void read(ChannelHandlerContext conCtx, BoltConnectorMessage msg) {
					
					String m = msg.getString();
					String[] tuples = m.split("<>");
					
					for (int i = 0; i < tuples.length; i++) {
						System.out.println(tuples[i]);
					}
					

					/* THIS SENDS A MESSAGE BACK TO THE CONTEXT
					final ByteBuf buff = conCtx.alloc().buffer(m.length());
					buff.writeBytes(m.getBytes());			  
					conCtx.writeAndFlush(buff);
					*/
				}

				@Override
				public void exceptionCatcher(Throwable cause) {
					// TODO Auto-generated method stub

				}
			});
			
			try {
				bcon.listen(port);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else/* Enter Client mode */
		{
			
			System.out.print("Enter port:");
			int port = scan.nextInt();
			System.out.print("Enter host:");
			String host = scan.next();
			System.out.println();
			
			bcon.setReader(new BoltConnectorReader() {

				@Override
				public void read(ChannelHandlerContext conCtx, BoltConnectorMessage msg) {

					System.out.println("Received Message: " + msg.getString());	
				}
				
				@Override
				public void exceptionCatcher(Throwable cause) {
					// TODO Auto-generated method stub
				}
			});
				
			bcon.connect(host, port);
			
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
