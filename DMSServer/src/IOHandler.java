import DB.CurrentSemesterParser;
import enums.Code1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public enum IOHandler {
    INSTANCE;

    public static final Path csvFilePath = Paths.get("tmp", "돈낸놈" + Code1.FileType.CSV.extension);

    public synchronized void write(Path path, byte[] bytes) throws IOException {
        File file = path.toFile();
        if(!file.exists())
            path.getParent().toFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.close();
    }

    public Path getFilePath(Code1.FileType type, String id) throws SQLException {
        int nowSemester = CurrentSemesterParser.getCurrentSemester();
        return Paths.get(type.name(), String.valueOf(nowSemester), id + type.extension);
    }
    
    public synchronized boolean delete(Path path) throws IOException
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

    public synchronized Collection<String> readCsv(Path path) throws IOException {
        BufferedReader br = Files.newBufferedReader(path);
        ArrayList<String> result = new ArrayList<>();
        String line = "";

        while( (line = br.readLine()) != null){
            if(!line.trim().equals(""))
                result.add(line.trim());
        }

        return result;
    }
}
