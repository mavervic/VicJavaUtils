package charsetUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>eclipse workspace未正確設定utf-8時，使用此工具可無痛轉換</p>
 * 
 * @author Vic
 *
 */
public class Big5ToUtf8 {
	
	// 要讀哪個資料夾底下的資料
	static final String rootIutputPath = "C:\\Users\\Vic\\Documents\\workspace-spring-tool-suite-4-4.7.0.RELEASE\\Team4_MVC\\src";
	// 要將轉換完畢的資料輸出到哪個資料夾底下
	static final String rootOutputPath = "C:\\Users\\Vic\\Desktop\\utf8files";

	public static void main(String[] args) throws Exception {
		File scanDir = new File(rootIutputPath);
		recursiveDir(scanDir);
	}

	// 顯示子目錄及文件名稱
	public static void recursiveDir(File scanDir) {
		if (scanDir == null || !scanDir.exists()) {
			throw new RuntimeException("讀取資料夾失敗，路徑可能不存在");
		}

		if (scanDir.isDirectory()) {
			File[] s = scanDir.listFiles();
			for (File file : s) {
				if (file.getName().indexOf(".java") >= 0) {
					charsetConverter(file);
				}
				recursiveDir(file);
			}
		}
	}

	public static void charsetConverter(File file) {
		String absolutePath = file.getParentFile().getAbsolutePath();
		String outputPath = rootOutputPath
				+ absolutePath.substring(absolutePath.indexOf("\\src"), absolutePath.length());
		
		if(!StringUtils.endsWith(outputPath, "\\")) {
			outputPath = outputPath + "\\";
		}
		
		System.out.println("outputPath = " + outputPath);

		if (!new File(outputPath).exists()) {
			new File(outputPath).mkdirs();
		}

		Charset big5 = Charset.forName("big5");
		Charset utf8 = Charset.forName("utf-8");

		try (FileInputStream fin = new FileInputStream(file);
				FileOutputStream fout = new FileOutputStream(new File(outputPath + file.getName()));) {

			FileChannel fcin = fin.getChannel();
			FileChannel fcout = fout.getChannel();

			MappedByteBuffer mapped = fcin.map(MapMode.READ_ONLY, 0, fin.available());
			fcout.write(utf8.encode(big5.decode(mapped)));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
