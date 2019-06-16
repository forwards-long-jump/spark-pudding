function getRequiredComponents()
  return {"enemySpawner", "position"}
end

function update()
  for i, entity in ipairs(entities) do
    -- Change scene when splace is pressed
    if math.random() < entity.enemySpawner.spawnChance then
      enemy = game.core:createEntity("enemy")

      enemy.position.y = entity.position.y + entity.enemySpawner.spawnHeight * math.random()
      enemy.position.x = entity.position.x

      enemy.speed.dx = -5 + -5 * math.random()
    end
  end
end