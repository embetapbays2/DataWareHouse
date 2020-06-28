package P2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Export {
    public static void importData() throws IOException, ClassNotFoundException, SQLException {
    	Class.forName("com.mysql.jdbc.Driver");
		Connection connectionControl = DriverManager.getConnection("jdbc:mysql://localhost:3306/warehouse", "root", "");
		Statement statementControl = connectionControl.createStatement();
		ResultSet resultSet = statementControl.executeQuery("SELECT* FROM control");
		while (resultSet.next()) {
			String source = resultSet.getString(2);
			String destination = resultSet.getString(3);
			String username = resultSet.getString(4);
			String password = resultSet.getString(5);

			Connection connection = DriverManager.getConnection(destination, username, password);
			FileInputStream fileInputStream = new FileInputStream(source);
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			rowIterator.next();

			String sql = "INSERT INTO students (mssv, ho, ten, dOB, malop, tenlop, sdt, email, quequan, ghichu) VALUES(?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);

			// loadFile method...
			while (rowIterator.hasNext()) {   // Ko dung loop WHILE, dung loadFile
				Row nextRow = rowIterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();

				while (cellIterator.hasNext()) {
					Cell nextCell = cellIterator.next();

					int columnIndex = nextCell.getColumnIndex();

					switch (columnIndex) {
					case 1:
						int mssv = (int) nextCell.getNumericCellValue();
						ps.setInt(1, mssv);
						break;
					case 2:
						String ho = nextCell.getStringCellValue();
						ps.setString(2, ho);
						break;
					case 3:
						String ten = nextCell.getStringCellValue();
						ps.setString(3, ten);
						break;
					case 4:
						Date dOB = (Date) nextCell.getDateCellValue();
						ps.setString(4, getString(dOB));
						break;
					case 5:
						String malop = nextCell.getStringCellValue();
						ps.setString(5, malop);
						break;
					case 6:
						String tenLop = nextCell.getStringCellValue();
						ps.setString(6, tenLop);
						break;
					case 7:
						int sdt = (int) nextCell.getNumericCellValue();
						ps.setInt(7, sdt);
						break;
					case 8:
						String email = nextCell.getStringCellValue();
						ps.setString(8, email);
						break;
					case 9:
						String queQuan = nextCell.getStringCellValue();
						ps.setString(9, queQuan);
						break;
					case 10:
						String ghiChu = nextCell.getStringCellValue();
						ps.setString(10, ghiChu);
						break;
					}

				}
				ps.execute();
			}
			
			
			workbook.close();
			fileInputStream.close();
			ps.close();
			connection.close();
		}
	}

	public static String getString(Date d) {
		return new SimpleDateFormat("yyyy-MM-dd").format(d);
	}
	public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
		importData();
    }
}
