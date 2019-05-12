function getRequiredComponents()
  return {"position", "size"}
end

local mouseClickedAtPreviousTick = false
function update()

  -- TODO: Handle z-index
  for i, entity in ipairs(entities) do
    if game.input:isMouseInRectangle(entity.position.x, entity.position.y, entity.size.width, entity.size.height) then
      entity._meta:addComponent("se-hover")
      if mouseClickedAtPreviousTick then
        mouseClickedAtPreviousTick = false
        entity._meta:addComponent("se-selected")
      end
    else
      entity._meta:deleteComponent("se-hover")
    end
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
