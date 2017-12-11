package com.degoos.processing.game.entity;

import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.util.Validate;
import com.degoos.processing.game.enums.EnumCollideAction;
import com.degoos.processing.game.listener.SetupListener;
import com.degoos.processing.game.object.Area;
import com.degoos.processing.game.object.Level;
import com.degoos.processing.game.util.StreamUtils;
import com.flowpowered.math.vector.Vector2d;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Teleport extends SavableEntity {

	private Vector2d destiny;


	public Teleport(Vector2d position, Area relativeCollisionBox, Vector2d destiny, Level level) {
		super(position, relativeCollisionBox, new Area(new Vector2d(), new Vector2d()), true, 0, null, level);
		this.destiny = destiny;
	}

	public Teleport(int id, Vector2d position, Area relativeCollisionBox, Vector2d destiny, Level level) {
		super(id, position, relativeCollisionBox, new Area(new Vector2d(), new Vector2d()), true, 0, null, level);
		this.destiny = destiny;
	}

	public Teleport(Area box, Vector2d destiny, Level level) {
		super(box.getMax()
			.sub(box.getMax().sub(box.getMin()).div(2)), new Area(new Vector2d(), new Vector2d()), new Area(new Vector2d(), new Vector2d()), true, 0, null, level);
		setRelativeCollisionBox(new Area(box.getMin().sub(getPosition()), box.getMax().sub(getPosition())));
		this.destiny = destiny;
	}

	public Teleport(int id, Area box, Vector2d destiny, Level level) {
		super(id, box.getMax()
			.sub(box.getMax().sub(box.getMin()).div(2)), new Area(new Vector2d(), new Vector2d()), new Area(new Vector2d(), new Vector2d()), true, 0, null, level);
		setRelativeCollisionBox(new Area(box.getMin().sub(getPosition()), box.getMax().sub(getPosition())));
		this.destiny = destiny;
	}

	public Teleport(DataInputStream inputStream, Level level) throws IOException {
		super(inputStream, level);
		this.destiny = new Vector2d(inputStream.readDouble(), inputStream.readDouble());
	}

	public Teleport(DataInputStream inputStream) throws IOException {
		super(inputStream);
		this.destiny = new Vector2d(inputStream.readDouble(), inputStream.readDouble());
	}

	public Vector2d getDestiny() {
		return destiny;
	}

	public void setDestiny(Vector2d destiny) {
		Validate.notNull(destiny, "Destiny cannot be null!");
		this.destiny = destiny;
	}

	@Override
	public void save(DataOutputStream stream) throws IOException {
		super.save(stream);
		StreamUtils.writeVector(stream, destiny);
	}

	@Override
	public EnumCollideAction collide(Entity entity) {
		if (!(entity instanceof Player) || !SetupListener.setup) {
			entity.setPosition(destiny);
			return EnumCollideAction.CANCEL;
		}
		return super.collide(entity);
	}

	@Override
	public void draw(Processing core) {
		super.draw(core);
	}
}
