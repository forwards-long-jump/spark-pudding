function getRequiredComponents()
  return {"position"}
end

local sceneX = 0  -- Do not do stuff like that in game systems
local sceneY = 0
local firstMousePressedTick = true

function update()
  local scaling = game.camera:getScaling()
  local speed = 20
  local dx = 0
  local dy = 0
  -- TODO: Reset camera position / scaling / move to selected entity / etc...
  game.camera:setMode("NO_FOLLOW")

  -- Handle moving camera with left click
  if game.input:isMouseButtonDown(3) then
    if firstMousePressedTick then
      firstMousePressedTick = false
      sceneX = game.camera:getX() / game.camera:getScaling()
      sceneY = game.camera:getY() / game.camera:getScaling()
    end

    game.camera:setPosition(sceneX + game.input:getMouseClickedX() - game.input:getMouseX(),
      sceneY + game.input:getMouseClickedY() - game.input:getMouseY())
  else
    firstMousePressedTick = true
  end

  -- Handle zooming
  if game.input:getMouseWheelRotation() ~= 0 then
    game.camera:setSmoothScaleSpeedCoeff(0.5)
    game.camera:setScalingPoint(game.input:getUIMouseX(), game.input:getUIMouseY())
    scaling = scaling * (1 - game.input:getMouseWheelRotation() * 0.15)
    game.camera:setTargetScaling(scaling)
  end

  -- Handle moving with keys
  local anyKeyPressed = false
  if game.input:isKeyDown(38) then
    dy = -speed
    anyKeyPressed = true
  end
  if game.input:isKeyDown(40) then
    dy = speed
    anyKeyPressed = true
  end
  if game.input:isKeyDown(37) then
    dx = -speed
    anyKeyPressed = true
  end
  if game.input:isKeyDown(39) then
    dx = speed
    anyKeyPressed = true
  end

  if anyKeyPressed then
    game.camera:setSmoothSpeedCoeff(1, 1)
    game.camera:setMode("SMOOTH")
    game.camera:setTargetPosition(game.camera:getX() + dx * (game.camera:getScaling() * 1.2), game.camera:getY()
      + dy * (game.camera:getScaling() * 1.2))
  else
    game.camera:setMode("NO_FOLLOW")
  end


  --[[
  if game.input:isMouseButtonDown(2) then
    dx = game.input:getMouseDX()
    dy = game.input:getMouseDY()
  end
    game.camera:setMode("NO_FOLLOW")

  game.camera:setPosition(game.camera:getX() - dx, game.camera:getY() - dy)
--̣]]
  -- Commented out in case we still want to use buttons to zoom
  --[[
  if game.input:isKeyDown(107) then
    scaling = scaling * 1.1
  end

  if game.input:isKeyDown(109) then
    scaling = scaling / 1.1
  end
  --]]
  -- scaling = scaling * (1 - game.input:getMouseWheelRotation() * 0.3)
  --game.camera:setSmoothScaleSpeedCoeff(1)
end
