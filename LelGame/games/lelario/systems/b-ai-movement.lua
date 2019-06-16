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
			elseif entity.acceleration.touchWallDown then
				entity.speed.y = -10
			end
		end
	end
end
