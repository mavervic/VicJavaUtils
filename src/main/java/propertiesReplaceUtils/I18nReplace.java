package propertiesReplaceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.commons.lang3.StringUtils;


/**
 * <p>多個語系檔中某個關鍵字A要全數替換成關鍵字B</p>
 * <p>因為語系檔檔案數量龐大，單個檔案本身也是很肥大</p>
 * <p>所以寫出這個工具</p>
 * 
 * @author Vic
 *
 */
public class I18nReplace {

	private static String find;
	private static String replacement;
	private static String[] excludesProperties;

	public static void main(String[] args) throws Exception {
		String srcPath = "C:\\Users\\Vic\\CBS_B2C\\PIB_RUNTIME\\Message\\b2c";
		String outputPath = "C:\\Users\\Vic\\Desktop\\test";

		find = string2Unicode("數位存款帳戶");
		replacement = string2Unicode("數位帳戶");

		excludesProperties = new String[] { "caatx001.home.digitalAcctContract", "caatx001.home.term1", "caatx001.home.term6",
				"caatx006.rule.digitalAcctContractTitle", "caatx006.rule.digitalAcctContract", };

		String[] targetFileNames = new String[] { "\\caatxConstants.properties", "\\caatxConstants_en_US.properties",
				"\\caatxConstants_zh_CN.properties", "\\caatxConstants_zh_TW.properties" };

		for (String fileName : targetFileNames) {
			replaceFile(srcPath + fileName, outputPath + fileName);
		}
	}

	public static void replaceFile(String srcPath, String outputPath) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(new File(srcPath)));
		File file = new File(outputPath);
		file.createNewFile();

		FileWriter fileWriter = new FileWriter(file);
		String oneLine = null;
		while ((oneLine = reader.readLine()) != null) {
			if (StringUtils.containsAny(oneLine, excludesProperties)) {
				System.out.println(oneLine);
				fileWriter.write(oneLine + "\r\n");
			} else {
				fileWriter.write(StringUtils.replaceIgnoreCase(oneLine, find, replacement) + "\r\n");
			}

			fileWriter.flush();
		}

		fileWriter.close();
		reader.close();
		System.out.println();
	}

	public static String string2Unicode(String string) {
		StringBuffer unicode = new StringBuffer();
		for (int i = 0; i < string.length(); i++) {
			// 取出每一個字元
			char c = string.charAt(i);
			// 轉換為uniCode
			unicode.append("\\u" + Integer.toHexString(c));
		}

		return unicode.toString();
	}

}
