function getRequiredComponents()
	return {players = {"debug", "position", "size", "acceleration"},
					squares = {"debug", "position", "size"},
					textures = {"texture", "position", "size"}}
end

function renderStart()
	game.camera:applyTransforms(g:getContext())
end

function renderTextures(e)
	g:drawImage(e.texture.name, e.position, e.size)
end

function renderSquares(entity)
	if entity.acceleration ~= nil then
		return
	end
	g:setColor(game.color:fromRGBA(entity.debug.r, entity.debug.g, entity.debug.b, entity.debug.a))
	g:fillRect(entity.position, entity.size)
end

function renderPlayers(e)
	g:setColor(game.color:fromRGBA(e.debug.r, e.debug.g, e.debug.b, e.debug.a))
	g:fillRect(e.position, e.size)
	g:setColor(255, 0, 0)
	if e.acceleration.touchWallDown then
		g:fillRect(e.position.x, e.position.y + 0.9 * e.size.height, e.size.width, e.size.height * 0.1)
	end
	if e.acceleration.touchWallUp then
		g:fillRect(e.position.x, e.position.y, e.size.width, e.size.height * 0.1)
	end
	if e.acceleration.touchWallLeft then
		g:fillRect(e.position.x, e.position.y, e.size.width * 0.1, e.size.height)
	end
	if e.acceleration.touchWallRight then
		g:fillRect(e.position.x + 0.9 * e.size.width, e.position.y, e.size.width * 0.1, e.size.height)
	end
end

function renderEnd()
	game.camera:resetTransforms(g:getContext())
	g:setColor(game.color:fromRGB(0, 0, 0))
	g:drawString("Editing mode", 20, 20)
	g:drawString("FPS:" .. game.core:getFPS(), 20, 40)
end
