import DB.CurrentSemesterParser;
import enums.Code1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public enum IOHandler {
    INSTANCE;

    public static final Path csvFilePath = Paths.get("/돈낸놈.csv");

    public void write(Path path, byte[] bytes) throws IOException {
        File file = path.toFile();
        if(!file.exists())
            path.getParent().toFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.close();
    }

    public static Path getFilePath(Code1.FileType type, String id) throws SQLException, ClassNotFoundException {
        int nowSemester = CurrentSemesterParser.getCurrentSemester();
        return Paths.get(type.name(), String.valueOf(nowSemester), id + type.extension);
    }
    
    public boolean delete(Path path) throws IOException
    {
    	File file = path.toFile();
        if(file.exists())
        {
        	if(file.delete())
        	{
        		//file delete successful
        		return true;
        	}
        	else
        	{
        		//file delete fail
        		return false;
        	}
        }
        else
        {
        	//no file
        	throw new IOException("파일이 존재하지 않음");
        }
    }
}
