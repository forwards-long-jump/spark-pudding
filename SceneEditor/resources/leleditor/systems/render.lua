function getRequiredComponents()
  return {entities = {"size", "position"}, selectedEntities = {"size", "position", "se-selected"}, hoveredEntities =  {"size", "position", "se-hover"}}
end

local editingTick = 0
function render()
  editingTick = editingTick + 1
  game.camera:applyTransforms(g:getContext())

  local penWidth = 2 / game.camera:getScaling()
  for i, entity in ipairs(selectedEntities) do
    g:setColor(game.color:fromRGBA(255, 255, 255, 150))
    g:fillRect(entity.position, entity.size)

    g:setAnimatedDashedLine(penWidth, 9 / game.camera:getScaling(), editingTick * 0.4 / game.camera:getScaling())
    g:setColor(game.color:fromRGBA(0, 0, 0, 200))
    g:drawRect(entity.position, entity.size)


  end

  for i, entity in ipairs(hoveredEntities) do
    g:setColor(game.color:fromRGBA(255, 255, 255, 50))
    g:fillRect(entity.position, entity.size)
  end

  game.camera:resetTransforms(g:getContext())

  g:setColor(game.color:fromRGB(0, 0, 0))
  g:drawString("Editing mode", 20, 20)
  g:drawString("FPS:" .. game.core:getFPS(), 20, 40)
end
