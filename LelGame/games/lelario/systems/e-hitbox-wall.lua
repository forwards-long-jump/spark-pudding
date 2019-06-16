function getRequiredComponents()
	return {walls = {"hitboxWall", "position", "size"},
		entities = {"position", "size", "speed", "acceleration"},
		movingWalls = {"hitboxWall", "position", "size", "speed"}}
end

function update()
	for i, entity in ipairs(entities) do
		if not entity.speed.noclip and game.camera:isInView(entity.position.x, entity.position.y, entity.size.width, entity.size.height) then
			local ex1 = entity.position.x
			local ey1 = entity.position.y
			local ex2 = ex1 + entity.size.width
			local ey2 = ey1 + entity.size.height
			local esx = entity.speed.x
			local esy = entity.speed.y

			-- Handle moving walls
			for j, wall in ipairs(movingWalls) do
				if entity ~= wall then
					local x1 = wall.position.x
					local y1 = wall.position.y
					local x2 = x1 + wall.size.width
					local y2 = y1 + wall.size.height
					local sx = wall.speed.x
					local sy = wall.speed.y

					-- Does entity intersect?
					if ex2 + esx > x1 + sx and ex1 + esx < x2 + sx and ey2 + esy > y1 + sy and ey1 + esy < y2 + sy then
						-- it intersect and it's inside in the x axis
						if ex2 - esx > x1 - sx and ex1 - esx < x2 - sx then
							if ey2 - esy < y1 - sy and not wall.hitboxWall.noTopCollision then
								-- top
								entity.acceleration.touchWallDown = true
								entity.position.y = y1 - entity.size.height
								-- TODO: Additional force entity.position.x = entity.position.x + sx -- todo * (1 - wall.hitboxWall.frictionX)

								entity.speed.y = sy + (entity.speed.y * -wall.hitboxWall.bounceCoeffY)
								entity.speed.x = entity.speed.x * (1 - wall.hitboxWall.frictionX)
							elseif ey1 - esy > y2 - sy and not wall.hitboxWall.noBottomCollision	then
								-- bottom
								entity.acceleration.touchWallUp = true
								entity.position.y = y2
								-- TODO: Additional force entity.position.x = entity.position.x + sx -- todo * (1 - wall.hitboxWall.frictionX)

								entity.speed.y = sy + (entity.speed.y * -wall.hitboxWall.bounceCoeffY)
								entity.speed.x = entity.speed.x * (1 - wall.hitboxWall.frictionX)
							end
						else
							if ex1 - esx < x1 - sx and not wall.hitboxWall.noLeftCollision	then
								-- left
								entity.acceleration.touchWallRight = true
								entity.position.x = x1 - entity.size.width

								entity.speed.x = sx -- todo handle bouncy
								entity.speed.y = entity.speed.y * (1 - wall.hitboxWall.frictionY)
							elseif ex2 - esx > x2 - sx and not wall.hitboxWall.noRightCollision	then
								-- right
								entity.acceleration.touchWallLeft = true
								entity.position.x = x2

								entity.speed.x = sx -- todo handle bouncy
								entity.speed.y = entity.speed.y * (1 - wall.hitboxWall.frictionY)
							end
						end
					end
				end
			end


			-- Do the same for non-moving walls
			local canChangeSpeed = true
			for j, wall in ipairs(walls) do
				if entity ~= wall and wall.speed == nil then -- TODO: Use something else to check if the wall moves

					local x1 = wall.position.x
					local y1 = wall.position.y
					local x2 = x1 + wall.size.width
					local y2 = y1 + wall.size.height

					-- Does entity intersect?
					if ex2 + esx > x1 and ex1 + esx < x2 and ey2 + esy > y1 and ey1 + esy < y2 then
						-- it intersect and it's inside in the x axis
						if ex2 > x1 and ex1 < x2 then
							if ey2 - esy < y1 and not wall.hitboxWall.noTopCollision then 
								-- top
								entity.acceleration.touchWallDown = true
								entity.position.y = y1 - entity.size.height
								-- Fixe for colliding bouncy walls
								if canChangeSpeed then
									entity.speed.y = (entity.speed.y * -wall.hitboxWall.bounceCoeffY)
									canChangeSpeed = false
								end
								entity.speed.x = entity.speed.x * (1 - wall.hitboxWall.frictionX)
							elseif ey1 - esy > y2 and not wall.hitboxWall.noBottomCollision	then
								-- bottom
								entity.acceleration.touchWallUp = true
								entity.position.y = y2
								entity.speed.y = math.abs(entity.speed.y * -wall.hitboxWall.bounceCoeffY)
								entity.speed.x = entity.speed.x * (1 - wall.hitboxWall.frictionX)
							end
						else
							if ey2 > y1 + 2 then -- Change the "2" here to make "stairs"
								if ex1 < x1 and not wall.hitboxWall.noLeftCollision	then
									-- left
									entity.acceleration.touchWallRight = true
									entity.position.x = x1 - entity.size.width
									entity.speed.x =	(entity.speed.x * -wall.hitboxWall.bounceCoeffX)
									entity.speed.y = entity.speed.y * (1 - wall.hitboxWall.frictionY)
								elseif ex2 > x2 and not wall.hitboxWall.noRightCollision	then
									-- right
									entity.acceleration.touchWallLeft = true
									entity.position.x = x2
									entity.speed.x = math.abs(entity.speed.x * -wall.hitboxWall.bounceCoeffX)
									entity.speed.y = entity.speed.y * (1 - wall.hitboxWall.frictionY)
								end
							end
						end
					end
				end
			end
		end
	end
end
