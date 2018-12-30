package com.tide.ailab.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tide.ailab.common.log.Logger;


/**
 * File Util
 * <ul>
 * Read or write file
 * <li>{@link #readFile(String)} read file</li>
 * <li>{@link #readFileToList(String)} read file to string list</li>
 * <li>{@link #writeFile(String, String, boolean)} write file from String</li>
 * <li>{@link #writeFile(String, String)} write file from String</li>
 * <li>{@link #writeFile(String, List, boolean)} write file from String List</li>
 * <li>{@link #writeFile(String, List)} write file from String List</li>
 * <li>{@link #writeFile(String, InputStream)} write file</li>
 * <li>{@link #writeFile(String, InputStream, boolean)} write file</li>
 * <li>{@link #writeFile(File, InputStream)} write file</li>
 * <li>{@link #writeFile(File, InputStream, boolean)} write file</li>
 * </ul>
 * <ul>
 * Operate file
 * <li>{@link #copyFile(String, String)}</li>
 * <li>{@link #getFileExtension(String)}</li>
 * <li>{@link #getFileName(String)}</li>
 * <li>{@link #getFileNameWithoutExtension(String)}</li>
 * <li>{@link #getFileSize(String)}</li>
 * <li>{@link #deleteFile(String)}</li>
 * <li>{@link #isFileExist(String)}</li>
 * <li>{@link #isFolderExist(String)}</li>
 * <li>{@link #makeFolders(String)}</li>
 * <li>{@link #makeDirs(String)}</li>
 * </ul>
 * @author User
 */
public class FileUtil {

    public static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final String ENCODING = "UTF-8";

    /**
     * 查找指定目录下指定后缀名的文件,无递归查询
     * @param dirPath
     * @return
     */
    public static List<String> getFiles(String dirPath) throws Exception {
        List<String> fileNames = new ArrayList<String>();
        File directory = new File(dirPath);
        if (!directory.exists() || directory.isFile()) {
            return fileNames;
        } else {
            File[] children = directory.listFiles();
            for (File child : children) {
                fileNames.add(child.getAbsolutePath());
            }
            return fileNames;
        }
    }

    /**
     * 将源文件复制到目标文件
     * @param srcFile
     *            源文件的完整路径
     * @param destFile
     *            目标文件的完整路径
     */
    public static void copyFile(File srcFile, File destFile) throws Exception {
        FileInputStream in = null;
        FileOutputStream out = null;
        BufferedInputStream bufferIn = null;
        BufferedOutputStream bufferOut = null;
        try {
            in = new FileInputStream(srcFile);
            File outputFolder = destFile.getParentFile();
            if (!outputFolder.exists()) {
                outputFolder.mkdir();
            }
            out = new FileOutputStream(destFile);
            bufferIn = new BufferedInputStream(in);
            bufferOut = new BufferedOutputStream(out);
            byte[] b = new byte[1024];
            int i = 0;
            while ((i = bufferIn.read(b)) != -1) {
                bufferOut.write(b, 0, i);
            }
            bufferOut.flush();
            bufferOut.close();
            out.close();
            bufferIn.close();
            in.close();
        } finally {
            closeStream(in);
            closeStream(out);
            closeStream(bufferIn);
            closeStream(bufferOut);
        }
    }

    /**
     * 拷贝srcPath目录下所有文件及子目录下的文件
     * @param srcPath
     * @param destPath
     */
    public static void copyFiles(String srcPath, String destPath) {
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) {
            return;
        }

