package bank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		new Client().start();
	}

	private Scanner scanner = new Scanner(System.in);
	DataInputStream dis;
	DataOutputStream dos;

	public void start() throws UnknownHostException, IOException {
		
		Socket server = new Socket("127.0.0.1", 8888);
		System.out.println("成功连接服务器");
		InetAddress addr = server.getInetAddress();
		System.out.println("服务器端的主机地址：" + addr.getHostAddress());
		System.out.println("服务器端的IP地址：" + Arrays.toString(addr.getAddress()));

		InputStream in =server.getInputStream();
		OutputStream out = server.getOutputStream();

		dis = new DataInputStream(in);
		dos = new DataOutputStream(out);

		boolean running = true;
		while (running) {
			System.out.println("****************");
			System.out.println("**     1.开户       **");
			System.out.println("**     2.存款       **");
			System.out.println("**     3.取款       **");
			System.out.println("**     4.转账       **");
			System.out.println("**     0.退出       **");
			System.out.println("****************");
			System.out.print("请输入：");
			String command = scanner.nextLine();
			switch (command) {
			case "1":
				register();
				break;
			case "2":
				diposit();
				break;
			case "3":
				withdraw();
				break;
			case "4":
				transfer();
				break;
			case "0":
				System.out.println("byebye");
				running = false;
			}

		}
		server.close();
		scanner.close();
	}

	// 开户
	public void register() throws IOException {
		System.out.println("请输入新的账户");
		String cardno =scanner.nextLine();
		dos.writeUTF("register");
		dos.writeUTF(cardno);
		dos.flush();
		String ret = dis.readUTF();
		System.out.println(ret);
	}

	// 存款
	public void diposit() throws IOException {
		System.out.print("请输入用户账号：");
		String cardno = scanner.nextLine();
		System.out.println("请输入存款金额：");
		float money = scanner.nextFloat();
		dos.writeUTF("diposit");
		dos.writeUTF(cardno);
		dos.writeFloat(money);
		dos.flush();
		String ret = dis.readUTF();
		System.out.println(ret);
	}

	// 取款
	public void withdraw() throws IOException {
		System.out.print("请输入用户账号：");
		String cardno = scanner.nextLine();
		System.out.println("请输入取款金额：");
		float money = scanner.nextFloat();
		dos.writeUTF("withdraw");
		dos.writeUTF(cardno);
		dos.writeFloat(money);
		dos.flush();
		String ret = dis.readUTF();
		System.out.println(ret);
		
	}

	// 转账
	public void transfer() throws IOException {
		System.out.print("请输入用户账号：");
		String cardno1 = scanner.nextLine();
		System.out.println("请输入您要转账的账号");
		String cardno2 = scanner.nextLine();
		System.out.println("请输入您要转账的金额：");
		float money =scanner.nextFloat();
		dos.writeUTF("transfer");
		dos.writeUTF(cardno1);
		dos.writeUTF(cardno2);
		dos.writeFloat(money);
		dos.flush();
		String ret = dis.readUTF();
		System.out.println(ret);
	}

}
