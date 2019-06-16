function getRequiredComponents()
	return {breakables = {"position", "size", "breakable"}, breakers = {"position", "size", "speed", "jump"}, 
				 animatedBreakables = {"position", "size", "breakable", "animatedTile"}, otherCameras = {"camera"}}
end

function update()
	
	for i, breakable in ipairs(animatedBreakables) do
		if game.camera:isInView(breakable.position.x, breakable.position.y, breakable.size.width, breakable.size.height) then
		for j, breaker in ipairs(breakers) do
			if breaker.speed.y < 0 then
				if breaker.position.x + breaker.size.width > breakable.position.x and 
					breaker.position.x < breakable.position.x + breakable.size.width and
					breaker.position.y + breaker.size.height > breakable.position.y and 
					breaker.position.y + breaker.speed.y < breakable.position.y + breakable.size.height
				then
					breakable.breakable.delay = breakable.breakable.delay - 1
					if breakable.breakable.delay >= 0 then
						if breakable.breakable.type == "question" then
							-- Stop player
							breaker.speed.y = 1
							breaker.jump.airDuration = 0
							-- Change animation
							if breakable.breakable.delay <= 0 then
								breakable.animatedTile.row = 4
								breakable.animatedTile.currentFrameIndex = 0
								breakable.animatedTile.frames = 0
							end
	
							-- Create shroom
							shroom = game.core:createEntity("mushroom")
							shroom.position.x = breakable.position.x
							shroom.position.y = breakable.position.y - shroom.size.height
							shroom.speed.y = -5
							game.sound:play("shroom-spawn.wav")
						elseif breakable.breakable.type == "coin" then
							breakable.breakable.delay = breakable.breakable.delay - 1
							
							-- Stop player
							breaker.speed.y = 1
							breaker.jump.airDuration = 0
							-- Change animation
							if breakable.breakable.delay <= 0 then
								breakable.animatedTile.row = 4
								breakable.animatedTile.currentFrameIndex = 0
								breakable.animatedTile.frames = 0
							end
	
							game.sound:play("coin.wav")
	
							-- Create coin
							shroom = game.core:createEntity("coin-particle")
							shroom.position.x = breakable.position.x + breakable.size.width / 2 - shroom.size.width / 2
							shroom.position.y = breakable.position.y - shroom.size.height
							shroom.speed.y = -5
						end
					end
				end
			end
		end
		end
	end
	
	-- Bricks and other breakable stuff
	for i, breakable in ipairs(breakables) do
		for j, breaker in ipairs(breakers) do
			if breaker.position.x + breaker.size.width > breakable.position.x and 
				breaker.position.x < breakable.position.x + breakable.size.width and
				breaker.position.y + breaker.size.height > breakable.position.y and 
				breaker.position.y + breaker.speed.y < breakable.position.y + breakable.size.height
			then
				breakable.breakable.delay = breakable.breakable.delay - 1
				if breakable.breakable.type == "gb-transform" then
					breaker.animatedSprite.file = "playergb.png"
					breaker.animatedSprite.height = 32
					if breaker.size.height ~= 128 then
						breaker.position.y = breaker.position.y - 128
					end

					if breakable.breakable.delay == 0 then
						game.sound:playMusic("Circling-the-Barn.wav")
					end
					breaker.size.height = 128
				end
				if breakable.breakable.type == "mario-transform" then
					breaker.animatedSprite.file = "player.png"
					breaker.animatedSprite.height = 25
				end
				if breakable.breakable.delay >= 0 then
					if breakable.breakable.type == "brick" and breaker.speed.y < 0 then
						if breaker.size.height > 64 then
							breaker.speed.y = 1
							breaker.jump.airDuration = 0
							game.sound:play("brick.wav")
							if breakable.breakable.delay <= 0 then
								lifetime = breakable._meta:addComponent("lifetime")
								lifetime.tick = 0
								for k= 0, 4 do
									particle = game.core:createEntity("brick-particle")
									particle.position.x = breakable.position.x + math.random() * (breakable.size.width - particle.size.width)
									particle.position.y = breakable.position.y + math.random() * (breakable.size.height - particle.size.height)
									particle.speed.x = math.random() * 10 -5
									particle.speed.y = -10
								end
							end
						else
							breakable.breakable.delay = breakable.breakable.delay + 1
						end
					elseif breakable.breakable.type == "mushroom" then
						breaker.position.y = breaker.position.y - 50
						breaker.size.height = breaker.size.height + 50
						game.sound:play("power-up.wav")
						breakable._meta:delete()
					elseif breakable.breakable.type == "goomba" then
						if breaker.size.height > 64 then
							breaker.position.y = breaker.position.y + 50
							breaker.size.height = breaker.size.height - 50
							game.sound:play("pipe.wav")
						else
							breaker.accelerationController.keyLeft = false
							breaker.accelerationController.keyRight = false
							breaker.acceleration.movingLeft = false
							breaker.acceleration.movingRight = false
							breaker.speed.x = 0
							breaker.speed.noclip = true
							breaker.speed.y = -20
							breaker._meta:removeComponent("accelerationController")
							breaker._meta:removeComponent("jumpController")
							game.sound:stopMusic()
							game.sound:play("die.wav")
						end
						breakable._meta:delete()
						break
					elseif breakable.breakable.type == "door" then
							game.core:setScene("pong")
							break
					end
				end
			end
		end
	end
end
