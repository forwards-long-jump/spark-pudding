function getRequiredComponents()
	return {accelerationEntities = {"acceleration", "accelerationAI", "speed"}}
end

function update()
	for i, entity in ipairs(accelerationEntities) do
		if entity.accelerationAI.type == "mushroom" then
			if entity.acceleration.touchWallRight then
				entity.acceleration.movingLeft = true
				entity.acceleration.movingRight = false
			elseif entity.acceleration.touchWallLeft then
				entity.acceleration.movingLeft = false
				entity.acceleration.movingRight = true
			end
		end
	end
end
