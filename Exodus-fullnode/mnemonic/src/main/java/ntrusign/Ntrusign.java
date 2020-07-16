package ntrusign;


import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.*;

public class Ntrusign {

    public static   Logger logger   = Logger.getLogger(Ntrusign.class);

    static String LIBFILENAME = "Project2.dll";// dll 文件


    static{
        String path = null;
//        try {
//            path = new File("").getCanonicalPath();
         path = Ntrusign.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        } catch (IOException e) {
//            logger.info("file load error ~!",e);
//        }
        //TODO 判断系统
        String os = System.getProperty("os.name").toLowerCase();
        if(os.startsWith("win")) {
            LIBFILENAME = "Project2.dll";
            try {
                if(path.startsWith("/")) {
                    path = path.substring(1);
                }
                final File dllFile = new File(path+LIBFILENAME);
                if(!dllFile.exists()) {
                    writeFile(dllFile);
                }
                System.load(path + LIBFILENAME);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(os.startsWith("linux")){
            LIBFILENAME = "libntrusign.so";
            try {
                final File dllFile = new File(path+LIBFILENAME);
                if(!dllFile.exists()) {
                    writeFile(dllFile);
                }
                System.load(path + LIBFILENAME);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void writeFile(File dllFile) throws IOException {
        InputStream is = getResource("classpath:" + LIBFILENAME);
        FileOutputStream outputStream = new FileOutputStream(dllFile);
        byte[] array = new byte[1024];
        int bytesRead = -1;
        while ((bytesRead = is.read(array)) != -1) {
            outputStream.write(array, 0, bytesRead);
        }
        outputStream.flush();
        outputStream.close();
    }

    public native byte[] InitialKey(String key);
    public native static byte[] Sign(byte[] msg,byte[] sk);
    public native static int Verify(byte[] smsg,byte[] msg,byte[] pk);
//    public native byte[]  GetPrivateKey();
//    public native byte[] GetPublicKey();
    public native static byte[] Secret2Public(byte[] sk);


    private static InputStream getResource(String location) throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        InputStream in = resolver.getResource(location).getInputStream();
        byte[] byteArray = IOUtils.toByteArray(in);
        in.close();
        return new ByteArrayInputStream(byteArray);
    }



}
