package team.rpsg.htmlTest.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import team.rpsg.htmlTest.util.Timer;

/**
 * GDX-RPG 音频管理器<br>
 */
public class Sound {
	
	static SEManager defaultSEManager = new SEManager();
	static MusicManager defaultMusicManager = new MusicManager();
	
	public static SEManager defaultSEManager() {
		return defaultSEManager;
	}
	
	public static MusicManager defaultMusicManager() {
		return defaultMusicManager;
	}
	
	
	/**
	 * 音效管理器
	 * 由于音效基本不需要细化控制，所以可以统一销毁、停止
	 */
	public static class SEManager{
		List<com.badlogic.gdx.audio.Sound> manager = new ArrayList<>();
		
		/**播放一个音效*/
		public void play(String path) {
			com.badlogic.gdx.audio.Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
			manager.add(sound);
			sound.play(1);
		}
		
		/**停止所有音效*/
		public void stop() {
			manager.forEach(com.badlogic.gdx.audio.Sound::stop);
		}
		
		/**卸载所有音效*/
		public void dispose() {
			stop();
			manager.forEach(com.badlogic.gdx.audio.Sound::dispose);
			manager.clear();
		}
	}
	
	/**
	 * 音乐控制器
	 * 相比音效要麻烦一些=。=
	 */
	public static class MusicManager{
		Map<String, MusicProxy> manager = new HashMap<>();
		Timer.Task loopTask;
		
		public MusicManager() {
			//添加task到主循环，让Timer托管
			loopTask = Timer.add(Timer.TimeType.frame, 0, () -> {
				manager.values().forEach(MusicProxy::act);
			});
		}
		
		/**播放一个音乐，如果正在播的就是这首音乐，则重新播放*/
		public MusicProxy play(String path) {
			MusicProxy proxy = null;
			if(manager.containsKey(path)){
				proxy = manager.get(path);
			}else{
				proxy = new MusicProxy(Gdx.audio.newMusic(Gdx.files.internal(path)));
				manager.put(path, proxy);
			}
			
			proxy.music.play();
			
			return proxy;
		}
		
		public MusicProxy get(String path) {
			return manager.get(path);
		}
		
		public void stopAll() {
			manager.values().forEach(p -> p.music.stop());
		}
		
		public void pauseAll() {
			manager.values().forEach(p -> p.music.pause());
		}
		
		/**卸载所有音乐，清除循环*/
		public void disposeAll() {
			manager.values().forEach(p -> p.music.dispose());
			manager.clear();
			
			//取消循环托管
			Timer.remove(loopTask);
		}
		
		
		
		static class MusicProxy {
			Music music;
			Actor proxy = new Actor();
			
			public MusicProxy(Music music) {
				this.music = music;
				music.setVolume(1);
			}
			
			public void act() {
				proxy.act(Gdx.graphics.getDeltaTime());
				music.setVolume(proxy.getColor().a * 1);
			}
			
			/**淡入某个音乐*/
			public MusicProxy fadeIn(float second) {
				return fadeTo(1, second);
			}
			
			/**淡出某个音乐*/
			public MusicProxy fadeOut(float second) {
				return fadeTo(0, second);
			}
			
			/**设置某个音乐音量*/
			public MusicProxy fadeTo(float volume, float second) {
				proxy.clearActions();
				proxy.addAction(Actions.alpha(volume, second));
				return this;
			}
		}
	}
}
