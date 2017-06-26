package asm.homecataloguer.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import asm.homecataloguer.annotations.CatalogFileType;
import asm.homecataloguer.annotations.CreateView;
import asm.homecataloguer.models.CatalogItem;
import asm.homecataloguer.models.ContentType;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

@CatalogFileType(contentType = ContentType.VIDEO, supportedExt = {"mp4"})
public class VideoFile extends CatalogFile
{
	private MediaPlayer player;
	
	public VideoFile(CatalogItem catalogItem)
	{
		super(catalogItem);
	}
	
	public File openVideo()
	{
		byte[] videoData = getData();
		File videoFile = new File("c:/ProgramData/HomeCataloguer/video.mp4");
		
		try
		{
			OutputStream os = new FileOutputStream(videoFile);
			os.write(videoData);
			os.close();
			
			return videoFile;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public void loadMedia()
	{
		Media media = null;
		try
		{
			File video = openVideo();
			String url = video.toURI().toURL().toString();
			media = new Media(url);
				
			player = new MediaPlayer(media);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	public void play()
	{
		player.play();
	}
	
	public void pause()
	{
		player.pause();
	}
	
	public void stop()
	{
		player.stop();
	}
	
	@Override
	@CreateView(className = "VideoFile")
	public void show(Object layout)
	{
		VideoFile self = this;

		AnchorPane videoLayout = new AnchorPane();
		AnchorPane.setLeftAnchor(videoLayout, 0.0);
		AnchorPane.setRightAnchor(videoLayout, 0.0);
		AnchorPane.setBottomAnchor(videoLayout, 70.0);
		AnchorPane.setTopAnchor(videoLayout, 70.0);

		Button btnPlay = new Button();
		btnPlay.setText("Play");
		btnPlay.setPrefSize(80, 40);
		btnPlay.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				self.play();
			}
		});
		AnchorPane.setLeftAnchor(btnPlay, 250.0);
		AnchorPane.setBottomAnchor(btnPlay, 20.0);

		Button btnPause = new Button();
		btnPause.setText("Pause");
		btnPause.setPrefSize(80, 40);
		btnPause.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				self.pause();
			}
		});
		AnchorPane.setLeftAnchor(btnPause, 455.0);
		AnchorPane.setBottomAnchor(btnPause, 20.0);
		
		Button btnStop = new Button();
		btnStop.setText("Stop");
		btnStop.setPrefSize(80, 40);
		btnStop.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				self.stop();
			}
		});
		AnchorPane.setRightAnchor(btnStop, 250.0);
		AnchorPane.setBottomAnchor(btnStop, 20.0);

		((AnchorPane) layout).getChildren().add(btnPlay);
		((AnchorPane) layout).getChildren().add(btnStop);
		((AnchorPane) layout).getChildren().add(btnPause);
		
		AnchorPane mediaLayout = new AnchorPane();
		mediaLayout.setMinWidth(400);
		mediaLayout.setMaxWidth(400);
		mediaLayout.setMinHeight(200);
		mediaLayout.setMaxHeight(200);
		AnchorPane.setLeftAnchor(mediaLayout, 50.0);
		AnchorPane.setRightAnchor(mediaLayout, 50.0);
		AnchorPane.setTopAnchor(mediaLayout, 0.0);
		AnchorPane.setBottomAnchor(mediaLayout, 0.0);
		
		loadMedia();
		MediaView mediaView = new MediaView(player);
		mediaView.fitWidthProperty().bind(mediaLayout.widthProperty());
		mediaView.fitHeightProperty().bind(mediaLayout.heightProperty());
		AnchorPane.setLeftAnchor(mediaView, 40.0);
		AnchorPane.setRightAnchor(mediaView, 40.0);
		mediaLayout.getChildren().add(mediaView);
		videoLayout.getChildren().add(mediaLayout);
		
		((AnchorPane) layout).getChildren().add(videoLayout);
	}
}
