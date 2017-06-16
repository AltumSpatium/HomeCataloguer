package asm.homecataloguer.core;

import asm.homecataloguer.annotations.CatalogFileType;
import asm.homecataloguer.annotations.CreateView;
import asm.homecataloguer.models.CatalogItem;
import asm.homecataloguer.models.ContentType;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;

import java.io.ByteArrayInputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

@CatalogFileType(contentType = ContentType.AUDIO)
public class AudioFile extends CatalogFile
{
	private Thread thread;
	private Player player = null;

	public AudioFile(CatalogItem catalogItem)
	{
		super(catalogItem);
	}

	public void play()
	{
		if (thread != null) return;
		
		thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					player = new Player(new ByteArrayInputStream(getData()));
					player.play();
				} catch (JavaLayerException e)
				{
					e.printStackTrace();
					player = null;
				}	
			}
		});
		thread.start();
	}

	public void pause()
	{
	}

	public void stop()
	{
		if (thread == null) return;
		
		thread.stop();
		thread = null;
	}

	@Override
	@CreateView(className = "AudioFile")
	public void show(Object layout)
	{
		AudioFile self = this;

		AnchorPane audioLayout = new AnchorPane();
		audioLayout.setMinWidth(200);
		audioLayout.setMaxWidth(200);
		AnchorPane.setLeftAnchor(audioLayout, 0.0);
		AnchorPane.setRightAnchor(audioLayout, 0.0);
		AnchorPane.setBottomAnchor(audioLayout, 50.0);

		Button btnPlay = new Button();
		btnPlay.setText("Play");
/*		btnPlay.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				self.play();
			}
		});*/
		btnPlay.setAlignment(Pos.BOTTOM_LEFT);
		AnchorPane.setLeftAnchor(btnPlay, 250.0);

		Button btnStop = new Button();
		btnStop.setText("Stop");
		btnStop.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				self.stop();
			}
		});
		btnStop.setAlignment(Pos.BOTTOM_RIGHT);
		AnchorPane.setRightAnchor(btnStop, 250.0);

		audioLayout.getChildren().add(btnPlay);
		audioLayout.getChildren().add(btnStop);
		((AnchorPane) layout).getChildren().add(audioLayout);
	}
}
