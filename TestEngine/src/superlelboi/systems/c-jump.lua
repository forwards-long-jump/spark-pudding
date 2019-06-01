function getRequiredComponents()
	return {"jumpController", "jump", "acceleration", "speed"}
end

function update()
	for i, entity in ipairs(entities) do
		local jump = entity.jump
		local possibleNewJump = false
		
		 if entity.acceleration.touchWallDown then
			--jump.airDuration = 0
		end
		if entity.acceleration.touchWallDown or entity.acceleration.touchWallLeft or entity.acceleration.touchWallRight then
			jump.timeSinceJumpable = -1
			jump.count = 0
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
				end
				if jump.count == 0 then
					if jump.timeSinceJumpable > -1 and jump.timeSinceJumpable < jump.leniencyDuration then
						-- lenient jump
						jump.timeSinceJumpable = jump.leniencyDuration
						jump.count = -1
					elseif jump.timeSinceJumpable > jump.leniencyDuration then
						jump.count = 1
					end
				end
				
				jump.count = jump.count + 1
				jump.airDuration = jump.airDurationDefault

				if entity.deformation ~= nil then
					-- jumping makes you lose weight
					entity.deformation.horizontal = entity.deformation.jumpingDeformation
					entity.deformation.vertical = - entity.deformation.jumpingDeformation
				end
			end
			
			jump.airDuration = jump.airDuration - 1
		else
			jump.released = true
			jump.airDuration = 0
		end

		
		jump.timeSinceJumpable = jump.timeSinceJumpable + 1
	end
end
