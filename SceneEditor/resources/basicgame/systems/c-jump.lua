function getRequiredComponents()
  return {"jumpController", "jump", "acceleration", "speed"}
end

function update()
  for i, entity in ipairs(entities) do
    local jump = entity.jump
    
    if entity.speed.y ~= 0 then
      -- failsafe for the onGroud condition
      entity.acceleration.isOnGround = false
    end
    
    if entity.acceleration.isOnGround then
      jump.count = 0
      jump.airDuration = 0
    end
    
    if game.input:isKeyDown(game.input:keyFromString(entity.jumpController.key)) then
    
      if jump.airDuration > 0 then
        -- continue current jump
        entity.speed.y = - jump.force
      elseif jump.count < jump.countMax and jump.released then
        -- start new jump
        jump.released = false
        entity.speed.y = - jump.force
        jump.count = jump.count + 1
        jump.airDuration = jump.airDurationDefault
      end
      
      jump.airDuration = jump.airDuration - 1
    else
      jump.released = true
      jump.airDuration = 0
    end
  end
end
