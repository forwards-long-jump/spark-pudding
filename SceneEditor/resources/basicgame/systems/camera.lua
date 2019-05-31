function getRequiredComponents()
	return {focusedEntities = {"camera", "position", "size"}}
end

function update()
	for i, entity in ipairs(focusedEntities) do
		if entity.camera.enabled then
			game.camera:centerTargetAt(entity.position.x, entity.position.y, entity.size.width, entity.size.height)
			game.camera:setTargetScaling(entity.camera.scaling)
			game.camera:setMode(entity.camera.mode)
		end
	end
end
