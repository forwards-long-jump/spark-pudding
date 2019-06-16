function getRequiredComponents()
	return {"position", "size", "speed", "speedForces", "jump"}
end

function updateEntities(entity)
	-- should only happen if the player dies
	if entity.position.y > 15000 then
		game.core:resetScene()
	end
end
