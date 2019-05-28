function getRequiredComponents()
  return {accelerationEntities = {"acceleration", "accelerationController"}}
end

function update()
  for i, entity in ipairs(accelerationEntities) do
    entity.acceleration.movingUp = game.input:isKeyDown(game.input:keyFromString(entity.accelerationController.keyTop))
    entity.acceleration.movingDown = game.input:isKeyDown(game.input:keyFromString(entity.accelerationController.keyBottom))
    entity.acceleration.movingLeft = game.input:isKeyDown(game.input:keyFromString(entity.accelerationController.keyLeft))
    entity.acceleration.movingRight = game.input:isKeyDown(game.input:keyFromString(entity.accelerationController.keyRight))
  end
end
