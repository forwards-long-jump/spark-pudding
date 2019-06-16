function getRequiredComponents()
  return {transformDone = {"se-entity-transform-done"},
    transformStart = {"se-entity-transform-start"},
    entities = {"position", "size"},
    selectedEntities = {"position", "size", "se-selected"}}
end

-- TODO: Use components and separate selection and resize
local draggingAction = -1
local draggingStartEntitySize = {w = 0, h = 0}
local draggingStartEntityPosition = {x = 0, y = 0}
local draggingStartOffset = {x = 0, y = 0}

function applyGrid(val)
  return math.floor(val / 64) * 64
end

function update()
  local cursorSize = 10 / game.camera:getScaling()
  local draggerSelected = false

  for i, entity in ipairs(transformDone) do
    -- we only need to notify the SE for one tick
    entity._meta:removeComponent("se-entity-transform-done")
  end

  for i, entity in ipairs(transformStart) do
    -- we only need to notify the SE for one tick
    entity._meta:removeComponent("se-entity-transform-start")
  end

  -- For each selected entities
  for i, entity in ipairs(selectedEntities) do
    local cursorSize = 10 / game.camera:getScaling()

    -- Find which dragger is hovered
    entity["se-selected"].hoverResizerIndex = -1
    for i = 0, 8 do
      local x = entity.position.x - cursorSize / 2 + (i % 3) * entity.size.width / 2
      local y = entity.position.y - cursorSize / 2 + math.floor(i / 3) * entity.size.height / 2

      if game.input:isMouseInRectangle(x, y, cursorSize, cursorSize) then
        -- Mouse is over the box
        entity["se-selected"].hoverResizerIndex = i

        -- Mouse is down and a previous dragging action was not started, save start drag state
        if draggingAction == -1 and game.input:isMouseButtonDown(1) then
          entity._meta:addComponent("se-entity-transform-start")
          draggingAction = i
          draggingStartEntitySize.w = entity.size.width
          draggingStartEntitySize.h = entity.size.height
          draggingStartOffset.x = -entity.position.x + game.input:getMouseX()
          draggingStartOffset.y = -entity.position.y + game.input:getMouseY()
          draggingStartEntityPosition.x = game.input:getMouseX()
          draggingStartEntityPosition.y = game.input:getMouseY()
        end
        break
      end
    end

    -- Allowing drag and dropping the whole entity
    if game.input:isMouseInRectangle(entity.position.x, entity.position.y, entity.size.width, entity.size.height) and game.input:isMouseButtonDown(1) and draggingAction == -1 then
      draggingAction = 4
      draggingStartOffset.x = -entity.position.x + game.input:getMouseX()
      draggingStartOffset.y = -entity.position.y + game.input:getMouseY()
      entity._meta:addComponent("se-entity-transform-start")
    end

    -- Apply effects on live entity
    if draggingAction == 0 then
      -- Top left
      if game.input:isKeyDown("shift") then
        entity.size.width = applyGrid(draggingStartEntitySize.w + draggingStartEntityPosition.x - game.input:getMouseX())
        entity.size.height = applyGrid(draggingStartEntitySize.h + draggingStartEntityPosition.y - game.input:getMouseY())

        entity.position.x = applyGrid(game.input:getMouseX() - draggingStartOffset.x)
        entity.position.y = applyGrid(game.input:getMouseY() - draggingStartOffset.y)
      else
        entity.size.width = draggingStartEntitySize.w + draggingStartEntityPosition.x - game.input:getMouseX()
        entity.size.height = draggingStartEntitySize.h + draggingStartEntityPosition.y - game.input:getMouseY()

        entity.position.x = game.input:getMouseX() - draggingStartOffset.x
        entity.position.y = game.input:getMouseY() - draggingStartOffset.y
      end
    end
    if draggingAction == 1 then
      -- Middle top
      if game.input:isKeyDown("shift") then
        entity.size.height = applyGrid(draggingStartEntitySize.h + draggingStartEntityPosition.y - game.input:getMouseY())
        entity.position.y = applyGrid(game.input:getMouseY() - draggingStartOffset.y)
      else
        entity.size.height = draggingStartEntitySize.h + draggingStartEntityPosition.y - game.input:getMouseY()
        entity.position.y = game.input:getMouseY() - draggingStartOffset.y
      end
    end
    if draggingAction == 2 then
      -- Top right
      if game.input:isKeyDown("shift") then
        entity.size.height = applyGrid(draggingStartEntitySize.h + draggingStartEntityPosition.y - game.input:getMouseY())
        entity.position.y = applyGrid(game.input:getMouseY() - draggingStartOffset.y)

        entity.size.width = applyGrid(-entity.position.x + game.input:getMouseX())
      else
        entity.size.height = draggingStartEntitySize.h + draggingStartEntityPosition.y - game.input:getMouseY()
        entity.position.y = game.input:getMouseY() - draggingStartOffset.y

        entity.size.width = -entity.position.x + game.input:getMouseX()
      end
    end
    if draggingAction == 3 then
      -- Middle left
      if game.input:isKeyDown("shift") then
        entity.size.width = applyGrid(draggingStartEntitySize.w + draggingStartEntityPosition.x - game.input:getMouseX())
        entity.position.x = applyGrid(game.input:getMouseX() - draggingStartOffset.x)
      else
        entity.size.width = draggingStartEntitySize.w + draggingStartEntityPosition.x - game.input:getMouseX()
        entity.position.x = game.input:getMouseX() - draggingStartOffset.x
      end
    end
    if draggingAction == 4 then
      -- Move entity
      if game.input:isKeyDown("shift") then
        entity.position.x = applyGrid(game.input:getMouseX()) - applyGrid(draggingStartOffset.x)
        entity.position.y = applyGrid(game.input:getMouseY()) - applyGrid(draggingStartOffset.y)
      else
        entity.position.x = game.input:getMouseX() - draggingStartOffset.x
        entity.position.y = game.input:getMouseY() - draggingStartOffset.y
      end
    end
    if draggingAction == 5 then
      -- Middle right
      if game.input:isKeyDown("shift") then
        entity.size.width = applyGrid(-entity.position.x + game.input:getMouseX() - draggingStartOffset.x + draggingStartEntitySize.w)
      else
        entity.size.width = -entity.position.x + game.input:getMouseX() - draggingStartOffset.x + draggingStartEntitySize.w
      end
    end
    if draggingAction == 6 then
      -- Bottom left
      if game.input:isKeyDown("shift") then
        entity.size.width = applyGrid(draggingStartEntitySize.w + draggingStartEntityPosition.x - game.input:getMouseX())
        entity.position.x = applyGrid(game.input:getMouseX() - draggingStartOffset.x)

        entity.size.height = applyGrid(-entity.position.y + game.input:getMouseY() - draggingStartOffset.y + draggingStartEntitySize.h)
      else
        entity.size.width = draggingStartEntitySize.w + draggingStartEntityPosition.x - game.input:getMouseX()
        entity.position.x = game.input:getMouseX() - draggingStartOffset.x

        entity.size.height = -entity.position.y + game.input:getMouseY() - draggingStartOffset.y + draggingStartEntitySize.h
      end
    end
    if draggingAction == 7 then
      -- Middle bottom
      if game.input:isKeyDown("shift") then
        entity.size.height = applyGrid(-entity.position.y + game.input:getMouseY() - draggingStartOffset.y + draggingStartEntitySize.h)
      else
        entity.size.height = -entity.position.y + game.input:getMouseY() - draggingStartOffset.y + draggingStartEntitySize.h
      end
    end
    if draggingAction == 8 then
      -- Bottom right
      if game.input:isKeyDown("shift") then
        entity.size.width = applyGrid(-entity.position.x + game.input:getMouseX() - draggingStartOffset.x + draggingStartEntitySize.w)
        entity.size.height = applyGrid(-entity.position.y + game.input:getMouseY() - draggingStartOffset.y + draggingStartEntitySize.h)
      else
        entity.size.width = -entity.position.x + game.input:getMouseX() - draggingStartOffset.x + draggingStartEntitySize.w
        entity.size.height = -entity.position.y + game.input:getMouseY() - draggingStartOffset.y + draggingStartEntitySize.h
      end
    end

    entity.size.width = math.max(entity.size.width, 0)
    entity.size.height = math.max(entity.size.height, 0)

    -- Mouse is released => no dragging actions
    if not game.input:isMouseButtonDown(1) then
      if draggingAction ~= - 1 then
        entity._meta:addComponent("se-entity-transform-done")
      end
      draggingAction = -1
    end
  end

  -- Mouse is released => no dragging actions
  if not game.input:isMouseButtonDown(1) then
    -- We reset it here just in case no selected entities are selected
    draggingAction = -1
  end

  -- Not dragging, select an entity
  if(draggingAction == -1) then
    local selectedEntities = {}
    for i, entity in ipairs(entities) do
      -- Find all candidate entities to be selected
      if game.input:isMouseInRectangle(entity.position.x, entity.position.y, entity.size.width, entity.size.height) then
        entity._meta:addComponent("se-hover")

        if game.input:isMouseClicked() then
          selectedEntities[#selectedEntities + 1] = entity
        end
      else
        entity._meta:removeComponent("se-hover")
      end

      -- Remove selection from all selected entities
      if game.input:isMouseClicked() then
        entity._meta:removeComponent("se-selected")
      end
    end

    if game.input:isMouseClicked() and #selectedEntities > 0 then
      local smallestEntity = selectedEntities[1]
      -- Find the smallest entity and select it
      for i, entity in ipairs(selectedEntities) do
        if entity.size.width * entity.size.height < smallestEntity.size.width * smallestEntity.size.height then
          smallestEntity = entity
        end
      end

      smallestEntity._meta:addComponent("se-selected")
    end
  end
end
