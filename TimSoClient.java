package Timso;

// Graphics
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

// Socket 
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

// public class TimSoClient extends JFrame implements MouseListener {
public class TimSoClient extends JFrame implements MouseListener, Runnable { // Runnable cho new Thread(this).start();
	public static void main(String[] args) {
		System.out.print("Run TimSo Offline");
		
//		không được code ntn vì nó sẽ gọi cả hàm khởi tạo TimSoClient() làm cho cái bảng xuất hiện 
//		TimSoClient t = new TimSoClient();
//		t.LoginFrame("");
		
//		 ta phải gọi như thế này , gọi trực tiếp giống như bên php phần static methods 
		TimSoClient.LoginFrame(""); // để gọi trực tiếp ntn thì ta phải để cho nó là static [19]
	}

	// int n = 10;
	int n = 10; // phải giống n bên client
	int s = 50;
	int os = 50;
	static int thutu; // [16] // [20]

	DataInputStream dis; /// +++
	DataOutputStream dos; /// +++

	int matran[][] = new int[n][n]; // [12.a]
	Random rand = new Random();
	List<Point> dadanh = new ArrayList<Point>();
	ArrayList<Integer> dsthutudanh = new ArrayList<>();

	// int dsthutudanh[] = new int[n*n];

	public static JFrame frame; // vì hàm LoginFrame là static nên để truy cập đến biến frame thì 
	// biến này cũng phải để là static 

	public static void LoginFrame(String ms) { // [19] thêm static để có thể gọi trực tiếp TimSoClient.LoginFrame("");
		frame = new JFrame("Login");
		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);

		JLabel lname = new JLabel("Message");
		lname.setBounds(50, 50, 50, 50);
		frame.add(lname);

		final JTextField Name = new JTextField("");
		Name.setBounds(120, 50, 200, 50);
		frame.add(Name);
		final JLabel msg = new JLabel("Msg:" + ms);
		msg.setBounds(120, 200, 200, 50);
		frame.add(msg);

