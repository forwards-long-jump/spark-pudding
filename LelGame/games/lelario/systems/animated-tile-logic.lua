function getRequiredComponents()
	return {"position", "size", "animatedTile"}
end

function updateEntities(e)
	local size = e.animatedTile.size

	e.animatedTile.currentDelay = e.animatedTile.currentDelay - 1
	if e.animatedTile.currentDelay <= 0 then
		e.animatedTile.currentDelay = e.animatedTile.delay
		e.animatedTile.currentFrameIndex = e.animatedTile.currentFrameIndex + 1
		if e.animatedTile.currentFrameIndex > e.animatedTile.frames then
			e.animatedTile.currentFrameIndex = 0
		end
	end
	
end

--[[ Uncomment to loop manually
function update()
	for i, entity in ipairs(entities) do
	end
end
---]]
