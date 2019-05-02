function getRequiredComponents()
  return {enemies = {"position", "size", "enemy", "shooter", "speed"},
    bullets = {"bullet", "position", "size"},
    players = {"position", "size", "player"}}
end

function update()
  for i, entity in ipairs(enemies) do
  -- Make enemy shoot
  --[[
    if entity.shooter.currentShootingDelay < 0 then
      bullet = game.core:createEntity("bullet")
      bullet.position.x = entity.position.x + entity.size.width
      bullet.position.y = entity.position.y + 5
      bullet.speed.dx = entity.shooter.speedX + entity.speed.dx
      entity.shooter.currentShootingDelay = entity.shooter.shootingDelay
    end
    
    entity.shooter.currentShootingDelay = entity.shooter.currentShootingDelay - 1
    --]]
  end

  -- die from bullet
  for i, bullet in ipairs(bullets) do
    for i, enemy in ipairs(enemies) do
      if (bullet.position.x < enemy.position.x + enemy.size.width and bullet.position.x + bullet.size.width > enemy.position.x) then
        if (bullet.position.y < enemy.position.y + enemy.size.height and bullet.position.y + bullet.size.height > enemy.position.y) then
          game.camera:shake(8, 10)
          bullet._meta:delete()
          enemy = enemy._meta:addComponent("gravity")
          enemy = enemy._meta:deleteComponent("enemy")
        end
      end
    end
  end

  -- kill player
  for i, player in ipairs(players) do
    for i, enemy in ipairs(enemies) do
      if (player.position.x < enemy.position.x + enemy.size.width and player.position.x + player.size.width > enemy.position.x) then
        if (player.position.y < enemy.position.y + enemy.size.height and player.position.y + player.size.height > enemy.position.y) then
          game.core:setScene("game-over")
        end
      end
    end
  end
end
    