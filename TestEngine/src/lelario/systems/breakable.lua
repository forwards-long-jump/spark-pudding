function getRequiredComponents()
	return {breakables = {"position", "size", "breakable"}, breakers = {"position", "size", "speed", "jump"}, 
				 animatedBreakables = {"position", "size", "breakable", "animatedTile"}}
end

function update()
	
	for i, breakable in ipairs(animatedBreakables) do
		for j, breaker in ipairs(breakers) do
			if breaker.speed.y < 0 then
				if breaker.position.x + breaker.size.width > breakable.position.x and 
					breaker.position.x < breakable.position.x + breakable.size.width and
					breaker.position.y + breaker.size.height > breakable.position.y and 
					breaker.position.y + breaker.speed.y < breakable.position.y + breakable.size.height
				then
					if breakable.breakable.type == "question" then
						-- Stop player
						breaker.speed.y = 1
						breaker.jump.airDuration = 0
						-- Change animation
						breakable.animatedTile.row = 4
						breakable.animatedTile.currentFrameIndex = 0
						breakable.animatedTile.frames = 0

						-- Create shroom
						shroom = game.core:createEntity("mushroom")
						shroom.position.x = breakable.position.x
						shroom.position.y = breakable.position.y - shroom.size.height
						shroom.speed.y = -5
						game.sound:play("shroom-spawn.wav")
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
				if breakable.breakable.type == "brick" and breaker.speed.y < 0 and breakable.breakable.delay > 0 then
					breakable.breakable.delay = 0
					lifetime = breakable._meta:addComponent("lifetime")
					lifetime.tick = 0
					breaker.speed.y = 1
					breaker.jump.airDuration = 0
					game.sound:play("brick.wav")
					for k= 0, 4 do
						particle = game.core:createEntity("brick-particle")
						particle.position.x = breakable.position.x + math.random() * (breakable.size.width - particle.size.width)
						particle.position.y = breakable.position.y + math.random() * (breakable.size.height - particle.size.height)
						particle.speed.x = math.random() * 10 -5
						particle.speed.y = -10
					end
				elseif breakable.breakable.type == "mushroom" and breakable.breakable.delay > 0 then
					breakable.breakable.delay = 0
					breaker.position.y = breaker.position.y - 50
					breaker.size.height = breaker.size.height + 50
					game.sound:play("power-up.wav")
					breakable._meta:delete()
				elseif breakable.breakable.type == "goomba" and breakable.breakable.delay > 0 then
					breakable.breakable.delay = 0
					breaker.position.y = breaker.position.y + 50
					breaker.size.height = breaker.size.height - 50
					game.sound:play("pipe.wav")
					breakable._meta:delete()
					break
				end
			end
		end
	end
end
