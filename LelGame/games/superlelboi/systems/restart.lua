function getRequiredComponents()
	return {players = {"deformation"}}
end

function update()
	if next(players) == nil then
		-- no player anmyore
		if game.input:isKeyDown("r") then
			game.core:resetScene()
		end
	end
end
