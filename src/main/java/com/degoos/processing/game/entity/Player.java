package com.degoos.processing.game.entity;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.enums.EnumTextAlign;
import com.degoos.processing.engine.enums.EnumTextHeight;
import com.degoos.processing.engine.object.Animation;
import com.degoos.processing.engine.object.Image;
import com.degoos.processing.engine.object.Shape;
import com.degoos.processing.engine.object.Text;
import com.degoos.processing.engine.util.Validate;
import com.degoos.processing.game.Game;
import com.degoos.processing.game.controller.Controller;
import com.degoos.processing.game.entity.special.PlayerShadowSpecial;
import com.degoos.processing.game.enums.EnumCollideAction;
import com.degoos.processing.game.enums.EnumFacingDirection;
import com.degoos.processing.game.network.packet.Packet;
import com.degoos.processing.game.network.packet.out.PacketOutLivingPlayerDeath;
import com.degoos.processing.game.network.packet.out.PacketOutPlayerChangeAnimation;
import com.degoos.processing.game.object.Area;
import com.degoos.processing.game.util.GameCoordinatesUtils;
import com.flowpowered.math.vector.Vector2d;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Player extends LivingEntity {

	private EnumFacingDirection direction;
	private static Map<EnumFacingDirection, Image> standAnimations, walkingAnimations, enemyStandAnimations, enemyWalkingAnimations;
	private String nick;
	private Text nametag;
	private Shape nametagBackground;
	private boolean walking, enemy, dead, localPlayer;
	private int lives;
	private PlayerShadowSpecial shadowSpecial;

	static {
		standAnimations = new HashMap<>();
		enemyStandAnimations = new HashMap<>();
		walkingAnimations = new HashMap<>();
		enemyWalkingAnimations = new HashMap<>();
		standAnimations.put(EnumFacingDirection.DOWN, new Animation("riolu/stand/down", "png"));
		standAnimations.put(EnumFacingDirection.UP, new Animation("riolu/stand/up", "png"));
		standAnimations.put(EnumFacingDirection.RIGHT, new Animation("riolu/stand/right", "png"));
		standAnimations.put(EnumFacingDirection.LEFT, new Animation("riolu/stand/left", "png"));
		standAnimations.put(EnumFacingDirection.DOWN_LEFT, standAnimations.get(EnumFacingDirection.DOWN));
		standAnimations.put(EnumFacingDirection.DOWN_RIGHT, standAnimations.get(EnumFacingDirection.DOWN));
		standAnimations.put(EnumFacingDirection.UP_LEFT, standAnimations.get(EnumFacingDirection.UP));
		standAnimations.put(EnumFacingDirection.UP_RIGHT, standAnimations.get(EnumFacingDirection.UP));
		enemyStandAnimations.put(EnumFacingDirection.DOWN, new Animation("enemyriolu/stand/down", "png"));
		enemyStandAnimations.put(EnumFacingDirection.UP, new Animation("enemyriolu/stand/up", "png"));
		enemyStandAnimations.put(EnumFacingDirection.RIGHT, new Animation("enemyriolu/stand/right", "png"));
		enemyStandAnimations.put(EnumFacingDirection.LEFT, new Animation("enemyriolu/stand/left", "png"));
		enemyStandAnimations.put(EnumFacingDirection.DOWN_LEFT, enemyStandAnimations.get(EnumFacingDirection.DOWN));
		enemyStandAnimations.put(EnumFacingDirection.DOWN_RIGHT, enemyStandAnimations.get(EnumFacingDirection.DOWN));
		enemyStandAnimations.put(EnumFacingDirection.UP_LEFT, enemyStandAnimations.get(EnumFacingDirection.UP));
		enemyStandAnimations.put(EnumFacingDirection.UP_RIGHT, enemyStandAnimations.get(EnumFacingDirection.UP));
		walkingAnimations.put(EnumFacingDirection.DOWN, new Animation("riolu/walk/down", "png"));
		walkingAnimations.put(EnumFacingDirection.UP, new Animation("riolu/walk/up", "png"));
		walkingAnimations.put(EnumFacingDirection.RIGHT, new Animation("riolu/walk/right", "png"));
		walkingAnimations.put(EnumFacingDirection.LEFT, new Animation("riolu/walk/left", "png"));
		walkingAnimations.put(EnumFacingDirection.DOWN_LEFT, walkingAnimations.get(EnumFacingDirection.DOWN));
		walkingAnimations.put(EnumFacingDirection.DOWN_RIGHT, walkingAnimations.get(EnumFacingDirection.DOWN));
		walkingAnimations.put(EnumFacingDirection.UP_LEFT, walkingAnimations.get(EnumFacingDirection.UP));
		walkingAnimations.put(EnumFacingDirection.UP_RIGHT, walkingAnimations.get(EnumFacingDirection.UP));
		enemyWalkingAnimations.put(EnumFacingDirection.DOWN, new Animation("enemyriolu/walk/down", "png"));
		enemyWalkingAnimations.put(EnumFacingDirection.UP, new Animation("enemyriolu/walk/up", "png"));
		enemyWalkingAnimations.put(EnumFacingDirection.RIGHT, new Animation("enemyriolu/walk/right", "png"));
		enemyWalkingAnimations.put(EnumFacingDirection.LEFT, new Animation("enemyriolu/walk/left", "png"));
		enemyWalkingAnimations.put(EnumFacingDirection.DOWN_LEFT, enemyWalkingAnimations.get(EnumFacingDirection.DOWN));
		enemyWalkingAnimations.put(EnumFacingDirection.DOWN_RIGHT, enemyWalkingAnimations.get(EnumFacingDirection.DOWN));
		enemyWalkingAnimations.put(EnumFacingDirection.UP_LEFT, enemyWalkingAnimations.get(EnumFacingDirection.UP));
		enemyWalkingAnimations.put(EnumFacingDirection.UP_RIGHT, enemyWalkingAnimations.get(EnumFacingDirection.UP));
	}

	public Player(Vector2d position, Controller controller, String nick, boolean enemy, boolean localPlayer) {
		this(-1, position, controller, nick, enemy, localPlayer);
	}

	public Player(int id, Vector2d position, Controller controller, String nick, boolean enemy, boolean localPlayer) {
		this(id, position, controller, nick, enemy, false, localPlayer);
	}

	public Player(int id, Vector2d position, Controller controller, String nick, boolean enemy, boolean dead, boolean localPlayer) {
		super(id, position, new Area(new Vector2d(-0.6, 0), new Vector2d(0.6, 0.5)), new Area(new Vector2d(-0.7, 0), new Vector2d(0.7, 1.3)), true, 0.004D, true,
			controller, 100, 100);
		Validate.notNull(nick, "Nick cannot be null!");
		this.nick = nick;
		this.enemy = enemy;
		this.dead = dead;
		this.localPlayer = localPlayer;
		this.lives = 4;
		loadInstance();
	}

	public Player(DataInputStream inputStream) throws IOException {
		super(inputStream);
		nick = inputStream.readUTF();
		dead = inputStream.readBoolean();
		lives = inputStream.readInt();
		enemy = true;
		localPlayer = false;
		loadInstance();
	}

	public Player(DataInputStream inputStream, Controller controller) throws IOException {
		super(inputStream, controller);
		nick = inputStream.readUTF();
		dead = inputStream.readBoolean();
		lives = inputStream.readInt();
		enemy = true;
		localPlayer = false;
		loadInstance();
	}

	private void loadInstance() {
		setTickPriority(0);

		this.walking = false;
		setDead(dead);
		this.direction = EnumFacingDirection.DOWN;
		this.shadowSpecial = new PlayerShadowSpecial(this);

		nametag = new Text(true, Integer.MAX_VALUE - 1, 0, nick, GameCoordinatesUtils.toEngineCoordinates(getPosition().add(0, 1.3)), Color.WHITE, 30, Game
			.getFont(), EnumTextAlign.CENTER, EnumTextHeight.CENTER);

		Vector2d size = Game.getFont().getStringSize(nick, 30).div(2);

		nametagBackground = new Shape(true, Integer.MAX_VALUE - 2, 0, GameCoordinatesUtils.toEngineCoordinates(getPosition().add(0, 1.3)), Arrays
			.asList(new Vector2d(size.getX(), -size.getY() - 0.003), new Vector2d(-size.getX(), -size.getY() - 0.003), new Vector2d(-size.getX(),
				size.getY() - 0.003), new Vector2d(size.getX(), size.getY() - 0.003)));
		nametagBackground.setFillColor(Color.GRAY.darker());
		nametagBackground.setFullOpacity(0.5F);
		setTexture(getAnimation(EnumFacingDirection.DOWN));
	}

	public String getNick() {
		return nick;
	}

	public EnumFacingDirection getDirection() {
		return direction;
	}

	public Player setDirection(EnumFacingDirection direction) {
		Validate.notNull(direction, "Direction cannot be null!");
		this.direction = direction;
		refreshAnimation();

		if (Game.isServer()) {
			Packet packet = new PacketOutPlayerChangeAnimation(getEntityId(), direction, walking);
			Game.getGameServer().getServerClients().stream().filter(client -> !client.getPlayer().equals(this)).forEach(client -> client.sendPacket(packet));
		}
		return this;
	}

	public Player setDirection(EnumFacingDirection direction, boolean toggleWalking) {
		Validate.notNull(direction, "Direction cannot be null!");
		if (this.direction == direction && !toggleWalking) return this;
		return setDirection(direction);
	}

	public Map<EnumFacingDirection, Image> getStandAnimations() {
		return standAnimations;
	}

	public Map<EnumFacingDirection, Image> getWalkingAnimations() {
		return walkingAnimations;
	}

	public void refreshAnimation() {
		Image image = getAnimation(direction);
		if (image.equals(getTexture())) return;
		if (image instanceof Animation) ((Animation) image).reset();
		setTexture(image);
	}

	public Image getAnimation(EnumFacingDirection direction) {
		if (walking) {
			if (enemy) {
				if (enemyWalkingAnimations.containsKey(direction)) return enemyWalkingAnimations.get(direction);
			} else if (walkingAnimations.containsKey(direction)) return walkingAnimations.get(direction);
		} else {
			if (enemy) {
				if (enemyStandAnimations.containsKey(direction)) return enemyStandAnimations.get(direction);
			} else if (standAnimations.containsKey(direction)) return standAnimations.get(direction);
		}
		return getTexture();
	}

	public boolean isWalking() {
		return walking;
	}

	public void setWalking(boolean walking) {
		this.walking = walking;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
		sendDeathPacket();
		if (localPlayer || (Game.getPlayer() != null && Game.getPlayer().isDead())) setFullOpacity(dead ? 0.5F : 1);
		else setVisible(dead);
		if (localPlayer) Game.getEntityManager().getEntities().stream().filter(entity -> entity instanceof Player && !entity.equals(this))
			.forEach(entity -> ((Player) entity).refreshDeathStatus());
	}

	public void refreshDeathStatus() {
		setDead(dead);
	}

	public boolean isLocalPlayer() {
		return localPlayer;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = Math.max(0, lives);
		sendDeathPacket();
	}

	public void sendDeathPacket() {
		if (Game.isServer()) Game.getGameServer().sendPacket(new PacketOutLivingPlayerDeath(getEntityId(), dead, lives));
	}

	public void shootAuraSphere(boolean up, boolean down, boolean left, boolean right) {
		if (left && right) left = right = false;
		if (up && down) up = down = false;
		if (!(up || down || left || right)) return;
		Vector2d position = getPosition().add(0, 0.3);
		EnumFacingDirection shootDirection = EnumFacingDirection.getFacingDirection(up, down, left, right);
		Vector2d direction = shootDirection.getNormalVector().mul(shootDirection.isDiagonal() ? Math.cos(45) : 1);
		AuraSphere auraSphere = new AuraSphere(position.add(!shootDirection.isDiagonal() ? direction.mul(0.5) : direction), null, 1, direction, getEntityId());
		auraSphere.sendSpawnPacket();
		Engine.getSoundManager().play("sound/laser.mp3", 0);
	}

	public PlayerShadowSpecial getShadowSpecial() {
		return shadowSpecial;
	}

	public void activateShadowSpecial() {
		shadowSpecial.execute();
	}


	@Override
	public void setHealth(double health) {
		if (shadowSpecial.isActive() && getHealth() > health) return;
		if (health <= 0) {
			super.setHealth(getMaxHealth());
			setLives(lives - 1);
			if (lives <= 0) setDead(true);
			else teleportToSpawn();
		} else super.setHealth(health);
	}

	@Override
	public void triggerMove(boolean up, boolean down, boolean left, boolean right, long dif) {
		if (!canMove()) {
			if (walking) {
				walking = false;
				refreshAnimation();
			}
			return;
		}
		double vel = getVelocity() * dif;
		if (left && right) left = right = false;
		if (up && down) up = down = false;
		boolean wasWalking = walking;
		walking = down || up || left || right;
		if (wasWalking && !walking) setDirection(getDirection());
		if (!(up || down || left || right)) return;

		EnumFacingDirection facingDirection = EnumFacingDirection.getFacingDirection(up, down, left, right);
		Vector2d velocity = facingDirection.getNormalVector().mul(vel);
		move(velocity);
		setDirection(facingDirection, wasWalking != walking);
	}

	@Override
	public double getVelocity() {
		return shadowSpecial.isActive() ? super.getVelocity() * 3 : super.getVelocity();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (nametagBackground != null) nametagBackground.setVisible(visible);
		if (nametag != null) nametag.setVisible(visible);
	}

	@Override
	public EnumCollideAction collide(Entity entity) {
		if (entity instanceof Player) return EnumCollideAction.PASS_THROUGH;
		return super.collide(entity);
	}

	@Override
	public void onTick(long dif) {
		super.onTick(dif);
		if (nametag != null && nametagBackground != null) {
			nametag.setPosition(GameCoordinatesUtils.toEngineCoordinates(getPosition().add(0, 1.3)));
			nametagBackground.setOrigin(nametag.getPosition());
		}
		shadowSpecial.onTick(dif);
	}


	@Override
	public void draw(Processing core) {
		super.draw(core);
		if (shadowSpecial.getActiveCooldown() > 0) {
			core.strokeWeight(10);
			core.stroke(Color.RED.getRGB());
			core.line(100, 50, 100 + shadowSpecial.getActiveCooldown() / 5, 50);
		}
	}

	@Override
	public void save(DataOutputStream stream) throws IOException {
		super.save(stream);
		stream.writeUTF(nick);
		stream.writeBoolean(dead);
		stream.writeInt(lives);
	}

	@Override
	public void delete() {
		super.delete();
		nametag.delete();
	}
}
