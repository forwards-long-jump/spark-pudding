function getRequiredComponents()
	return {players = {"debug", "position", "size", "acceleration", "deformation"},
					squares = {"debug", "position", "size"},
					textures = {"texture", "position", "size"}}
end

function renderStart()
	game.camera:applyTransforms(g:getContext())
	local scaling = game.camera:getScaling()
	local pos = {x = game.camera:getX() / scaling, y = game.camera:getY() / scaling}
	local size = {width = game.core:getGameWidth() / scaling, height = game.core:getGameHeight() / scaling}

	g:setColor(230, 230, 230)
	g:fillRect(pos, size)
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
	local pos = {x = e.position.x + e.size.width * e.deformation.currentHorizontal,
							 y = e.position.y + 2 * e.size.height * e.deformation.currentVertical}
	local size = {width = e.size.width * (1 - 2 * (e.deformation.currentHorizontal)),
								height = e.size.height * (1 - e.deformation.currentVertical)}

	
	g:setColor(game.color:fromRGBA(e.debug.r, e.debug.g, e.debug.b, e.debug.a))
	g:fillRect(pos, size)
	g:setColor(0, 0, 125)
	if e.acceleration.touchWallDown then
		g:fillRect(pos.x, pos.y + 0.9 * size.height, size.width, size.height * 0.1)
	end
	if e.acceleration.touchWallUp then
		g:fillRect(pos.x, pos.y, size.width, size.height * 0.1)
	end
	if e.acceleration.touchWallLeft then
		g:fillRect(pos.x, pos.y, size.width * 0.1, size.height)
	end
	if e.acceleration.touchWallRight then
		g:fillRect(pos.x + 0.9 * size.width, pos.y, size.width * 0.1, size.height)
	end
end

function renderEnd()
	game.camera:resetTransforms(g:getContext())

	-- game over text
	if next(players) == nil then
		g:setColor(0,0,0)
		g:drawString("YOU DIED", 50, 50)
		g:drawString("Press r to restart", 50, 70)
	end
end
