/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.util.properties;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fpt.qa.mdnlib.util.filesystem.DirFileUtil;
import fpt.qa.mdnlib.util.string.StrUtil;

/**
 *
 * @author hieupx
 */
public class Path {
    private static String rootDir = null;
    
    static {
        rootDir = DirFileUtil.normalizeDir(findRootDir());
    }
    
    private static String findRootDir() {
        String res;
        
        URL root = new Path().getClass().getProtectionDomain().getCodeSource().getLocation();
        
        String rootStr = root.toString();
        List<String> tokens = StrUtil.tokenizeStr(rootStr, "/");        

        int idx = tokens.size() - 1;
        while (idx >= 0) {
            String token = tokens.get(idx).toLowerCase();
            
            if (token.endsWith("jar")) {
                idx--;
                continue;
            }
            
            if (token.startsWith("mdnlib")) {
                break;
            }
            idx--;
        }
        
        List pathTokens = new ArrayList();
        for (int i = 0; i <= idx; i++) {
            String token = tokens.get(i);
            
            if (token.startsWith("file:") || 
                    token.startsWith("http:") || 
                    token.startsWith("ftp:")) {
                continue;
            }
            
            pathTokens.add(token);
        }
        
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") >= 0) {
            res = StrUtil.join(pathTokens, File.separator);
        } else {
            res = File.separator + StrUtil.join(pathTokens, File.separator);
        }
        
        return res;
    }
    
    public static String getRootDir() {
        return rootDir;
    }
    
    public static String getPropertiesFile() {
        return rootDir + File.separator + "mdnlib.properties";
    }
}
