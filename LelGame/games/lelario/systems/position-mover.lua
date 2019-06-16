function getRequiredComponents()
	return {movers = {"position", "size", "positionMover"}, entities = {"position", "size", "acceleration", "speed"}}
end

function update()
	for i, mover in ipairs(movers) do
		for j, entity in ipairs(entities) do
		if entity.position.x + entity.size.width > mover.position.x and 
				entity.position.x < mover.position.x + mover.size.width and
				entity.position.y + entity.size.height > mover.position.y and 
				entity.position.y < mover.position.y + mover.size.height
			then
				entity.position.x = entity.position.x + mover.positionMover.fx
				entity.position.y = entity.position.y + mover.positionMover.fy
				entity.speed.y = mover.positionMover.fy
			end
		end
	end
end

