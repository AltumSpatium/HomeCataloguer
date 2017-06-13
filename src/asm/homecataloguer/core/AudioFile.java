package asm.homecataloguer.core;

import asm.homecataloguer.models.CatalogItem;
import javafx.scene.media.MediaPlayer;

import java.io.ByteArrayInputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class AudioFile extends CatalogFile
{	
	private Thread thread;
	private MediaPlayer mediaPlayer = null;
	private Player player = null;
	private boolean isPlaying = false;
	
	public AudioFile(CatalogItem catalogItem)
	{
		this.catalogItem = catalogItem;
	}
	
	@Override
	public void show()
	{
		try
		{
			player = new Player(new ByteArrayInputStream(catalogItem.getData()));
			thread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{	
						isPlaying = true;
						player.play();
					}
					catch (JavaLayerException e)
					{
						e.printStackTrace();
					}
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void play()
	{
		if (player == null || isPlaying) return;
		
		thread.start();
	}
	
	public void pause()
	{
	}
	
	public void stop()
	{
		if (player == null || !isPlaying) return;
		
		System.out.println("Stopping");
		thread.interrupt();
		isPlaying = false;
	}
}
