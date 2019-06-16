function getRequiredComponents()
  return {entities = {"speed", "acceleration"}, sines = {"speed", "acceleration-sine"}}
end

function update()
  for i, entity in ipairs(entities) do
    if entity.acceleration.movingRight then
      entity.speed.dx = entity.speed.dx + entity.acceleration.delta
    elseif entity.acceleration.movingLeft then
      entity.speed.dx = entity.speed.dx - entity.acceleration.delta
    elseif entity.speed.dx > 0 then
      entity.speed.dx = entity.speed.dx - entity.acceleration.friction
      if entity.speed.dx < 0 then
        entity.speed.dx = 0
      end
    elseif entity.speed.dx < 0 then
      entity.speed.dx = entity.speed.dx + entity.acceleration.friction
      if entity.speed.dx > 0 then
        entity.speed.dx = 0
      end
    end

    if entity.acceleration.movingBottom then
      entity.speed.dy = entity.speed.dy + entity.acceleration.delta
    elseif entity.acceleration.movingTop then
      entity.speed.dy = entity.speed.dy - entity.acceleration.delta
    elseif entity.speed.dy > 0 then
      entity.speed.dy = entity.speed.dy - entity.acceleration.friction
      if entity.speed.dy < 0 then
        entity.speed.dy = 0
      end
    elseif entity.speed.dy < 0 then
      entity.speed.dy = entity.speed.dy + entity.acceleration.friction
      if entity.speed.dy > 0 then
        entity.speed.dy = 0
      end
    end

    if entity.speed.dx > entity.acceleration.max then
      entity.speed.dx = entity.acceleration.max
    elseif entity.speed.dx < -entity.acceleration.max then
      entity.speed.dx = -entity.acceleration.max
    end

    if entity.speed.dy > entity.acceleration.max then
      entity.speed.dy = entity.acceleration.max
    elseif entity.speed.dy < -entity.acceleration.max then
      entity.speed.dy = -entity.acceleration.max
    end
  end

  for i, entity in ipairs(sines) do
    entity.speed.dx = math.sin(game.core:getTick() * entity["acceleration-sine"].coeffFrequence) * entity["acceleration-sine"].coeffX
    entity.speed.dy = math.sin(game.core:getTick() * entity["acceleration-sine"].coeffFrequence) * entity["acceleration-sine"].coeffY
  end
end
