function getRequiredComponents()
  return {entities = {"size", "position"}, selectedEntities = {"size", "position", "se-selected"}, hoveredEntities =  {"size", "position", "se-hover"}}
end

function renderStart()
end

function renderEnd()
  editingTick = game.core:getEditingTick()
  
  game.camera:applyTransforms(g:getContext())

  local penWidth = 2 / game.camera:getScaling()
  local cursorSize = 10 / game.camera:getScaling()

  for i, entity in ipairs(selectedEntities) do
    g:setColor(game.color:fromRGBA(255, 255, 255, 150))
    g:fillRect(entity.position, entity.size)

    g:setAnimatedDashedLine(penWidth, 9 / game.camera:getScaling(), editingTick * 0.4 / game.camera:getScaling())
    g:setColor(game.color:fromRGBA(0, 0, 0, 200))
    g:drawRect(entity.position, entity.size)

    -- Draw the resizing cursors
    -- TODO: Use images

    local selectedHoverIndex = entity["se-selected"].hoverResizerIndex
    local draggerColorSelected = game.color:fromRGBA(0, 0, 0, 255)
    local draggerColor = game.color:fromRGBA(255, 255, 255, 200)

    for i = 0, 8 do
      local x = entity.position.x - cursorSize / 2 + (i % 3) * entity.size.width / 2
      local y = entity.position.y - cursorSize / 2 + math.floor(i / 3) * entity.size.height / 2
      if(selectedHoverIndex == i) then g:setColor(draggerColorSelected) else g:setColor(draggerColor) end
      g:fillRect(x, y, cursorSize, cursorSize)
    end
  end

  for i, entity in ipairs(hoveredEntities) do
    g:setColor(game.color:fromRGBA(255, 255, 255, 50))
    g:fillRect(entity.position, entity.size)
  end
  
  if game.input:isKeyDown("shift") then
   g:setAnimatedDashedLine(1, 1)
    g:setColor(game.color:fromRGBA(255, 255, 255, 150))
    for i= game.camera:getX() / game.camera:getScaling(), (game.core:getGameWidth() +  game.camera:getX()) / game.camera:getScaling(), 64 do
      for j=game.camera:getY() / game.camera:getScaling(), (game.core:getGameHeight() +  game.camera:getY()) / game.camera:getScaling(), 64 do
        g:drawRect(math.floor(i / 64) * 64, math.floor(j / 64) * 64, 64, 64)
      end
    end
  end

  game.camera:resetTransforms(g:getContext())

  g:setColor(game.color:fromRGB(0, 0, 0))
  g:drawString("Editing mode", 20, 20)
  g:drawString("FPS:" .. game.core:getFPS(), 20, 40)
end
