function getRequiredComponents()
  return {entities = {"position", "size", "player", "shooter"}, mice = {"position", "mouse"}}
end

-- NOTE: NEVER do that outside of a test, it breaks one of ECS most important principle
local scaling = 1
local following = true

function update()
  -- Change scene when space is pressed
  if(game.input:isKeyDown(32)) then
    game.core:setScene("game", true)
  end

  for i, entity in ipairs(mice) do
    entity.position.x = game.input:getMouseX()
    entity.position.y = game.input:getMouseY()
  end

  for i, entity in ipairs(entities) do

    -- Move player
    if(game.input:isKeyDown(40)) then
      entity.position.y = entity.position.y + 5
    end
    if(game.input:isKeyDown(38)) then
      entity.position.y = entity.position.y - 5
    end
    if(game.input:isKeyDown(39)) then
      entity.position.x = entity.position.x + 5
    end
    if(game.input:isKeyDown(37)) then
      entity.position.x = entity.position.x - 5
    end

    -- Remove boundary with 0
    if(game.input:isKeyDown(48)) then
      game.camera:clearBoundary()
    end

    -- Change game boundary with 9 or 8
    if(game.input:isKeyDown(57)) then
      game.camera:setBoundary(0, 0, game.core:getGameWidth(), game.core:getGameHeight())
    end

    -- Make the camera stop following with enter (use it to test zoom)
    if(game.input:isKeyDown(10)) then
      game.camera:setMode("NO_FOLLOW")
    end

    if(game.input:isKeyDown(107)) then
      game.camera:setScalingPoint(game.input:getUIMouseX(), game.input:getUIMouseY())
      scaling = scaling * 1.1
      game.camera:setTargetScaling(scaling)
    end

    if(game.input:isKeyDown(109)) then
      game.camera:setScalingPoint(game.input:getUIMouseX(), game.input:getUIMouseY())
      scaling = scaling / 1.1
      game.camera:setTargetScaling(scaling)
    end

    -- Force to stay in map
    if entity.position.y < 0 then
      entity.position.y = 0
    end

    if entity.position.x < 0 then
      entity.position.x = 0
    end

    if entity.position.x + entity.size.width > 1280 then
      entity.position.x = 1280 - entity.size.width
    end

    if entity.position.y + entity.size.height > 720 then
      entity.position.y = 720 - entity.size.height
    end

    game.camera:centerTargetAt(entity.position.x, entity.position.y, entity.size.width, entity.size.height)

    -- f = shooting
    if(game.input:isKeyDown(70)) then
      if entity.shooter.currentShootingDelay < 0 then
        bullet = game.core:createEntity("bullet")
        bullet.position.x = entity.position.x + entity.size.width
        bullet.position.y = entity.position.y + 5
        bullet.speed.dx = entity.shooter.speedX
        entity.shooter.currentShootingDelay = entity.shooter.shootingDelay
      end
    end
    entity.shooter.currentShootingDelay = entity.shooter.currentShootingDelay - 1
  end
end
