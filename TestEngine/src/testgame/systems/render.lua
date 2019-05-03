function getRequiredComponents()
  return {players = {"position", "size", "color"}, texts = {"color", "position", "text"}, mice = {"position", "size", "color", "mouse"}}
end

function render(g)
  game.camera:applyTransforms(g)
  local renderCount = 0
  for i, entity in ipairs(players) do
    pos = entity.position
    size = entity.size
    color = entity.color
    if game.camera:isInView(pos.x, pos.y, size.width, size.height) then
      renderCount = renderCount + 1
      g:setColor(game.color:fromRGB(color.r, color.g, color.b))
      g:fillRect(pos.x, pos.y, size.width, size.height)
    end
  end

  g:setColor(game.color:fromRGBA(0, 0, 208, 5))
  g:fillRect(0, 0, game.core:getGameWidth(), game.core:getGameHeight())



  for i, entity in ipairs(texts) do
    pos = entity.position
    size = entity.size
    color = entity.color

    g:setColor(game.color:fromRGB(color.r, color.g, color.b))
    g:drawString(entity.text.value, pos.x, pos.y)
  end

  for i, entity in ipairs(mice) do
    pos = entity.position
    size = entity.size
    color = entity.color

    g:setColor(game.color:fromRGB(color.r, color.g, color.b))
    g:fillRect(pos.x, pos.y, size.width, size.height)
  end

  game.camera:resetTransforms(g)
  g:drawString("FPS: " .. game.core:getFPS(), 20, 20)

end
