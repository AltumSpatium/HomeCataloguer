package asm.homecataloguer.core;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@CatalogFileType(contentType = ContentType.AUDIO, supportedExt = {"mp3"})
public class AudioFile extends CatalogFile
{
	private MediaPlayer player;

	public AudioFile(CatalogItem catalogItem)
	{
		super(catalogItem);
	}
	
	public File openAudio()
	{
		byte[] audioData = getData();
		File audioFile = new File("c:/ProgramData/HomeCataloguer/audio.mp3");
		
		try
		{
			OutputStream os = new FileOutputStream(audioFile);
			os.write(audioData);
			os.close();
			
			return audioFile;
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
			File video = openAudio();
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
	@CreateView(className = "AudioFile")
	public void show(Object layout)
	{
		AudioFile self = this;
		loadMedia();

		AnchorPane audioLayout = new AnchorPane();
		audioLayout.setMinWidth(200);
		audioLayout.setMaxWidth(200);
		AnchorPane.setLeftAnchor(audioLayout, 0.0);
		AnchorPane.setRightAnchor(audioLayout, 0.0);
		AnchorPane.setBottomAnchor(audioLayout, 50.0);

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

		audioLayout.getChildren().add(btnPlay);
		audioLayout.getChildren().add(btnStop);
		audioLayout.getChildren().add(btnPause);
		((AnchorPane) layout).getChildren().add(audioLayout);
	}
}
