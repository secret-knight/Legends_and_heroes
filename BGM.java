import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.LineEvent.Type;
/**
 * static class that can start a thread and play bgm
 */
public class BGM
{
    public static synchronized void playClip(File clipFile) 
    {
        new Thread(new Runnable() 
        {
            public void run()
            {
                if(clipFile == null)
                {
                    return;
                }
                class AudioListener implements LineListener {
                    private boolean done = false;
                    @Override public synchronized void update(LineEvent event) {
                        Type eventType = event.getType();
                        if (eventType == Type.STOP || eventType == Type.CLOSE) {
                            done = true;
                            notifyAll();
                        }
                    }
                    public synchronized void waitUntilDone() throws InterruptedException {
                        while (!done) { wait(); }
                    }
                }
                AudioListener listener;
                AudioInputStream audioInputStream = null;
                try {
                    listener = new AudioListener();
                    audioInputStream = AudioSystem.getAudioInputStream(clipFile);
                    Clip clip = AudioSystem.getClip();
                    clip.addLineListener(listener);
                    clip.open(audioInputStream);
                    try {
                        clip.start();
                        listener.waitUntilDone();
                    } finally {
                        clip.close();
                    }
                } 
                catch(Exception e){}
                finally {
                    if(audioInputStream != null)
                    {
                        try
                        {
                            audioInputStream.close();
                        } catch (IOException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }                
            }
        }).start();
    }
}
