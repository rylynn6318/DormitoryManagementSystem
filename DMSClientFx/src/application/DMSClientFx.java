package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class DMSClientFx extends Application 
{
	@Override
	public void start(Stage primaryStage) 
	{
		try 
		{
			//기본으로 LoginPage로 세팅
			Parent root = FXMLLoader.load(getClass().getResource("/page/LoginPage.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("DMS Client Fx");
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}
