package me.davidnery.flappybird.panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

import me.davidnery.flappybird.entities.Bird;
import me.davidnery.flappybird.entities.Pipe;
import me.davidnery.flappybird.listeners.BirdClickListener;
import me.davidnery.flappybird.threads.MainThread;
import me.davidnery.flappybird.utils.AngleUtils;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private BufferedImage backgroundImage, base, birddown, birdmid, birdup,
	startMessage, pipe, gameover;

	private Clip hit, point, wing;

	private final MainThread mainThread;

	private final AngleUtils angleUtils;

	private final Bird bird;

	private final List<Pipe> pipes;
	private final List<BufferedImage> numbers;

	private final Random r;

	private int score = 0, baseX = 0;

	public GamePanel() throws IOException {
		pipes = new ArrayList<Pipe>();
		numbers = new ArrayList<BufferedImage>();

		r = new Random();

		setFocusable(true);
		setPreferredSize(new Dimension(288, 512));

		initImages();
		try {
			initAudios();
		} catch (UnsupportedAudioFileException | LineUnavailableException e) {
			e.printStackTrace();
		}

		bird = new Bird(288/2 - birdmid.getWidth()/2, 292, birdmid.getWidth(), birdmid.getHeight());

		this.mainThread = new MainThread(bird, this);

		this.angleUtils = new AngleUtils();

		addMouseListener(new BirdClickListener(bird, mainThread, this));
	}

	public void playHit() {
		hit.stop();
		hit.setFramePosition(0);
		hit.start();
	}

	public void playPoint() {
		point.stop();
		point.setFramePosition(0);
		point.start();
	}

	public void playWing() {
		wing.stop();
		wing.setFramePosition(0);
		wing.start();
	}

	public void restart() {
		bird.setX(288/2 - birdmid.getWidth()/2);
		bird.setY(292);
		bird.setDead(false);
		pipes.clear();

		score = 0;
	}

	public void updateScore() {
		this.score++;
	}

	public List<Pipe> getPipes() {
		return pipes;
	}

	private void initImages() throws IOException {
		backgroundImage = ImageIO.read(Paths.get("assets/sprites/background-day.png").toFile());
		base = ImageIO.read(Paths.get("assets/sprites/base.png").toFile());
		startMessage = ImageIO.read(Paths.get("assets/sprites/message.png").toFile());

		birddown = ImageIO.read(Paths.get("assets/sprites/yellowbird-downflap.png").toFile());
		birdmid = ImageIO.read(Paths.get("assets/sprites/yellowbird-midflap.png").toFile());
		birdup = ImageIO.read(Paths.get("assets/sprites/yellowbird-upflap.png").toFile());

		pipe = ImageIO.read(Paths.get("assets/sprites/pipe-green.png").toFile());

		gameover = ImageIO.read(Paths.get("assets/sprites/gameover.png").toFile());

		for(int i = 0; i <= 9; i++)
			numbers.add(ImageIO.read(Paths.get("assets/sprites/" + i + ".png").toFile()));
	}

	private void initAudios() throws UnsupportedAudioFileException, IOException, LineUnavailableException {		
		AudioInputStream inputStream = AudioSystem.getAudioInputStream(Paths.get("assets/audio/hit.wav").toFile());
		hit = AudioSystem.getClip();
		hit.open(inputStream);

		inputStream = AudioSystem.getAudioInputStream(Paths.get("assets/audio/point.wav").toFile());
		point = AudioSystem.getClip();
		point.open(inputStream);

		inputStream = AudioSystem.getAudioInputStream(Paths.get("assets/audio/wing.wav").toFile());
		wing = AudioSystem.getClip();
		wing.open(inputStream);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		if(-baseX >= base.getWidth()) baseX = 0;

		g2d.drawImage(backgroundImage, null, 0, 0);
		g2d.drawImage(base, null, baseX--, getHeight()-base.getHeight());
		g2d.drawImage(base, null, baseX+base.getWidth(), getHeight()-base.getHeight());

		if(!mainThread.isRunning()) {
			// draw centered start message
			g2d.drawImage(startMessage, null, getWidth()/2 - startMessage.getWidth()/2, 
					getHeight()/2 - startMessage.getHeight()/2);
		}else {
			AffineTransform t = AffineTransform.getRotateInstance(angleUtils.calcAngle(bird), 
					bird.getWidth()/2, bird.getHeight()/2);
			AffineTransformOp op = new AffineTransformOp(t, AffineTransformOp.TYPE_BILINEAR);

			drawBird(g2d, op);

			drawPipes(g2d, t, op);

			drawScore(g2d);

			if(bird.isDead()) {
				g2d.drawImage(gameover, null, getWidth()/2 - gameover.getWidth()/2, 
						getHeight()/2 - gameover.getHeight()/2);

				mainThread.stop();
			}

		}
	}

	public void addPipes() {
		// 150 > 112 = height of base
		int height = r.nextInt((300 - 150) + 1) + 150;
		Pipe p = new Pipe(getWidth(), getHeight()-height, pipe.getWidth(), height);
		pipes.add(p);

		int pipey = (p.getY()-110)-pipe.getHeight();
		pipes.add(new Pipe(getWidth(), pipey, pipe.getWidth(), pipe.getHeight()+pipey));
	}

	private void drawBird(Graphics g2d, AffineTransformOp op) {
		switch(bird.getBirdStatus()) {
		case DOWN:
			g2d.drawImage(op.filter(birddown, null), bird.getX(), bird.getY(), null);
			break;
		case MID:
			g2d.drawImage(op.filter(birdmid, null), bird.getX(), bird.getY(), null);
			break;
		case UP:
			g2d.drawImage(op.filter(birdup, null), bird.getX(), bird.getY(), null);
			break;
		}
	}

	private void drawPipes(Graphics g2d, AffineTransform t, AffineTransformOp op) {
		Pipe p;
		t = AffineTransform.getRotateInstance(Math.toRadians(180), pipe.getWidth()/2, pipe.getHeight()/2);
		op = new AffineTransformOp(t, AffineTransformOp.TYPE_BILINEAR);
		for(int i = 0; i<pipes.size(); i+=2) {
			p = pipes.get(i);
			g2d.drawImage(pipe, p.getX(), p.getY(), null);
			p = pipes.get(i+1);
			g2d.drawImage(op.filter(pipe, null), p.getX(), p.getY(), null);
		}
	}

	private void drawScore(Graphics g2d) {
		String s = String.valueOf(score);
		int pos = getWidth()/2 - (s.length()*29)/2;
		for(int i = 0; i<s.length(); i++) {
			BufferedImage bi = numbers.get(Integer.parseInt(String.valueOf(s.charAt(i))));
			g2d.drawImage(bi, pos, 50, null);
			pos += bi.getWidth();
		}
	}

	public boolean collided() {
		for(Pipe p : pipes) {
			Rectangle pipeR = new Rectangle(p.getX(), p.getY(), p.getWidth(), pipe.getHeight());
			if(pipeR.intersects(new Rectangle(bird.getX(), bird.getY(), bird.getWidth(), bird.getHeight()))) return true;
		}

		if(bird.getY()+bird.getHeight() >= getHeight()-base.getHeight()) return true;

		return false;
	}

}
