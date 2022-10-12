package Timso;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

public class TimSoOffline extends JFrame implements MouseListener {
	public static void main(String[] args) {
		System.out.print("Run TimSo Offline");
		new TimSoOffline();
	}

	// khai báo các biến dùng chung cho cả class 
//	int n = 10; // kích thước n x n
	int n = 5;
	int s = 50; // kích thước mỗi ô
	int os = 50; // padding
	int matran[][] = new int[n][n]; // tạo ma trận lưu các ô 
	Random rand = new Random();
	List<Point> dadanh = new ArrayList<Point>(); // [2]

	public TimSoOffline() {
		this.setSize(n * s + 2 * os, n * s + 2 * os); // setSize(width,height)
		this.setTitle("Caro"); // title
		this.setDefaultCloseOperation(3); // [1]
		this.addMouseListener(this); // [3]
		
		// đánh số từ 1 đến n*n cho bảng 
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				matran[i][j] = i * n + j + 1;
//				System.out.print(matran[i][j]+" ");
			}
		}
		// random lại các giá trị trong bảng [4]
		for (int r = 0; r < n * n; r++) {
			int i1 = rand.nextInt(n);
			int j1 = rand.nextInt(n);
			int i2 = rand.nextInt(n);
			int j2 = rand.nextInt(n);
			
//			System.out.print(i1+" "+j1+" "+i2+" "+j2+"\n");a
			// lấy ngẫu nhiên ra 2 phần tử rồi hoán đổi giá trị cho nhau thông qua biến trung gian tmp 
			int tmp = matran[i1][j1];
			matran[i1][j1] = matran[i2][j2];
			matran[i2][j2] = tmp;
		}
		// random lại các giá trị trong bảng [4]

		this.setVisible(true); // [5]
	}

	public void paint(Graphics g) {
		g.setColor(Color.WHITE); // nền trắng 
		g.fillRect(0, 0, this.getWidth(), this.getHeight()); // [7]

		g.setColor(Color.YELLOW); // vẽ cho ô đó màu vàng 
		for (int i = 0; i < dadanh.size(); i++) { // i chạy từ 0 đến số lượng các điểm đã đánh - 1 
			int ix = dadanh.get(i).x; // lấy ra x của điểm đã đánh tương ứng 
			int iy = dadanh.get(i).y; // tương tự 
			int x = os + ix * s; // khoảng cách đến lề phải bằng padding os + với vị trí đánh * với kíc thước mỗi ô 
			int y = os + iy * s; // tương tự khoảng cách đến lề trên 
			g.fillRect(x, y, s, s); // [8]
		}

		// Vẽ bảng bằng các đường nối các điểm 
		g.setColor(Color.BLACK); // màu của đường kẻ và cả màu nền của chữ 
		for (int i = 0; i <= n; i++) { // kẻ n+1 đường dọc và ngang 
			g.drawLine(os, os + i * s, os + n * s, os + i * s); // [9] // kẻ ngang 
			g.drawLine(os + i * s, os, os + i * s, os + n * s); // [9] // kẻ dọc 
		}
		
		// kiểu chữ và font-weight , cỡ chữ 
		g.setFont(new Font("arial", Font.BOLD, s / 3));
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				String str = matran[i][j] + ""; // một dạng convert từ số về kiểu chữ , có thể dùng hàm convert cx đc
				// dưới đây chỉ bắt cho đến giá trị lớn nhất là 100 nên 
				if (matran[i][j] < 10) // nếu giá trị của ma trận < 10 => có 1 chữ số => cộng thêm 00 cho đủ 3 chữ số
					str = "00" + str;
				else if (matran[i][j] < 100) // ngược lại > 10 và < 100 => chỉ cộng thêm 0 là đủ 3 chữ số 
					str = "0" + str;
				
				int x = os + i * s + s / 2 - s / 4; // dịch vào cho số vào giữ ô 
				int y = os + j * s + s / 2 + s / 4 - s / 8; // dịch vào cho số vào giữ ô 
				g.drawString(str, x, y); // [10] vẽ chuỗi str tại tọa độ x , y 
			}
		}
	}

	// xử lý xự kiện khi click [6]
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (x < os || x >= os + n * s)
			return;
		if (y < os || y >= os + n * s)
			return;
		int ix = (x - os) / s;
		int iy = (y - os) / s;
		for (Point d : dadanh) {
			if (ix == d.x && iy == d.y)
				return;
		}
		if (matran[ix][iy] != dadanh.size() + 1)
			return;
		dadanh.add(new Point(ix, iy));
		this.repaint(); // cập nhật ví trị mới của mỗi điểm 
	}
	// xử lý xự kiện khi click [7]
	
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
