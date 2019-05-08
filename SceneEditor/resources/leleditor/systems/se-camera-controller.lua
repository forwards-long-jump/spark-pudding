function getRequiredComponents()
  return {"position"}
end

local scaling = 1 -- Do not do stuff like that in game systems
function update()
  local speed = 35
  local dx = 0
  local dy = 0
  
  --[[
  for i = 0, 999 do
    if game.input:isKeyDown(i) then
      print(i)
    end
  end
  --]]
  -- print(#entities)
  if game.input:isKeyDown(38) then
    dy = -speed
  end
  if game.input:isKeyDown(40) then
    dy = speed
  end
  if game.input:isKeyDown(37) then
    dx = -speed
  end
  if game.input:isKeyDown(39) then
    dx = speed
  end

  game.camera:setTargetPosition(game.camera:getX() + dx, game.camera:getY() + dy)

  if game.input:isKeyDown(107) then
    scaling = scaling * 1.1
  end

  if game.input:isKeyDown(109) then
    scaling = scaling / 1.1
  end
  
  game.camera:setTargetScaling(scaling)
end

