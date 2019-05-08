function getRequiredComponents()
  return {directions = {"direction", "acceleration-controller"},
    accelerators = {"acceleration-controller", "acceleration"},
    shooters = {"shoot-controller", "shooter", "direction", "size", "speed"}}
end

function update()
  for i, entity in ipairs(accelerators) do
    accelerationController = entity["acceleration-controller"]
    entity.acceleration.movingTop = game.input:isKeyDown(accelerationController.KEY_TOP)
    entity.acceleration.movingBottom = game.input:isKeyDown(accelerationController.KEY_BOTTOM)
    entity.acceleration.movingLeft = game.input:isKeyDown(accelerationController.KEY_LEFT)
    entity.acceleration.movingRight = game.input:isKeyDown(accelerationController.KEY_RIGHT)
  end

  for i, entity in ipairs(directions) do
    accelerationController = entity["acceleration-controller"]
    if game.input:isKeyDown(accelerationController.KEY_TOP) then
      entity.direction.current = "TOP"
    elseif game.input:isKeyDown(accelerationController.KEY_BOTTOM) then
      entity.direction.current = "BOTTOM"
    elseif game.input:isKeyDown(accelerationController.KEY_LEFT) then
      entity.direction.current = "LEFT"
    elseif game.input:isKeyDown(accelerationController.KEY_RIGHT) then
      entity.direction.current = "RIGHT"
    end
  end

  for i, entity in ipairs(shooters) do
    shootController = entity["shoot-controller"]
    entity.shooter.currentShootingDelay = entity.shooter.currentShootingDelay - 1
    
    if game.input:isKeyDown(shootController.KEY_SHOOT) then
      if entity.shooter.currentShootingDelay < 0 then
      entity.shooter.currentShootingDelay = entity.shooter.shootingDelay
      
        bullet = game.core:createEntity("bullet")
        bullet.position.x = entity.position.x + entity.size.width / 2 - bullet.size.width / 2
        bullet.position.y = entity.position.y + entity.size.height / 2 - bullet.size.height / 2

        if entity.direction.current == "TOP" then
          bullet.speed.dy = -entity.shooter.projectileSpeed + entity.speed.dy
          bullet.speed.dx = entity.speed.dx
        elseif entity.direction.current == "BOTTOM" then
          bullet.speed.dy = entity.shooter.projectileSpeed + entity.speed.dy
          bullet.speed.dx = entity.speed.dx
        elseif entity.direction.current == "LEFT" then
          bullet.speed.dx = -entity.shooter.projectileSpeed + entity.speed.dx
          bullet.speed.dy = entity.speed.dy
        elseif entity.direction.current == "RIGHT" then
          bullet.speed.dx = entity.shooter.projectileSpeed + entity.speed.dx
          bullet.speed.dy = entity.speed.dy
        end
      end
    end
  end
end
