function getRequiredComponents()
	return {"position", "parallax"}
end

function updateEntities(entity)
	if entity.parallax.initialX == -1 and entity.parallax.initialY == -1 then
		entity.parallax.initialX = entity.position.x * (1- entity.parallax.cx)
		entity.parallax.initialY = entity.position.y * (1 - entity.parallax.cy)
	end
	entity.position.x = entity.parallax.initialX + game.camera:getX() * entity.parallax.cx
	entity.position.y = entity.parallax.initialY + game.camera:getY() * entity.parallax.cy
end
