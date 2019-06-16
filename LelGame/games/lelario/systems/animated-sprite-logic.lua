function getRequiredComponents()
	return {"position", "size", "animatedSprite", "acceleration"}
end

function updateEntities(e)
	e.animatedSprite.currentDelay = e.animatedSprite.currentDelay - 1
	if not e.acceleration.touchWallDown then
		e.animatedSprite.currentAnimation = "jump"
	elseif e.acceleration.movingLeft or e.acceleration.movingRight then
		e.animatedSprite.currentAnimation = "walk"
	else
		e.animatedSprite.currentAnimation = "stand"
	end

	-- Handle mirroring
	if e.acceleration.movingLeft or e.acceleration.movingRight then
		e.animatedSprite.mirrorX = e.acceleration.movingLeft
	end
	
	if e.animatedSprite.currentDelay <= 0 then
		e.animatedSprite.currentDelay = e.animatedSprite.delay
		e.animatedSprite.currentFrameIndex = e.animatedSprite.currentFrameIndex + 1
		local frameCount = 0
		local baseFrameIndex = 0
		
		if e.animatedSprite.currentAnimation == "stand" then 
			frameCount = e.animatedSprite.standFrames
			baseFrameIndex = e.animatedSprite.standIndex
		elseif e.animatedSprite.currentAnimation == "jump" then 
			frameCount = e.animatedSprite.jumpFrames
			baseFrameIndex = e.animatedSprite.jumpIndex
		elseif e.animatedSprite.currentAnimation == "walk" then 
			frameCount = e.animatedSprite.walkFrames 
			baseFrameIndex = e.animatedSprite.walkIndex
		end
		
		if e.animatedSprite.currentFrameIndex + baseFrameIndex > frameCount then
			e.animatedSprite.currentFrameIndex = baseFrameIndex
		end
	end
	
end

--[[ Uncomment to loop manually
function update()
	for i, entity in ipairs(entities) do
	end
end
---]]
