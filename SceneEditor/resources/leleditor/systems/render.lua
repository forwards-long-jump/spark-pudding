function getRequiredComponents()
  return {"size", "position"}
end

function render()
  game.camera:applyTransforms(g:getContext())
  
  for i, entity in ipairs(entities) do
    g:setColor(game.color:fromRGBA(255, 0, 0, 200))
    g:drawRect(entity.position, entity.size)
  end
  game.camera:resetTransforms(g:getContext())
  
  g:setColor(game.color:fromRGB(0, 0, 0))
  g:drawString("Editing mode", 20, 20)
  g:drawString("FPS:" .. game.core:getFPS(), 20, 40)
end