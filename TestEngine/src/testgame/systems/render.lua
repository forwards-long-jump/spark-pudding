function getRequiredComponents()
  return {players = {"position", "size", "color"}, texts = {"color", "position", "text"}}
end

function render(g)
  game.camera:applyTransforms(g)
  for i, entity in ipairs(players) do
    pos = entity.position
    size = entity.size
    color = entity.color

    g:setColor(game.color:fromRGB(color.r, color.g, color.b))
    g:fillRect(pos.x, pos.y, size.width, size.height)
  end
  
  g:setColor(game.color:fromRGBA(253, 99, 128, 25))
  g:fillRect(0, 0, 1280, 720)
  
  
  for i, entity in ipairs(texts) do
    pos = entity.position
    size = entity.size
    color = entity.color

    g:setColor(game.color:fromRGB(color.r, color.g, color.b))
    g:drawString(entity.text.value, pos.x, pos.y)
  end
  
  
  g:setColor(game.color:fromRGB(253, 99, 128))
  g:fillRect(620, 340, 40, 40)

  game.camera:resetTransforms(g)
  g:drawString("FPS: " .. game.core:getFPS(), 20, 20)
  
end
