function getRequiredComponents()
	return {actors = {"position", "size"},
					reactors = {"position", "size", "reactor"}}
end

function updateActors(actor)
	if actor.accelerationController ~= nil then
		for i, reactor in ipairs(reactors) do
			if actor.position.x + actor.size.width > reactor.position.x and 
					actor.position.x < reactor.position.x + reactor.size.width and
					actor.position.y + actor.size.height > reactor.position.y and 
					actor.position.y < reactor.position.y + reactor.size.height then
				if reactor.reactor.type == "hurt" then
					game.core:resetScene()
				elseif reactor.reactor.type == "levelEnd" then
					game.core:setScene("1-1")
				end
			end
		end
	end
end

