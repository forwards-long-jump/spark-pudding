function getRequiredComponents()
	return {portals = {"position", "size", "teleporter"}, entities = {"position", "size", "speed"}}
end

function update()
	for i, portal in ipairs(portals) do
		if portal.teleporter.isEntrance then
			for j, entity in ipairs(entities) do
				if entity.position.x + entity.size.width > portal.position.x and 
						entity.position.x < portal.position.x + portal.size.width and
						entity.position.y + entity.size.height > portal.position.y and 
						entity.position.y < portal.position.y + portal.size.height 
				then
					-- find the exit portal
					for k, portal2 in ipairs(portals) do
						if portal.teleporter.id == portal2.teleporter.id and not portal2.teleporter.isEntrance then
							game.sound:play("pipe.wav")
							entity.position.x = portal2.position.x + portal2.size.width / 2 - entity.size.width / 2
							entity.position.y = portal2.position.y + portal2.size.height / 2 - entity.size.height / 2
						end
					end
				end
			end
		end
	end
end
---]]
