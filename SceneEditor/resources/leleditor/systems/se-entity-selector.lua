function getRequiredComponents()
  return {"position", "size"}
end

local mouseClickedAtPreviousTick = false
local previousSelectedIndex = -1
function update()

  -- TODO: Handle z-index
  local noEntitiesFound = true
  for i, entity in ipairs(entities) do
    if game.input:isMouseInRectangle(entity.position.x, entity.position.y, entity.size.width, entity.size.height) then
      entity._meta:addComponent("se-hover")
      if mouseClickedAtPreviousTick and previousSelectedIndex < i then
        previousSelectedIndex = i
        mouseClickedAtPreviousTick = false
        noEntitiesFound = false
        entity._meta:addComponent("se-selected")
      end
    else
      entity._meta:deleteComponent("se-hover")
    end
  end

  if noEntitiesFound and mouseClickedAtPreviousTick then
    previousSelectedIndex = -1
  end

  if game.input:getMouseClickedX() ~= game.input:getMouseX() or game.input:getMouseClickedY() ~= game.input:getMouseY() then
    previousSelectedIndex = -1
  end

  mouseClickedAtPreviousTick = false

  if game.input:isMouseClicked() then
    mouseClickedAtPreviousTick = true
    -- Remove selected to all other entities
    for i, entity in ipairs(entities) do
      entity._meta:deleteComponent("se-selected")
    end
  end
end
