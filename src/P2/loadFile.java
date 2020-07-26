package P2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;

import loadFile.loadExcel;

public class loadFile {
	public static void loadFiles(int idFile) throws IOException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/control", "root", "");
		Statement statementControl = connection.createStatement();

		ResultSet rsLog = statementControl.executeQuery("SELECT * FROM log WHERE status= 'ER' AND id_file = " + idFile);
		if (rsLog.next()) {
			String srcFile = rsLog.getString(5);
			String typeFile = rsLog.getString(6);

			ResultSet rs = statementControl.executeQuery("SELECT* FROM config WHERE id_file = " + idFile);
			if (rs.next()) {
				String dest = rs.getString(1);
				String fields = rs.getString(3);
				String delimiter = rs.getString(5);
				if (typeFile.equals("xlsx")) {
					loadExcel.loadExcel(dest, fields, srcFile);
				} else if (typeFile.equals("txt")) {
					File file = new File(srcFile);
					if (!file.exists()) {
						System.out.println("File not found!");
						return;
					}
					String sql = "INSERT INTO students (";
					String[] arFiels = fields.split("\\,");
					for (int i = 0; i < arFiels.length; i++) {
						if (i == 0) {
							sql += arFiels[i];
						} else {
							sql += "," + arFiels[i];
						}
					}
					sql += ") VALUES (";
					for (int i = 0; i < arFiels.length - 1; i++) {
						if (i == 0) {
							sql += "?";
						} else {
							sql += ",?";
						}
					}
					sql += ",'Null')";

					PreparedStatement ps = connection.prepareStatement(sql);
					try {
						BufferedReader bReader = new BufferedReader(
								new InputStreamReader(new FileInputStream(file), "utf8"));
						String line = bReader.readLine();

						while ((line = bReader.readLine()) != null) {
							String[] arrValue = line.split(delimiter);
							for (int i = 1; i < arrValue.length; i++) {
								switch (i) {
								case 1:
									String mssv = arrValue[i];
									ps.setString(1, mssv);
									break;
								case 2:
									String ho = arrValue[i];
									ps.setString(2, ho);
									break;
								case 3:
									String ten = arrValue[i];
									ps.setString(3, ten);
									break;
								case 4:
									String dOB = arrValue[i];
									ps.setString(4, dOB);
									break;
								case 5:
									String malop = arrValue[i];
									ps.setString(5, malop);
									break;
								case 6:
									String tenlop = arrValue[i];
									ps.setString(6, tenlop);
									break;
								case 7:
									String sdt = arrValue[i];
									ps.setString(7, sdt);
									break;
								case 8:
									String email = arrValue[i];
									ps.setString(8, email);
									break;
								case 9:
									String queQuan = arrValue[i];
									ps.setString(9, queQuan);
									break;
								case 10:
									String ghiChu = arrValue[i];
									ps.setString(10, ghiChu);
									break;
								}
							}
							ps.execute();
						}
						bReader.close();
						System.out.println("Success");
					} catch (NoSuchElementException | IOException e) {
						e.printStackTrace();
						return;
					}
				}

			}
		}
		statementControl.close();
		connection.close();
	}

	public static String getString(Date d) {
		return new SimpleDateFormat("yyyy-MM-dd").format(d);
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
		loadFiles(2);
	}
}