        try {
            File destFile = new File(destPath);
            if (srcFile.isDirectory()) {
                if (!destFile.exists()) {
                    destFile.mkdirs();
                }
                for (File tmpFile : srcFile.listFiles()) {
                    if (tmpFile.isDirectory()) {
                        copyFiles(tmpFile.getAbsolutePath(), destPath + "/" + tmpFile.getName());
                    } else {
                        copyFile(tmpFile, new File(destPath, tmpFile.getName()));
                    }
                }
            } else {
                copyFile(srcFile, destFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭文件
     * @param file
     */
    private static void closeStream(Object stream) {
        if (stream != null) {
            try {
                if (stream instanceof InputStream) {
                    ((InputStream) stream).close();
                } else if (stream instanceof OutputStream) {
                    ((OutputStream) stream).close();
                }
            } catch (Throwable e) {
                Logger.e(e);
            }
        }
    }

    /**
     * 剪切源文件到目标文件
     * @param srcFile
     * @param destFile
     * @throws Exception
     */
    public static void cutFile(String srcFile, String destFile) throws Exception {
        copyFile(new File(srcFile), new File(destFile));
        new File(srcFile).delete();
    }

    /**
     * 得到文件的字节数组
     * @param filePath
     * @return
     * @throws Exception
     */
    public static byte[] getFileByteArray(String filePath) throws Exception {
        if (!new File(filePath).exists()) {
            throw new RuntimeException("The file is not exist in specified file path!" + filePath);
        }
        if (new File(filePath).isDirectory()) {
            throw new RuntimeException("The directory path is specified!" + filePath);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = null;
        byte[] result = new byte[0];
        try {
            in = new FileInputStream(filePath);
            byte[] b = new byte[1024];
            int i = 0;
            while ((i = in.read(b)) != -1) {
                out.write(b, 0, i);
            }
            result = out.toByteArray();
            in.close();
            out.close();
        } finally {
            closeStream(in);
            closeStream(out);
        }
        return result;
    }

    /**
     * 将字节数组写入文件
     * @param buffer
     * @param filePath
     * @throws Exception
     */
    public static void writeByteArrayToFile(byte[] buffer, String filePath, boolean append) throws Exception {
        OutputStream out = null;
        BufferedOutputStream bufferOut = null;
        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            out = new FileOutputStream(file, append);
            bufferOut = new BufferedOutputStream(out);
            bufferOut.write(buffer);
            bufferOut.flush();
            bufferOut.close();
            out.close();
        } finally {
            closeStream(bufferOut);
            closeStream(out);
        }
    }

    /**
     * 读取文件内容
     * @param fileName
     * @return
     */
    public static String readFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File(fileName);
            if (file.exists()) {
                FileInputStream input = new FileInputStream(file);
                InputStreamReader read = new InputStreamReader(input, ENCODING);
                BufferedReader buf = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = buf.readLine()) != null) {
                    sb.append(lineTxt + "\r\n");
                }
                buf.close();
                read.close();
                input.close();
            }
        } catch (Exception e) {
            Logger.e(e);
        }
        return sb.toString().trim();
    }

    /**
     * read file
     * @param filePath
     * @param charsetName The name of a supported {@link java.nio.charset.Charset <code>charset</code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file
     * @param filePath
     * @param content
     * @param append is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (StringUtil.isEmpty(content)) {
            return false;
        }
        try {
            FileOutputStream fs = new FileOutputStream(filePath, append);
            OutputStreamWriter out = new OutputStreamWriter(fs, ENCODING);
            out.write(content);
            out.flush();
            out.close();
            fs.flush();
            fs.close();
            return true;
        } catch (Throwable e) {
            throw new RuntimeException("IOException occurred. ", e);
        }
    }

    /**
     * write file
     * @param filePath
     * @param contentList
     * @param append is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if contentList is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, List<String> contentList, boolean append) {
        if (ListUtil.isEmpty(contentList)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            int i = 0;
            for (String line : contentList) {
                if (i++ > 0) {
                    fileWriter.write("\r\n");
                }
                fileWriter.write(line);
            }
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file, the string will be written to the begin of the file
     * @param filePath
     * @param content
     * @return
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }

    /**
     * write file, the string list will be written to the begin of the file
     * @param filePath
     * @param contentList
     * @return
     */
    public static boolean writeFile(String filePath, List<String> contentList) {
        return writeFile(filePath, contentList, false);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     * @param filePath
     * @param stream
     * @return
     * @see {@link #writeFile(String, InputStream, boolean)}
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    /**
     * write file
     * @param filePath the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     * @param file
     * @param stream
     * @return
     * @see {@link #writeFile(File, InputStream, boolean)}
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }

    /**
     * write file
     * @param file the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * copy file
     * @param sourceFilePath
     * @param destFilePath
     * @return
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        return writeFile(destFilePath, inputStream);
    }

    /**
     * read file to string list, a element of list is a line
     * @param filePath
     * @param charsetName The name of a supported {@link java.nio.charset.Charset <code>charset</code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static List<String> readFileToList(String filePath, String charsetName) {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList<String>();
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * get file name from path, not include suffix
     *
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     *
     * @param filePath
     * @return file name from path, not include suffix
     * @see
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi)
                : filePath.substring(filePosi + 1));
    }

    /**
     * get file name from path, include suffix
     *
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     *
     * @param filePath
     * @return file name from path, include suffix
     */
    public static String getFileName(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * get folder name from path
     *
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {

        if (StringUtil.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * get suffix of file from path
     *
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFileExtension(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }

    /**
     * Creates the directory named by the trailing filename of this file, including the complete directory path required
     * to create this directory. <br/>
     * <br/>
     * <ul>
     * <strong>Attentions:</strong>
     * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
     * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
     * </ul>
     * @param filePath
     * @return true if the necessary directories have been created or the target directory already exists, false one of
     *         the directories can not be created.
     *         <ul>
     *         <li>if {@link FileUtil#getFolderName(String)} return null, return false</li>
     *         <li>if target directory already exists, return true</li>
     *         <li>return {@link java.io.File#makeFolder}</li>
     *         </ul>
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (StringUtil.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    /**
     * @param filePath
     * @return
     * @see #makeDirs(String)
     */
    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }

    /**
     * Indicates if this file represents a file on the underlying file system.
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * Indicates if this file represents a directory on the underlying file system.
     * @param directoryPath
     * @return
     */
    public static boolean isFolderExist(String directoryPath) {
        if (StringUtil.isBlank(directoryPath)) {
            return false;
        }

        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * delete file or directory
     * <ul>
     * <li>if path is null or empty, return true</li>
     * <li>if path not exist, return true</li>
     * <li>if path exist, delete recursion. return true</li>
     * </ul>
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (StringUtil.isBlank(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    /**
     * get file size
     * <ul>
     * <li>if path is null or empty, return -1</li>
     * <li>if path exist and it is a file, return file size, else return -1</li>
     * </ul>
     * @param path
     * @return returns the length of this file in bytes. returns -1 if the file does not exist.
     */
    public static long getFileSize(String path) {
        if (StringUtil.isBlank(path)) {
            return -1;
        }

        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }

    /**
     * 刪除文件夾
     * @param folderPath
     */
    public static void deleteFolder(String folderPath) {
        try {
            // 删除完里面所有内容
            deleteFile(folderPath);
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File fp = new java.io.File(filePath);
            // 删除空文件夹
            fp.delete();
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    /**
     * 创建文件
     * @throws IOException
     */
    public static boolean creatTxtFile(File file) {
        boolean flag = false;
        try {
            if (!file.exists()) {
                file.createNewFile();
                flag = true;
            } else {
                flag = true;
            }
        } catch (IOException e) {
            flag = false;
            Logger.e(e);
        }
        return flag;
    }

    /**
     * 写文件
     * @param newStr 新内容
     * @throws IOException
     */
    public static boolean writeTxtFile(File file, String newStr) throws IOException {
        // 先读取原有文件内容，然后进行写入操作
        boolean flag = false;
        String filein = newStr + "\r\n";
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            // 文件路径
            // File file = new File(fileName);
            // 将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();

            // 保存该文件原有的内容
            while ((temp = br.readLine()) != null) {
                buf = buf.append(temp);
                // System.getProperty("line.separator")
                // 行与行之间的分隔符 相当于“\n”
                buf = buf.append(System.getProperty("line.separator"));
            }
            buf.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();
            flag = true;
        } catch (IOException e1) {
            throw e1;
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return flag;
    }

    /**
     * Save text file as UTF-8 with BOM.
     * @param file filename
     * @param data data to be written
     * @param append true, append to existing file. false, overwrite file
     */
    public static void saveFile(String file, String data, boolean append) throws IOException {
        BufferedWriter bw = null;
        OutputStreamWriter osw = null;

        File f = new File(file);
        FileOutputStream fos = new FileOutputStream(f, append);
        try {
            // write UTF8 BOM mark if file is empty
            if (f.length() < 1) {
                final byte[] bom = new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
                fos.write(bom);
            }

            osw = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(osw);
            if (data != null) {
                bw.write(data);
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            try {
                bw.close();
                fos.close();
            } catch (Exception e) {
                Logger.e("close failure.", e);
            }
        }
    }

    /**
     * Save properties as UTF-8 with BOM file.
     * @param file filename
     * @param title title row is written to start of file, or null
     * @param props properties
     */
    public static void saveProperties(String file, String title, Map<?, ?> props) throws IOException {
        BufferedWriter bw = null;
        OutputStreamWriter osw = null;

        final char[] newline = System.getProperty("line.separator").toCharArray();

        File f = new File(file);
        FileOutputStream fos = new FileOutputStream(f, false);
        try {
            // write UTF8 BOM mark
            final byte[] bom = new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
            fos.write(bom);

            osw = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(osw);

            if (title != null) {
                title = "## " + title;
                osw.write(title.toCharArray(), 0, title.length());
                osw.write(newline, 0, newline.length);
            }

            Iterator<?> iter = props.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                osw.write(key.toCharArray(), 0, key.length());
                osw.write('=');
                if (val.length() > 0) {
                    osw.write(val.toCharArray(), 0, val.length());
                }
                osw.write(newline, 0, newline.length);
            }

        } catch (IOException ex) {
            throw ex;
        } finally {
            try {
                bw.close();
                fos.close();
            } catch (Exception e) {
                Logger.e("close failure.", e);
            }
        }
    }
}
