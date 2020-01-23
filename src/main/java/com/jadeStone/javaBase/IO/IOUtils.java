package com.jadeStone.javaBase.IO;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {
    /**
     * 获取当前输入流的编码格式
     * 可处理无bom头的场景
     * 应用场景：本人再导入csv时遇到场景，无法识别导入文件的编码格式，导致读取到的文件乱码
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static String getCharset(InputStream inputStream) throws Exception {
        BufferedInputStream bin;
        int bom = 0;
        String str = " ";
        String str2 = "";
        try {
            bin = new BufferedInputStream(inputStream);
            bom = (bin.read() << 8);
            bom = bom + bin.read();
            // 获取两个字节内容，如果文件无BOM信息，则通过判断字的字节长度区分编码格式
            byte bs[] = new byte[10];
            while (str.matches("\\s+\\w*")) {
                bin.read(bs);
                str = new String(bs, "UTF-8");
            }
            str2 = new String(bs, "GBK");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String code = null;
        // 有BOM
        switch (bom) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                // 无BOM
                if (str.length() <= str2.length()) {
                    code = "UTF-8";
                } else {
                    code = "GBK";
                }
        }
        return code;
    }
}
