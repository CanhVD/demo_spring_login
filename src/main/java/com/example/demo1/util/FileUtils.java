package com.example.demo1.util;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    // 1. Tạo file mới
    public static boolean createFile(String path) throws IOException {
        File file = new File(path);
        return file.createNewFile();
    }

    // 2. Tạo thư mục
    public static boolean createDirectory(String path, boolean recursive) {
        File dir = new File(path);
        return recursive ? dir.mkdirs() : dir.mkdir();
    }

    // 3. Kiểm tra file tồn tại
    public static boolean exists(String path) {
        return new File(path).exists();
    }

    // 4. Đọc file trả về List<String>
    public static List<String> readFile(String path) throws IOException {
        return Files.readAllLines(Paths.get(path));
    }

    // 5. Ghi file (overwrite hoặc append)
    public static void writeFile(String path, String content, boolean append) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, append))) {
            bw.write(content);
            bw.newLine();
        }
    }

    // 6. Xóa file
    public static boolean deleteFile(String path) throws IOException {
        return Files.deleteIfExists(Paths.get(path));
    }

    // 7. Đổi tên hoặc di chuyển file
    public static void moveFile(String source, String target, boolean replace) throws IOException {
        Files.move(Paths.get(source), Paths.get(target),
                replace ? StandardCopyOption.REPLACE_EXISTING : StandardCopyOption.ATOMIC_MOVE);
    }

    // 8. Sao chép file
    public static void copyFile(String source, String target, boolean replace) throws IOException {
        Files.copy(Paths.get(source), Paths.get(target),
                replace ? StandardCopyOption.REPLACE_EXISTING : StandardCopyOption.COPY_ATTRIBUTES);
    }

    // 9. Lấy thông tin file
    public static void printFileInfo(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("File không tồn tại");
            return;
        }
        System.out.println("Tên file: " + file.getName());
        System.out.println("Đường dẫn tuyệt đối: " + file.getAbsolutePath());
        System.out.println("Kích thước: " + file.length() + " bytes");
        System.out.println("Có thể đọc? " + file.canRead());
        System.out.println("Có thể ghi? " + file.canWrite());
        System.out.println("Là thư mục? " + file.isDirectory());
    }

    // 10. Đọc file an toàn (tránh lỗi file lớn)
    public static List<String> readFileBuffered(String path) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    // =========================
    // 11. File <-> byte[]
    // =========================

    // Đọc file thành byte[]
    public static byte[] fileToBytes(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }

    // Ghi byte[] ra file
    public static void bytesToFile(byte[] data, String filePath) throws IOException {
        Files.write(Paths.get(filePath), data);
    }

    // =========================
    // 12. Object <-> byte[]
    // =========================

    // Serialize object thành byte[]
    public static byte[] objectToBytes(Object obj) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            oos.writeObject(obj);
            oos.flush();
            return bos.toByteArray();
        }
    }

    // Deserialize byte[] thành Object
    public static <T> T bytesToObject(byte[] data, Class<T> clazz)
            throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {

            Object obj = ois.readObject();
            return clazz.cast(obj);
        }
    }
}
