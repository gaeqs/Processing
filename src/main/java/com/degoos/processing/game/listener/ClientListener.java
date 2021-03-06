package com.degoos.processing.game.listener;

import com.degoos.processing.engine.event.Listener;
import com.degoos.processing.engine.event.keyboard.KeyPressEvent;
import com.degoos.processing.engine.event.keyboard.KeyReleaseEvent;
import com.degoos.processing.game.Game;
import com.degoos.processing.game.controller.PlayerController;
import com.degoos.processing.game.entity.Entity;
import com.degoos.processing.game.entity.LivingEntity;
import com.degoos.processing.game.entity.Player;
import com.degoos.processing.game.event.packet.ClientPacketReceiveEvent;
import com.degoos.processing.game.network.packet.in.PacketInPressKey;
import com.degoos.processing.game.network.packet.in.PacketInReleaseKey;
import com.degoos.processing.game.network.packet.out.PacketOutEntityDelete;
import com.degoos.processing.game.network.packet.out.PacketOutEntityMove;
import com.degoos.processing.game.network.packet.out.PacketOutLivingEntityHealthChange;
import com.degoos.processing.game.network.packet.out.PacketOutLivingPlayerDeath;
import com.degoos.processing.game.network.packet.out.PacketOutOwnClientData;
import com.degoos.processing.game.network.packet.out.PacketOutPlayerChangeAnimation;

public class ClientListener {

	@Listener
	public void onPacketReceive(ClientPacketReceiveEvent event) {
		System.out.println("NEW PACKET " + event.getPacket());
		if (event.getPacket() instanceof PacketOutOwnClientData) {
			Game.setPlayer(new Player(((PacketOutOwnClientData) event.getPacket()).getEntityId(), ((PacketOutOwnClientData) event.getPacket())
				.getPosition(), new PlayerController(), ((PacketOutOwnClientData) event.getPacket()).getNick(), false, true));
			Game.setLoading(false);
		}

		if (event.getPacket() instanceof PacketOutEntityMove) {
			if (((PacketOutEntityMove) event.getPacket()).getEntityId() == Game.getPlayer().getEntityId()) {
				if (Game.getPlayer().getPosition().distance(((PacketOutEntityMove) event.getPacket()).getPosition()) > 1)
					Game.getPlayer().setPosition(((PacketOutEntityMove) event.getPacket()).getPosition());
				return;
			}
			Game.getEntityManager().getEntity(((PacketOutEntityMove) event.getPacket()).getEntityId())
				.ifPresent(entity -> entity.setPosition(((PacketOutEntityMove) event.getPacket()).getPosition()));
		}

		if (event.getPacket() instanceof PacketOutPlayerChangeAnimation) {
			Game.getEntityManager().getEntity(((PacketOutPlayerChangeAnimation) event.getPacket()).getEntityId()).ifPresent(entity -> {
				if (!(entity instanceof Player)) return;
				((Player) entity).setWalking(((PacketOutPlayerChangeAnimation) event.getPacket()).isWalking());
				((Player) entity).setDirection(((PacketOutPlayerChangeAnimation) event.getPacket()).getFacingDirection());
			});
		}
		if (event.getPacket() instanceof PacketOutEntityDelete)
			Game.getEntityManager().getEntity(((PacketOutEntityDelete) event.getPacket()).getEntityId()).ifPresent(Entity::delete);

		if (event.getPacket() instanceof PacketOutLivingEntityHealthChange) {
			PacketOutLivingEntityHealthChange packet = (PacketOutLivingEntityHealthChange) event.getPacket();
			Game.getEntityManager().getEntity(packet.getEntityId()).filter(entity -> entity instanceof LivingEntity)
				.ifPresent(entity -> ((LivingEntity) entity).setHealth(packet.getNewHealth()));
		}

		if (event.getPacket() instanceof PacketOutLivingPlayerDeath) {
			PacketOutLivingPlayerDeath packet = (PacketOutLivingPlayerDeath) event.getPacket();
			Game.getEntityManager().getEntity(packet.getEntityId()).filter(entity -> entity instanceof Player).ifPresent(entity -> {
				((Player) entity).setDead(packet.isDead());
				((Player) entity).setLives(packet.getLives());
			});
		}
	}

	@Listener
	public void onKeyPress(KeyPressEvent event) {
		Game.getServerConnection().sendPacket(new PacketInPressKey(event.getKeyCode()));
	}


	@Listener
	public void onKeyPress(KeyReleaseEvent event) {
		Game.getServerConnection().sendPacket(new PacketInReleaseKey(event.getKeyCode()));
	}

}
