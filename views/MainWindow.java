import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.PlayMain;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JProgressBar;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

    private JPanel contentPane;
    private EmbeddedMediaPlayerComponent playerComponent; // 创建播放器界面
    private JPanel panel;
    private JButton btnPlay;
    private JButton btnPause;
    private JButton btnStop;
    private JPanel controlpanel;
    private JProgressBar progress;
    private JMenuBar menuBar;
    private JMenu mnFile;
    private JMenuItem mntmOpenVideo;
    private JMenuItem mntmOpenSubtitle;
    private JMenuItem mntmExit;
    private JSlider slider;
    private JMenu mnAbout;
    private JMenuItem mntmNote;

    /**
     * 打开程序
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainWindow frame = new MainWindow();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 实例化窗体
     */
    public MainWindow() {
        /*
         *********************************** 以下设置窗体的基本属性********************************
		 */

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1300, 772);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

		/*
		 ************************************* 以下设置菜单栏********************************
		 */
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        mnFile = new JMenu("File");
        mnFile.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
        menuBar.add(mnFile);

        mntmOpenVideo = new JMenuItem("Open Video...");
        mntmOpenVideo.setHorizontalAlignment(SwingConstants.CENTER);
        mntmOpenVideo.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
        mnFile.add(mntmOpenVideo);

        mntmOpenSubtitle = new JMenuItem("Open Subtitle...");
        mntmOpenSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        mntmOpenSubtitle.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
        mnFile.add(mntmOpenSubtitle);

        mntmExit = new JMenuItem("Exit");
        mntmExit.setHorizontalAlignment(SwingConstants.CENTER);
        mntmExit.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
        mnFile.add(mntmExit);

        mnAbout = new JMenu("About");
        mnAbout.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
        menuBar.add(mnAbout);

        mntmNote = new JMenuItem("Note");
        mntmNote.setHorizontalAlignment(SwingConstants.CENTER);
        mnAbout.add(mntmNote);

		/*
		 *********************************** 以下创建组件并加入窗体*********************************
		 */

        // 播放面板，包含播放器
        JPanel videopane = new JPanel();
        contentPane.add(videopane, BorderLayout.CENTER);
        videopane.setLayout(new BorderLayout(0, 0));

        // 播放器
        playerComponent = new EmbeddedMediaPlayerComponent();
        videopane.add(playerComponent);

        // 操作面板，包含控制面板和进度条
        panel = new JPanel();
        videopane.add(panel, BorderLayout.SOUTH);
        panel.setLayout(new BorderLayout(0, 0));

        // 控制面板，包含play,pause和stop三个控制按钮
        controlpanel = new JPanel();
        panel.add(controlpanel);

        // 停止键
        btnStop = new JButton("Stop");
        btnStop.setFont(new Font("宋体", Font.PLAIN, 20));
        controlpanel.add(btnStop);

        // 播放键
        btnPlay = new JButton("Play");
        btnPlay.setFont(new Font("宋体", Font.PLAIN, 20));
        controlpanel.add(btnPlay);

        // 暂停键
        btnPause = new JButton("Pause");
        btnPause.setFont(new Font("宋体", Font.PLAIN, 20));
        controlpanel.add(btnPause);

        // 滑块，用于控制音量
        slider = new JSlider();
        slider.setValue(100);//设置滑块当前值
        slider.setMaximum(120);//设置滑块最大值
        controlpanel.add(slider);

        // 进度条
        progress = new JProgressBar();
        progress.setStringPainted(true);// 设置进度条显示数字
        panel.add(progress, BorderLayout.NORTH);

		/*
		 ********************************* 以下是鼠标点击的事件*************************************
		 */

        // 当打开文件选项被点击时，调用主方法打开文件
        mntmOpenVideo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PlayMain.openVideo();
            }
        });

        // 当打开字幕选项被点击时，调用主方法打开字幕
        mntmOpenSubtitle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PlayMain.openSubtitle();
            }
        });

        // 当关闭选项被点击时，调用主方法关闭程序
        mntmExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PlayMain.exit();
            }
        });

        // 当pause键被点击时，调用主方法中的pause方法
        btnPause.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                PlayMain.pause();
            }
        });

        // 当play键被点击时，调用主方法中的play方法
        btnPlay.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                PlayMain.play();
            }
        });

        // 当stop键被点击时，调用主方法中的stop方法
        btnStop.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                PlayMain.stop();
            }
        });

        // 当进度条被点击时，转到视频相应百分比的位置
        progress.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();// 接收鼠标点击的位置
                PlayMain.jumpTo(true, (float) x / progress.getWidth());// 准备跳转并计算点击位置所表示的百分比，设置进度条
            }
        });

        //当滑块被移动时，设置视频的音量
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                PlayMain.setVol(true, slider.getValue());
            }
        });
    }


	/*
	 ************************** 以下用于生成相应组件的getter方法***********************************
	 */

    // MediaPlayer的get方法
    public EmbeddedMediaPlayer getMediaPlayer() {
        return playerComponent.getMediaPlayer();
    }

    // JProgressBar的get方法
    public JProgressBar getProgressBar() {
        return progress;
    }

}
