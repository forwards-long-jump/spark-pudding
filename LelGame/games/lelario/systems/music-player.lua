function getRequiredComponents()
	return {"play-music"}
end

function update()
	for i, entity in ipairs(entities) do
			game.sound:playMusic(entity["play-music"].name)
			entity._meta:delete() -- We don't want to play a music multiple time!
	end
end

