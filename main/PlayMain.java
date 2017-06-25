
import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingWorker;

import views.MainWindow;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/*
 * 注意运行前要先配置环境变量，指向Plugins文件夹
 */
public class PlayMain {

    static MainWindow frame;

    public static void main(String[] args) {

        String path = System.getProperty("user.dir");// 使用相对路径定位工程目录

		/*
         ********************************* 判断系统类型，找到不同系统中VLC软件的位置***************************************
		 */
        if (RuntimeUtil.isWindows()) {
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), path + "\\VLC");// 放在根目录
        } else if (RuntimeUtil.isMac()) {
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "/Applications/VLC.app/contents/MacOS/lib");
        } else if (RuntimeUtil.isNix()) {
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "/home/linux/vlc/install/lib");
        }

        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

		/*
		 ******************************************** 以下用于实例化并启动窗体*******************************************
		 */

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    frame = new MainWindow();
                    frame.setVisible(true);
                    String options[] = {"--subsdec-encoding=GB18030"};// 设置字幕编码为GB18030
					/*
					 * playmedia直接播放视频,应该使用preparemedia方法
					 * frame.getMediaPlayer().playMedia(path+
					 * "\\movie\\movie.mkv",options);// 默认播放的视频文件,并指定解码类型
					 */
                    frame.getMediaPlayer().prepareMedia(path + "\\movie\\movie.mkv", options);// 默认播放的视频文件,并指定解码类型

					/*
					 **************************** 以下使用后台线程进行进度条的设定*****************************
					 */
                    new SwingWorker<String, Integer>() {

                        protected String doInBackground() throws Exception {
                            while (true) {// 不断循环
                                long total = frame.getMediaPlayer().getLength();// 获取当前视频的长度，返回一个长整型
                                long curr = frame.getMediaPlayer().getTime();// 获取当前视频播放的时间
                                float percent = (float) curr / total;// 获取当前播放百分比
                                publish((int) (percent * 100));// 设置进度需要0~100之间的数字
                                Thread.sleep(100);// 每秒钟休息十次，防止循环过快
                            }
                        }

                        // 使用process方法来接收返回值
                        protected void process(java.util.List<Integer> chunks) {
                            for (int v : chunks) {
                                frame.getProgressBar().setValue(v);
                                // 随着视频的播放不断设置进度条的进度
                            }
                        }

                        ;
                    }.execute();
                    // 调用execute方法使其运行

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });// 实例化窗体
    }

	/*
	 ********************************************* 以下用于实现各类按钮的方法****************************************
	 */

    public static void play() {
        frame.getMediaPlayer().play();
    }// 创建播放视频的方法

    public static void pause() {
        frame.getMediaPlayer().pause();
    }// 创建暂停视频的方法

    public static void stop() {
        frame.getMediaPlayer().stop();
    }// 创建关闭视频的方法

    public static void setVol(Boolean slider, int v) {
        if (slider == true) {// 当是滑块改变时，设定视频的播放音量
            frame.getMediaPlayer().setVolume(v);
        } else {// 当不是滑块改变时而准备快速设置音量时
            int curVol = frame.getMediaPlayer().getVolume();//获取当前的音量
            frame.getMediaPlayer().setVolume(curVol + v);
        }
    }

    public static void jumpTo(Boolean jump, float to) {
        if (jump == true) { // 当准备跳转时
            frame.getMediaPlayer().setTime((long) (to * frame.getMediaPlayer().getLength()));
        } else {// 当不准备跳转而准备快进快退时
            float curTime = frame.getMediaPlayer().getTime() / frame.getMediaPlayer().getLength();//获取当前的时间百分比
            long jumpTime = (long) (curTime + to) * frame.getMediaPlayer().getLength();
            frame.getMediaPlayer().setTime(jumpTime);
        }
    }// 用于跳转到某个时间点,to表示当前时间的百分比

    public static void openVideo() {
        JFileChooser chooser = new JFileChooser();// 创建文件类型选择器
        int v = chooser.showOpenDialog(null);// 根据v的值判断文件的接收情况
        if (v == JFileChooser.APPROVE_OPTION) {// 如果接收到了文件
            File file = chooser.getSelectedFile();// 用File对象接收文件
            frame.getMediaPlayer().playMedia(file.getAbsolutePath());// 用播放器播放该文件
            PlayMain.play();
        }
    }// 用于打开一个视频

    public static void openSubtitle() {
        JFileChooser chooser = new JFileChooser();// 创建文件类型选择器
        int v = chooser.showOpenDialog(null);// 根据v的值判断字幕文件的接收情况
        if (v == JFileChooser.APPROVE_OPTION) {// 如果接收到了字幕文件
            File file = chooser.getSelectedFile();// 用File对象接收字幕文件
            frame.getMediaPlayer().setSubTitleFile(file);// 用播放器设置字幕
        }
    }// 用于打开一个字幕文件

    public static void exit() {
        frame.getMediaPlayer().release();// 释放掉播放器的资源
        System.exit(0);// 退出程序
    }// 用于退出程序

}
