function getRequiredComponents()
  return {"speed", "acceleration"}
end

function update()
  for i, entity in ipairs(entities) do
    -- Handle x speed
    if entity.acceleration.movingRight then
      entity.speed.x = entity.speed.x + entity.acceleration.accelerationRight
    elseif entity.acceleration.movingLeft then
      entity.speed.x = entity.speed.x - entity.acceleration.accelerationLeft
    else
      if entity.speed.x > 0 then
        entity.speed.x = entity.speed.x - entity.acceleration.decelerationRight
        if entity.speed.x < 0 then
          entity.speed.x = 0
        end
      elseif entity.speed.x < 0 then
        entity.speed.x = entity.speed.x + entity.acceleration.decelerationLeft
        if entity.speed.x > 0 then
          entity.speed.x = 0
        end
      end
    end

    -- Handle y speed
    if entity.acceleration.movingUp then
      entity.speed.y = entity.speed.y - entity.acceleration.accelerationUp
    elseif entity.acceleration.movingDown then
      entity.speed.y = entity.speed.y + entity.acceleration.accelerationDown
    else
      if entity.speed.y > 0 then
        entity.speed.y = entity.speed.y - entity.acceleration.decelerationDown
        if entity.speed.y < 0 then
          entity.speed.y = 0
        end
      elseif entity.speed.y < 0 then
        entity.speed.y = entity.speed.y + entity.acceleration.decelerationUp
        if entity.speed.y > 0 then
          entity.speed.y = 0
        end
      end
    end

    -- Handle max x speed
    if entity.speed.x > entity.acceleration.maxSpeedRight then
      entity.speed.x = entity.acceleration.maxSpeedRight
    elseif entity.speed.x < -entity.acceleration.maxSpeedLeft then
      entity.speed.x = -entity.acceleration.maxSpeedLeft
    end
    
    -- Handle max y speed
    if entity.speed.y > entity.acceleration.maxSpeedDown then
      entity.speed.y = entity.acceleration.maxSpeedDown
    elseif entity.speed.y < -entity.acceleration.maxSpeedUp then
      entity.speed.y = -entity.acceleration.maxSpeedUp
    end
  end
end
