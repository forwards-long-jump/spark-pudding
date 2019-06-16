function getRequiredComponents()
	return {"jumpController", "jump", "acceleration", "speed"}
end

function update()
	for i, entity in ipairs(entities) do
		local jump = entity.jump
		
		if entity.acceleration.touchWallLeft or entity.acceleration.touchWallRight or entity.acceleration.touchWallDown then
			jump.count = 0
		end
		if entity.acceleration.touchWallDown then
			jump.airDuration = 0
		end
		
		if game.input:isKeyDown(game.input:keyFromString(entity.jumpController.key)) then
		
			if jump.airDuration > 0 then
				-- continue current jump
				entity.speed.y = - jump.force
			elseif jump.count < jump.countMax and jump.released then
				-- start new jump
				
				jump.released = false
				entity.speed.y = - jump.force
				
				if not entity.acceleration.touchWallDown then
					-- wall jump it is
					if entity.acceleration.touchWallLeft then
						entity.speed.x = jump.force
					elseif entity.acceleration.touchWallRight then
						entity.speed.x = - jump.force
					end
					--game.sound:play("wall-jump.wav")
				else
					--game.sound:play("jump.wav")
				end
				jump.count = jump.count + 1
				jump.airDuration = jump.airDurationDefault
			end
			
			jump.airDuration = jump.airDuration - 1
		else
			jump.released = true
		end
	end
end
