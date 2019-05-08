function getRequiredComponents()
  return {"position"}
end

local scaling = 1 -- Do not do stuff like that in game systems
function update()
  local speed = 35
  local dx = 0
  local dy = 0
  
  
--  for i = 0, 999 do
--    if game.input:isMouseButtonDown(i) then
--      print(i)
--    end
--  end
  
  -- print(#entities)
--  if game.input:isKeyDown(38) then
--    dy = -speed
--    game.camera:setMode("SMOOTH")
--  end
--  if game.input:isKeyDown(40) then
--    dy = speed
--    game.camera:setMode("SMOOTH")
--  end
--  if game.input:isKeyDown(37) then
--    dx = -speed
--    game.camera:setMode("SMOOTH")
--  end
--  if game.input:isKeyDown(39) then
--    dx = speed
--    game.camera:setMode("SMOOTH")
--  end

  if game.input:isMouseButtonDown(2) then
    dx = game.input:getMouseDX()
    dy = game.input:getMouseDY()
  end
    game.camera:setMode("NO_FOLLOW")
  
  game.camera:setPosition(game.camera:getX() - dx, game.camera:getY() - dy)

  -- Commented out in case we still want to use buttons to zoom
  --[[
  if game.input:isKeyDown(107) then
    scaling = scaling * 1.1
  end

  if game.input:isKeyDown(109) then
    scaling = scaling / 1.1
  end
  --]]
  scaling = scaling * (1 - game.input:getMouseWheelRotation() * 0.3)
  game.camera:setSmoothScaleSpeedCoeff(1)
  game.camera:setTargetScaling(scaling)
end