		JButton OK = new JButton("Login");
		OK.setBounds(120, 120, 200, 50);
		OK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (Name.getText().equals("want to join")) {
					new TimSoClient(); // sau khi TimSoClient được khởi tạo thì thutu đã có giá trị 
					
					if(thutu <= 4) {
						JOptionPane.showMessageDialog(null, "You are a player "+thutu); 
//						msg.setText("You are a player "+thutu);
					}
					else {
						JOptionPane.showMessageDialog(null, "You are a viewer "+thutu); 
//						msg.setText("You are a viewer "+thutu);
					}
					
					// [20] vì LoginFrame static nên thutu cũng static 
					
				    //Sau 2 giây thì mới tắt (để người dùng còn đọc thông báo
					TimSoClient.setTimeoutSync(() -> frame.dispose(), 2000);
					
				} else
					msg.setText("Msg: Message False !");
			}
		}

		);

		frame.add(OK);

		frame.setVisible(true);
	}
	public static void setTimeoutSync(Runnable runnable, int delay) {
	    try {
	        Thread.sleep(delay);
	        runnable.run();
	    }
	    catch (Exception e){
	        System.err.println(e);
	    }
	}
	public TimSoClient() {

		this.setSize(n * s + 2 * os, n * s + 2 * os);
		this.setTitle("Caro");
		this.setDefaultCloseOperation(3);
		this.addMouseListener(this);

		// [12]

		// for (int i = 0; i < n; i++) {
		// for (int j = 0; j < n; j++) {
		// matran[i][j] = i * n + j + 1;
		// }
		// }
		// for (int r = 0; r < n * n; r++) {
		// int i1 = rand.nextInt(n);
		// int j1 = rand.nextInt(n);
		// int i2 = rand.nextInt(n);
		// int j2 = rand.nextInt(n);
		// int tmp = matran[i1][j1];
		// matran[i1][j1] = matran[i2][j2];
		// matran[i2][j2] = tmp;
		// }

		// [12]

		/// +++
		try {
			// Socket soc = new Socket("192.168.1.119",5000);
//			Socket soc = new Socket("172.21.112.1", 5000); // wifi ở nhà // cổng 5000 giống bên server
//			 Socket soc = new Socket("192.168.61.1",5000);
			// Socket soc = new Socket("localhost",5000);
			// Socket soc = new Socket("192.168.10.62",5000);
			 Socket soc = new Socket("192.168.43.102",5000); // IP Phuc
			// Socket soc = new Socket("192.168.43.100",5000); // IP Nguyen
			// Socket soc = new Socket("192.168.43.4",5000); // IP Manh
			dis = new DataInputStream(soc.getInputStream());
			dos = new DataOutputStream(soc.getOutputStream());

			// [14]
			try {
				String s = dis.readUTF(); // nhận một chuỗi từ Server
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < n; j++) {
						matran[i][j] = Integer.parseInt(s.split(",")[i * n + j]);
					}
				}
				thutu = Integer.parseInt(s.split(",")[n * n]); // [16]
				System.out.print("thu tu : " + thutu + "\n");
				this.repaint(); // sau khi nhận bảng từ server xong để cho chắc thì ta repaint lại lần nữa
			} catch (Exception e) {

			}
			// [14]

		} catch (Exception e) {

		}
		new Thread(this).start();
		/// +++

		this.setVisible(true);
	}

	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		// g.setColor(Color.YELLOW);
		for (int i = 0; i < dadanh.size(); i++) {
			// System.out.print("thutu danh :"+dsthutudanh[i]);
			if (dsthutudanh.get(i) == 1)
				g.setColor(Color.GREEN);
			else if (dsthutudanh.get(i) == 2)
				g.setColor(Color.RED);
			else if (dsthutudanh.get(i) == 3)
				g.setColor(new Color(192, 0, 255));
			else if (dsthutudanh.get(i) == 4)
				g.setColor(Color.YELLOW);
			else {
			}
			;
			//
			int ix = dadanh.get(i).x;
			int iy = dadanh.get(i).y;
			int x = os + ix * s;
			int y = os + iy * s;
			g.fillRect(x, y, s, s);
		}

		g.setColor(Color.BLACK);
		for (int i = 0; i <= n; i++) {
			g.drawLine(os, os + i * s, os + n * s, os + i * s);
			g.drawLine(os + i * s, os, os + i * s, os + n * s);
		}

		g.setFont(new Font("arial", Font.BOLD, s / 3));
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				String str = matran[i][j] + "";
				if (matran[i][j] < 10)
					str = "00" + str;
				else if (matran[i][j] < 100)
					str = "0" + str;

				int x = os + i * s + s / 2 - s / 4;
				int y = os + j * s + s / 2 + s / 4 - s / 8;
				g.drawString(str, x, y);
			}
		}
	}

	/// +++
	public void run() {
		while (true) {
			try {
				String s = dis.readUTF();
				int ix = Integer.parseInt(s.split(",")[0]);
				int iy = Integer.parseInt(s.split(",")[1]);
				int nguoidanh;

				if (s.split(",").length == 3) {
					nguoidanh = Integer.parseInt(s.split(",")[2]);
					dsthutudanh.add(nguoidanh);
				}

				System.out.print(ix + " " + iy + "\n");

				dadanh.add(new Point(ix, iy));
				this.repaint();
			} catch (Exception e) {

			}
		}
	}
	/// +++

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		System.out.print(x + " " + y + "\n");
		if (x < os || x >= os + n * s)
			return;
		if (y < os || y >= os + n * s)
			return;
		int ix = (x - os) / s;
		int iy = (y - os) / s;
		System.out.print(ix + " " + iy);
		for (Point d : dadanh) {
			if (ix == d.x && iy == d.y)
				return;
		}
		if (matran[ix][iy] != dadanh.size() + 1)
			return;

		// thay vì ở Offine thì ta đánh , còn ở đây là client thì ta sẽ gửi tạo độ lên
		// server

		// Offilen
		// dadanh.add(new Point(ix, iy));
		// this.repaint();

		// Client
		// Gửi tọa độ lên Server

		try {
			// dos.writeUTF(ix + "," + iy);
			System.out.print("thu tu khi click : " + thutu + "\n");
			dos.writeUTF(ix + "," + iy + "," + thutu); // [17] gửi thêm thứ tự người chơi cho server
			System.out.print(ix + " " + iy);
		} catch (Exception e1) { // chú ý là nên khai báo biến khác khác cho an toàn nên ta đặt e1 chứ không phải
									// e
			System.out.print("Error");
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

}
