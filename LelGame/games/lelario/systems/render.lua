function getRequiredComponents()
	return {debug = {"debug", "position", "size"},
					squares = {"color", "position", "size"},
					textures = {"texture", "position", "size"},
					tiledTextures = {"tiledTexture", "position", "size"},
					animatedTiles = {"position", "size", "animatedTile"},
					animatedSprites = {"position", "size", "animatedSprite"}}
end

function renderStart()
	game.camera:applyTransforms(g:getContext())
end

function renderTextures(e)
	if game.camera:isInView(e.position.x, e.position.y, e.size.width, e.size.height) then
		g:drawImage(e.texture.name, e.position, e.size)
	end
end

function renderAnimatedTiles(e)
	local col = e.animatedTile.col
	local row = e.animatedTile.row
	local size = e.animatedTile.size

	col = col + e.animatedTile.currentFrameIndex

	if game.camera:isInView(e.position.x, e.position.y, e.size.width, col * size) then
		g:drawImage(e.animatedTile.file, e.position, e.size, 
					col * size,
					row * size, size, size)
	end
end

function renderAnimatedSprites(e)
	local col = e.animatedSprite.currentFrameIndex
	local width = e.animatedSprite.width
	local height = e.animatedSprite.height
	--[[
		width = -width
	end--]]
	if e.animatedSprite.mirrorX then
		g:drawImage(e.animatedSprite.file, e.position.x + e.size.width, e.position.y, -e.size.width, e.size.height, 
					col * math.abs(width),
					0, width, height)
	else
		g:drawImage(e.animatedSprite.file, e.position.x, e.position.y, e.size.width, e.size.height, 
					col * math.abs(width),
					0, width, height)
	end
end

function renderTiledTextures(e)
	local spacing = e.tiledTexture.spacing

	if not e.tiledTexture["auto-tile"] then
			if game.camera:isInView(e.position.x, e.position.y, e.size.width, e.size.height) then
				g:drawImage(e.tiledTexture.file, e.position, e.size, 
					e.tiledTexture.x * (spacing + e.tiledTexture["size-tile"]),
					e.tiledTexture.y * (spacing + e.tiledTexture["size-tile"]), e.tiledTexture["size-tile"], e.tiledTexture["size-tile"])
			end
	else
		local ti = math.floor(e.size.width / e.tiledTexture["size-target"]) - 1
		local tj =	math.floor(e.size.height / e.tiledTexture["size-target"]) - 1
		if game.camera:isInView(e.position.x, e.position.y, e.size.width, e.size.height) then
			-- Render x y
			for i = 0, ti do
				local x = e.position.x + i * e.tiledTexture["size-target"]
				if game.camera:isInView(x, e.position.y, e.tiledTexture["size-target"], e.size.height) then
				for j = 0, tj do
					local col = e.tiledTexture.x
					local row = e.tiledTexture.y
					local y = e.position.y + j * e.tiledTexture["size-target"]
					if e.tiledTexture["has-borders"] then
						if i == 0 and j == 0 then
							col = col - 1
							row = row - 1
						elseif i == 0 then
							col = col - 1
						elseif j == 0 then
							row = row - 1
						end
					end
					if game.camera:isInView(x, y, e.tiledTexture["size-target"], e.tiledTexture["size-target"]) then
						g:drawImage(e.tiledTexture.file, x, y, 
							e.tiledTexture["size-target"], e.tiledTexture["size-target"],
							1 + col * (spacing + e.tiledTexture["size-tile"]),
							1 + row * (spacing + e.tiledTexture["size-tile"]), 
							e.tiledTexture["size-tile"], e.tiledTexture["size-tile"])
						end
				end
			end
		end

		-- Last row
		for i = 0, tj do
			local targetSize = e.tiledTexture["size-target"] - (-e.size.width + e.tiledTexture["size-target"] * (ti + 2))
			local x = e.position.x + (ti + 1) * e.tiledTexture["size-target"]
			local y = e.position.y + i * e.tiledTexture["size-target"]
			local col = e.tiledTexture.x
			local row = e.tiledTexture.y
			if e.tiledTexture["has-borders"] then
				col = col + 1
				if i == 0 then
					row = row - 1
				end
			end
			
		if game.camera:isInView(x, y, targetSize, e.tiledTexture["size-target"]) then
			g:drawImage(e.tiledTexture.file, 
				x,
				y, 
				targetSize, 
				e.tiledTexture["size-target"],
				1 + col * (spacing + e.tiledTexture["size-tile"]),
				1 + row * (spacing + e.tiledTexture["size-tile"]), 
				e.tiledTexture["size-tile"], 
				e.tiledTexture["size-tile"])
			end	
		end

		-- Last col
		for i = 0, ti do
			local targetSize = e.tiledTexture["size-target"] - (-e.size.height + e.tiledTexture["size-target"] * (tj + 2))
			local x = e.position.x + i * e.tiledTexture["size-target"]
			local y = e.position.y + (tj + 1) * e.tiledTexture["size-target"]
			local col = e.tiledTexture.x
			local row = e.tiledTexture.y
			if e.tiledTexture["has-borders"] then
				row = row + 1
				if i == 0 then
					col = col - 1
				end
			end
			
			if game.camera:isInView(x, y, e.tiledTexture["size-target"], targetSize) then
				g:drawImage(e.tiledTexture.file, 
					x,
					y, 
					e.tiledTexture["size-target"],
					targetSize,
					1 + col * (spacing + e.tiledTexture["size-tile"]),
					1 + row * (spacing + e.tiledTexture["size-tile"]), 
					e.tiledTexture["size-tile"], 
					e.tiledTexture["size-tile"] * (targetSize / e.tiledTexture["size-target"]) + 1)
			end
		end

		-- Last tile
		local targetSizeY = e.tiledTexture["size-target"] - (-e.size.height + e.tiledTexture["size-target"] * (tj + 2))
		local targetSizeX = e.tiledTexture["size-target"] - (-e.size.width + e.tiledTexture["size-target"] * (ti + 2))
		local col = e.tiledTexture.x
		local row = e.tiledTexture.y
		
		if e.tiledTexture["has-borders"] then
			col = col + 1
			row = row + 1
		end
		
		g:drawImage(e.tiledTexture.file, 
			e.position.x + (ti + 1) * e.tiledTexture["size-target"],
			e.position.y + (tj + 1) * e.tiledTexture["size-target"], 
			targetSizeX,
			targetSizeY,
			1 + col * (spacing + e.tiledTexture["size-tile"]),
			1 + row * (spacing + e.tiledTexture["size-tile"]),	
			e.tiledTexture["size-tile"] * (targetSizeX / e.tiledTexture["size-target"]) + 1, 
			e.tiledTexture["size-tile"] * (targetSizeY / e.tiledTexture["size-target"]) + 1)
		end
	end
end

function renderSquares(entity)
	g:setColor(entity.color.r, entity.color.g, entity.color.b, entity.color.a)
	g:fillRect(entity.position, entity.size)
end


function renderEnd()
	--[[for i, entity in ipairs(debug) do
		g:setColor(entity.debug.r, entity.debug.g, entity.debug.b, 200)
		g:fillRect(entity.position, entity.size)
	end---]]
	game.camera:resetTransforms(g:getContext())
	g:setColor(game.color:fromRGB(0, 0, 0))
	g:drawString("FPS:" .. game.core:getFPS(), 20, 40)
end
