package Timso;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Random;

// GHI CHÚ [11] 

public class TimSoServer {

	public static void main(String[] args) { // hàm main được chạy đầu tiên 
		System.out.print("Run TimSoServer");
		new TimSoServer(); // gọi hàm TimSoServer() 
	}

	int n = 10; // phải giống bên client
	
	// [12] khai báo mảng tại server 
	int matran[][] = new int[n][n];
	Random rand = new Random();

	Vector<Xuly> cls = new Vector<Xuly>(); // mảng các người vào chơi
	
	List<Point> dadanh = new ArrayList<Point>();
	
	// Tạo mảng lưu thứ tự người đánh tại server 
	ArrayList<Integer> dsthutudanh = new ArrayList<>();

	public TimSoServer() {

		// [12]
		// xây dựng bảng random ngay tại server
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				matran[i][j] = i * n + j + 1;
			}
		}
		for (int r = 0; r < n * n; r++) {
			int i1 = rand.nextInt(n);
			int j1 = rand.nextInt(n);
			int i2 = rand.nextInt(n);
			int j2 = rand.nextInt(n);
			int tmp = matran[i1][j1];
			matran[i1][j1] = matran[i2][j2];
			matran[i2][j2] = tmp;
		}

		// [12]

		// [12.5]
		try { 
			ServerSocket server = new ServerSocket(5000);
			while (true) { // lắng nghe liên tục , cứ người nào vào thì
				Socket soc = server.accept(); // accept cho người đó vào
				System.out.print("so luong nguoi vao choi : "+cls.size()+"\n");
				Xuly x = new Xuly(soc, this, n, matran,cls.size()+1); // [12.1]
				cls.add(x); // add người x đó vào mảng cls
				x.start();
			}

		} catch (Exception e) {

		}

	}
}


// [12]
// [12.2]
class Xuly extends Thread {
	TimSoServer server;
	Socket soc;
	int n;
	int matran[][]; // [12.2]
	int soluong;
//	int dsthutudanh[];
	String ss2 = "";

	public Xuly(Socket soc, TimSoServer server, int n, int matran[][],int soluong) { // [12.2]
		this.soc = soc;
		this.server = server; 

		// [12a]
		this.n = n;
		this.matran = matran; // [12.2]
//		this.dsthutudanh = dsthutudanh;
		
		this.soluong = soluong; // người mới vào là người thứ soluong

		try {
			String ss = "";
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					ss += matran[i][j] + ","; // [12.3]
				}
			}
			
			ss+=soluong; // [15]
			DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
			dos.writeUTF(ss); // [12.4]
			System.out.print("ss:" + ss);
		} catch (Exception e) {
		}
		// [12a]
	}
// [12]
	
	public void run() {
		for (int i=0;i<server.dadanh.size();i++) { // lặp qua các điểm đã đánh 
			// luồng Server gửi chuỗi về client
			try {
				DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
				// gửi về tọa độ điểm kèm theo người đánh điểm đó 
				ss2 = server.dadanh.get(i).x + "," + server.dadanh.get(i).y + "," + server.dsthutudanh.get(i) ;
				dos.writeUTF(ss2);
			} catch (Exception e) {
			}
		}

		loop: while (true) {
			// Server nhận chuỗi từ luồng client để xử lí
			try {
				DataInputStream dis = new DataInputStream(soc.getInputStream());
				String s = dis.readUTF();
				int ix = Integer.parseInt(s.split(",")[0]);
				int iy = Integer.parseInt(s.split(",")[1]);
				int nguoidanh = Integer.parseInt(s.split(",")[2]); // [18]
				
				// 4 người [0,1,2,3] , nếu khác 1 trong 4 người đầu tiên thì continue
				if (this != server.cls.get(0) && this != server.cls.get(1) && this != server.cls.get(2)
						&& this != server.cls.get(3))
					continue;

				// ai nhanh thì đánh chứ không thay nhau như bên carro nên bỏ đoạn này đi
				// if (server.dadanh.size() % 2 == 0 && this != server.cls.get(0))
				// continue;
				// if (server.dadanh.size() % 2 == 1 && this != server.cls.get(1))
				// continue;

				for (Point p : server.dadanh) {
					if (ix == p.x && iy == p.y)
						continue loop;
				}

				// nằm ngoài ma trận nxn thì continue
				if (ix < 0 || ix >= server.n)
					continue;
				if (iy < 0 || iy >= server.n)
					continue;

				// nếu không thì add điểm đó vào
//				dsthutudanh[server.dadanh.size()] = nguoidanh; // [18]
				// nếu không có lỗi thì gì add điểm đã đánh vào 
				server.dadanh.add(new Point(ix, iy));
				// add người đánh tương ứng vào // [18]
				server.dsthutudanh.add(nguoidanh);
//				System.out.print("thu tu server nhan duoc server. : "+server.dsthutudanh[server.dadanh.size()]+"\n");
				
				// và trả về chuỗi mang tọa độ điểm đó cho client vẽ ra
				for (Xuly x : server.cls) { // lặp qua tất cả các người chơi 
					try {
						DataOutputStream dos = new DataOutputStream(x.soc.getOutputStream());
						dos.writeUTF(s);
					} catch (Exception e1) {

					}
				}

			} catch (Exception e) {

			}
		}
	}
}
