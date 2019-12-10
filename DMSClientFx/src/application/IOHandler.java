package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

//대충 입출력하는 클래스
//showAlert같은거 여러 클래스에서 쓰여서 그냥 싱글톤으로 만듬.
public class IOHandler
{
	private static IOHandler _instance;
	private final long MAXFILESIZE = 10485760;	//10MB = 10485760 byte(in 바이너리)
	public final static String downloadDirectoryName = "다운받은파일";
	
	public static IOHandler getInstance()
	{
		if(_instance == null)
			_instance = new IOHandler();
		return _instance;
	}
	
	//경고창 띄우는 메소드
	public void showAlert(String s)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(s);
		alert.showAndWait();
	}
	
	public boolean showDialog(String header, String content)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Dialog");
		alert.setHeaderText(header);
		alert.setContentText(content);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
		{
		    return true;
		} 
		else 
		{
		    return false;
		}
	}
	
	
	//File IO
	public String selectFile(String prevDirectory, FileNameExtensionFilter filter)
    {
    	JFileChooser jfc;
    	
    	//이전에 파일을 열었다면, 그 파일이 있는 폴더에서 열 수 있게 함.
    	if(prevDirectory.equals("N/A"))
    	{
    		jfc = new JFileChooser();
    	}
    	else
    	{
    		jfc = new JFileChooser(prevDirectory);
    	}
    	
    	jfc.setFileFilter(filter);
    	
		int returnVal = jfc.showOpenDialog(null);
		if(returnVal == 0)
		{
			//파일선택 선택됨
			try
			{
				File file = jfc.getSelectedFile();
				long fileSize = file.length();
				
				if(fileSize <= MAXFILESIZE)
				{
					return file.getPath();
				}
				else
				{
					showAlert("파일의 크기가 10MB보다 큽니다.");
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			//파일선택 취소됨
			showAlert("파일 선택이 취소되었습니다.");
		}
		return null;
    }

	public void write(Path path, byte[] bytes) {
		File file = path.toFile();

		if(!file.exists())
			path.getParent().toFile().mkdirs();


		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.close();
		} catch (FileNotFoundException e) {
			IOHandler.getInstance().showAlert(file.getAbsolutePath() + "\n작성해야할 파일의 경로를 찾을 수 없습니다!");
			e.printStackTrace();
		} catch (IOException e) {
			IOHandler.getInstance().showAlert(file.getAbsolutePath() + "\n해당 경로에 파일을 작성할 수 없습니다!");
			e.printStackTrace();
		}
	}
}
