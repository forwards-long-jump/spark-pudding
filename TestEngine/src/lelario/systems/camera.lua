function getRequiredComponents()
	return {focusedEntities = {"camera", "position", "size"}}
end

function update()
	for i, entity in ipairs(focusedEntities) do
		if entity.camera.enabled then
			game.camera:centerTargetAt(math.floor(entity.position.x), math.floor(entity.position.y), math.floor(entity.size.width), math.floor(entity.size.height))
			game.camera:setTargetScaling(entity.camera.scaling)
			game.camera:setMode(entity.camera.mode)
			game.camera:setBoundary(0, -500, 999999, 1220)
		end
	end
end
