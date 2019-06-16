function getRequiredComponents()
	return {actors = {"position", "size", "deformation"},
					reactors = {"position", "size", "reactor"}}
end

function updateActors(actor)

	for i, reactor in ipairs(reactors) do
		if actor.position.x + actor.size.width > reactor.position.x and 
				actor.position.x < reactor.position.x + reactor.size.width and
				actor.position.y + actor.size.height > reactor.position.y and 
				actor.position.y < reactor.position.y + reactor.size.height then
				
			if reactor.reactor.type == "hurt" then
				game.camera:shake(30, 20)
				actor._meta:delete()
			elseif reactor.reactor.type == "levelEnd" then
				if reactor.goal ~= nil then
					game.core:resetScene()
					game.core:setScene(reactor.goal.nextLevel, true)
				end
			end
			
		end
	end

end

