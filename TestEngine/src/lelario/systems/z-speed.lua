function getRequiredComponents()
		return {entities = {"speed", "position"}, acceleratedEntities = {"speed", "position", "acceleration"}}
end

function updateEntities(entity)
		entity.position.x = entity.position.x + entity.speed.x
		entity.position.y = entity.position.y + entity.speed.y
end

function updateAcceleratedEntities(entity)
		if entity.speed.y ~= 0 then
			entity.acceleration.touchWallUp = false
			entity.acceleration.touchWallDown = false
		end
		
		if entity.speed.x ~= 0 then
			entity.acceleration.touchWallLeft = false
			entity.acceleration.touchWallRight = false
		end
end
