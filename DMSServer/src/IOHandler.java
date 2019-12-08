import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum IOHandler {
    INSTANCE;

    public static final Path csvFilePath = Paths.get("/돈낸놈.csv");

    public void write(Path path, byte[] bytes) throws IOException {
        File file = path.toFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.close();
    }
}
