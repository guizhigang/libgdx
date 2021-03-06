package de.swagner.paxbritannica.fighter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.swagner.paxbritannica.GameInstance;
import de.swagner.paxbritannica.Resources;
import de.swagner.paxbritannica.Ship;

public class Fighter extends Ship {

	private float shotCooldownTime = 6f;
	private float shotCapacity = 5f;
	private float shotReloadRate = 1f;

	private float shots = shotCapacity;
	private float cooldown = 0;
	float delta;

	public FighterAI ai = new FighterAI(this);

	public Fighter(int id, Vector2 position, Vector2 facing) {
		super(id, position, facing);

		turnSpeed = 120f;
		accel = 120.0f;
		hitPoints = 40;
		
		switch (id) {
		case 1:
			this.set(Resources.getInstance().fighterP1);
			break;
		case 2:
			this.set(Resources.getInstance().fighterP2);
			break;
		case 3:
			this.set(Resources.getInstance().fighterP3);
			break;
		default:
			this.set(Resources.getInstance().fighterP4);
			break;
		}
		this.setOrigin(this.getWidth()/2, this.getHeight()/2);
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime());
		
		ai.update();

		cooldown = Math.max(0, cooldown - delta*50f);
		shots = Math.min(shots + (shotReloadRate * Gdx.graphics.getDeltaTime()), shotCapacity);

		super.draw(spriteBatch);
	}

	public boolean isEmpty() {
		return shots < 1;
	}

	public boolean isReloaded() {
		return shots == shotCapacity;
	}

	public boolean isCooledDown() {
		return cooldown == 0;
	}

	public boolean isReadyToShoot() {
		return isCooledDown() && !isEmpty();
	}

	public void shoot() {
		if (cooldown == 0 && shots >= 1) {
			shots -= 1;
			cooldown = shotCooldownTime;

			GameInstance.getInstance().bullets.add(new Laser(id,collisionCenter, facing));
		}
	}

}
